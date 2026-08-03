package com.fscore.app.service;

import com.fscore.app.dto.client.*;
import com.fscore.app.entity.*;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.*;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientApiService {

    private static final List<String> LIVE_STATUSES = List.of(
            "LIVE", "IN_PLAY", "1ST_HALF", "2ND_HALF", "HT", "ET", "PENALTY_SHOOTOUT");

    private final MatchRepository matchRepository;
    private final MatchEventRepository matchEventRepository;
    private final LineupRepository lineupRepository;
    private final MatchStatisticsTeamRepository matchStatisticsTeamRepository;
    private final MatchStatisticsPlayerRepository matchStatisticsPlayerRepository;
    private final StandingRepository standingRepository;
    private final SeasonRepository seasonRepository;
    private final CompetitionRepository competitionRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final TeamSeasonParticipationRepository teamSeasonParticipationRepository;
    private final PlayerSeasonStatRepository playerSeasonStatRepository;
    private final NewsRepository newsRepository;
    private final MatchRefereeRepository matchRefereeRepository;
    private final TeamCoachRepository teamCoachRepository;
    private final PlayerSeasonRegistrationRepository playerSeasonRegistrationRepository;
    private final InjuryRepository injuryRepository;
    private final SuspensionRepository suspensionRepository;
    private final LiveScoreService liveScoreService;

    public ClientApiService(MatchRepository matchRepository,
                            MatchEventRepository matchEventRepository,
                            LineupRepository lineupRepository,
                            MatchStatisticsTeamRepository matchStatisticsTeamRepository,
                            MatchStatisticsPlayerRepository matchStatisticsPlayerRepository,
                            StandingRepository standingRepository,
                            SeasonRepository seasonRepository,
                            CompetitionRepository competitionRepository,
                            TeamRepository teamRepository,
                            PlayerRepository playerRepository,
                            TeamSeasonParticipationRepository teamSeasonParticipationRepository,
                            PlayerSeasonStatRepository playerSeasonStatRepository,
                            NewsRepository newsRepository,
                            MatchRefereeRepository matchRefereeRepository,
                            TeamCoachRepository teamCoachRepository,
                            PlayerSeasonRegistrationRepository playerSeasonRegistrationRepository,
                            InjuryRepository injuryRepository,
                            SuspensionRepository suspensionRepository,
                            LiveScoreService liveScoreService) {
        this.matchRepository = matchRepository;
        this.matchEventRepository = matchEventRepository;
        this.lineupRepository = lineupRepository;
        this.matchStatisticsTeamRepository = matchStatisticsTeamRepository;
        this.matchStatisticsPlayerRepository = matchStatisticsPlayerRepository;
        this.standingRepository = standingRepository;
        this.seasonRepository = seasonRepository;
        this.competitionRepository = competitionRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.teamSeasonParticipationRepository = teamSeasonParticipationRepository;
        this.playerSeasonStatRepository = playerSeasonStatRepository;
        this.newsRepository = newsRepository;
        this.matchRefereeRepository = matchRefereeRepository;
        this.teamCoachRepository = teamCoachRepository;
        this.playerSeasonRegistrationRepository = playerSeasonRegistrationRepository;
        this.injuryRepository = injuryRepository;
        this.suspensionRepository = suspensionRepository;
        this.liveScoreService = liveScoreService;
    }

    @Transactional(readOnly = true)
    public PageInfo<MatchCard> matches(LocalDate date, String seasonId, String competitionId, String teamId,
                                       Boolean live, int page, int size) {
        Specification<Match> spec = matchSpec(date, seasonId, competitionId, teamId, live);
        Page<Match> result = matchRepository.findAll(spec,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "kickoff")));
        Page<MatchCard> cards = result.map(this::toMatchCard);
        return PageInfo.from(cards);
    }

    @Transactional(readOnly = true)
    public MatchDetail matchDetail(String id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + id));
        List<MatchEventItem> events = matchEventRepository.findByMatchIdOrderByMinuteAscExtraMinuteAsc(id)
                .stream().map(this::toMatchEventItem).toList();
        List<TeamStatItem> teamStats = matchStatisticsTeamRepository.findByMatchId(id)
                .stream().map(this::toTeamStatItem).toList();
        List<PlayerStatItem> playerStats = matchStatisticsPlayerRepository.findByMatchIdOrderByGoalsDescAssistsDesc(id)
                .stream().map(this::toPlayerStatItem).toList();
        List<LineupItem> lineups = lineupRepository.findByMatchIdOrderByStarterDescShirtNumberAsc(id)
                .stream().map(this::toLineupItem).toList();
        return new MatchDetail(
                toMatchCard(match),
                match.getReferee() != null ? match.getReferee().getFullName() : null,
                match.getStadium() != null && match.getStadium().getCity() != null ? match.getStadium().getCity().getName() : null,
                match.getAttendance(),
                match.getWeather(),
                match.getTemperature(),
                match.getWindSpeed(),
                match.getNote(),
                match.getHomePenaltyForm(),
                match.getAwayPenaltyForm(),
                match.getFirstHalfStart(),
                match.getSecondHalfStart(),
                match.getExtraTimeStart(),
                match.getPenaltyShootoutStart(),
                events,
                teamStats,
                playerStats,
                lineups);
    }

    @Transactional
    public MatchEventItem addMatchEvent(String matchId, MatchEventCreateRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + matchId));
        if (request.getMinute() == null) {
            throw new IllegalArgumentException("minute is required");
        }
        if (request.getPeriod() == null || request.getPeriod().isBlank()) {
            throw new IllegalArgumentException("period is required");
        }
        if (request.getEventType() == null || request.getEventType().isBlank()) {
            throw new IllegalArgumentException("eventType is required");
        }
        if (request.getTeamId() == null || request.getTeamId().isBlank()) {
            throw new IllegalArgumentException("teamId is required");
        }
        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + request.getTeamId()));
        Player player = request.getPlayerId() != null
                ? playerRepository.findById(request.getPlayerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + request.getPlayerId()))
                : null;
        Player relatedPlayer = request.getRelatedPlayerId() != null
                ? playerRepository.findById(request.getRelatedPlayerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + request.getRelatedPlayerId()))
                : null;

        MatchEvent event = MatchEvent.builder()
                .match(match)
                .team(team)
                .player(player)
                .relatedPlayer(relatedPlayer)
                .minute(request.getMinute())
                .extraMinute(request.getExtraMinute())
                .period(request.getPeriod())
                .eventType(request.getEventType())
                .detail(request.getDetail())
                .comments(request.getComments())
                .varReviewed(Boolean.TRUE.equals(request.getVarReviewed()))
                .build();
        MatchEvent saved = matchEventRepository.save(event);
        liveScoreService.broadcastMatchEvent(saved);
        applyScoreForGoal(match, saved);
        return toMatchEventItem(saved);
    }

    private void applyScoreForGoal(Match match, MatchEvent event) {
        String type = event.getEventType() == null ? "" : event.getEventType().toUpperCase();
        if (!List.of("GOAL", "PENALTY", "PENALTY_GOAL").contains(type)) {
            return;
        }
        if (match.getHomeTeam() == null || match.getAwayTeam() == null || event.getTeam() == null) {
            return;
        }
        if (match.getHomeTeam().getId().equals(event.getTeam().getId())) {
            match.setHomeScore((match.getHomeScore() == null ? 0 : match.getHomeScore()) + 1);
        } else if (match.getAwayTeam().getId().equals(event.getTeam().getId())) {
            match.setAwayScore((match.getAwayScore() == null ? 0 : match.getAwayScore()) + 1);
        } else {
            return;
        }
        matchRepository.save(match);
        liveScoreService.broadcastMatchUpdate(match);
    }

    @Transactional(readOnly = true)
    public List<CompetitionRef> competitions() {
        return competitionRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .filter(c -> Boolean.TRUE.equals(c.getActive()))
                .map(c -> toCompetitionRef(c, true))
                .toList();
    }

    @Transactional(readOnly = true)
    public CompetitionDetail competitionDetail(String id) {
        Competition competition = competitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Competition not found with id: " + id));
        List<SeasonRef> seasons = seasonRepository.findByCompetitionIdOrderByYearStartDesc(id)
                .stream().map(this::toSeasonRef).toList();
        return new CompetitionDetail(toCompetitionRef(competition, true), seasons);
    }

    @Transactional(readOnly = true)
    public List<SeasonRef> seasons(String competitionId) {
        return seasonRepository.findByCompetitionIdOrderByYearStartDesc(competitionId)
                .stream().map(this::toSeasonRef).toList();
    }

    @Transactional(readOnly = true)
    public List<StandingItem> standings(String seasonId, String stageId) {
        List<Standing> standings = stageId != null && !stageId.isBlank()
                ? standingRepository.findBySeasonIdAndStageIdOrderByRankPositionAsc(seasonId, stageId)
                : standingRepository.findBySeasonIdOrderByRankPositionAsc(seasonId);
        return standings.stream().map(this::toStandingItem).toList();
    }

    @Transactional(readOnly = true)
    public List<TeamRef> competitionTeams(String seasonId) {
        if (seasonId == null || seasonId.isBlank()) {
            return teamRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                    .filter(t -> t.getDeletedAt() == null)
                    .map(this::toTeamRef)
                    .toList();
        }
        return teamSeasonParticipationRepository.findBySeasonId(seasonId).stream()
                .map(p -> toTeamRef(p.getTeam()))
                .distinct()
                .toList();
    }

    @Transactional(readOnly = true)
    public TeamDetail teamDetail(String id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
        Country country = team.getCountry();
        Stadium stadium = team.getStadium();
        return new TeamDetail(
                team.getId(),
                team.getName(),
                team.getShortName(),
                team.getCode(),
                team.getFounded(),
                team.getLogo(),
                team.getKitPrimaryColor(),
                team.getKitSecondaryColor(),
                team.getWebsite(),
                team.getDescription(),
                country != null ? country.getName() : null,
                country != null ? country.getIso2() : null,
                country != null ? country.getFlagUrl() : null,
                stadium != null ? stadium.getName() : null,
                stadium != null && stadium.getCity() != null ? stadium.getCity().getName() : null,
                stadium != null ? stadium.getCapacity() : null);
    }

    @Transactional(readOnly = true)
    public List<SquadPlayerItem> teamSquad(String teamId, String seasonId) {
        Season season = resolveTeamSeason(teamId, seasonId);
        List<PlayerSeasonRegistration> registrations = season != null
                ? playerSeasonRegistrationRepository.findByTeamIdAndSeasonId(teamId, season.getId())
                : playerSeasonRegistrationRepository.findByTeamId(teamId);
        return registrations.stream()
                .sorted(Comparator
                        .comparing(PlayerSeasonRegistration::getShirtNumber, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(PlayerSeasonRegistration::getPlayer, Comparator.comparing(Player::getFullName)))
                .map(this::toSquadPlayerItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PlayerSeasonStatItem> teamPlayerStats(String teamId, String seasonId) {
        Season season = resolveTeamSeason(teamId, seasonId);
        if (season == null) {
            return List.of();
        }
        return playerSeasonStatRepository.findByTeamIdAndSeasonId(teamId, season.getId()).stream()
                .sorted(Comparator
                        .comparing(PlayerSeasonStat::getGoals, Comparator.nullsFirst(Comparator.naturalOrder())).reversed()
                        .thenComparing(PlayerSeasonStat::getAppearances, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
                .map(this::toPlayerSeasonStatItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TeamSuspensionItem> teamSuspensions(String teamId) {
        return suspensionRepository.findByTeamIdOrderByStartDateDesc(teamId).stream()
                .map(this::toTeamSuspensionItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TeamInjuryItem> teamInjuries(String teamId) {
        return injuryRepository.findByTeamIdOrderByStartDateDesc(teamId).stream()
                .map(this::toTeamInjuryItem)
                .toList();
    }

    private Season resolveTeamSeason(String teamId, String seasonId) {
        if (seasonId != null && !seasonId.isBlank()) {
            return seasonRepository.findById(seasonId).orElse(null);
        }
        return teamSeasonParticipationRepository.findByTeamId(teamId).stream()
                .map(TeamSeasonParticipation::getSeason)
                .max(Comparator.comparing(Season::getYearStart))
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<PlayerSeasonStatItem> playerSeasonStats(String seasonId, String stat) {
        String type = stat == null ? "" : stat.toLowerCase();
        Comparator<PlayerSeasonStat> comparator = switch (type) {
            case "assists" -> Comparator.comparing(PlayerSeasonStat::getAssists, Comparator.nullsFirst(Comparator.naturalOrder())).reversed();
            case "cards" -> Comparator
                    .comparing(PlayerSeasonStat::getYellowCards, Comparator.nullsFirst(Comparator.naturalOrder())).reversed()
                    .thenComparing(PlayerSeasonStat::getRedCards, Comparator.nullsFirst(Comparator.naturalOrder())).reversed();
            default -> Comparator.comparing(PlayerSeasonStat::getGoals, Comparator.nullsFirst(Comparator.naturalOrder())).reversed();
        };
        return playerSeasonStatRepository.findBySeasonId(seasonId).stream()
                .sorted(comparator)
                .map(this::toPlayerSeasonStatItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NewsItem> competitionNews(String competitionId, String teamId) {
        List<News> news;
        if (teamId != null && !teamId.isBlank()) {
            news = newsRepository.findByTeamIdAndDeletedAtIsNullOrderByPublishedAtDesc(teamId);
        } else if (competitionId != null && !competitionId.isBlank()) {
            news = newsRepository.findByCompetitionIdAndDeletedAtIsNullOrderByPublishedAtDesc(competitionId);
        } else {
            news = newsRepository.findByDeletedAtIsNullOrderByPublishedAtDesc();
        }
        return news.stream().map(this::toNewsItem).toList();
    }

    @Transactional(readOnly = true)
    public List<RefereeItem> seasonReferees(String seasonId) {
        Map<String, RefereeItem> byReferee = new LinkedHashMap<>();
        for (MatchReferee mr : matchRefereeRepository.findByMatchSeasonId(seasonId)) {
            Referee referee = mr.getReferee();
            if (referee == null) {
                continue;
            }
            String key = referee.getId();
            RefereeItem existing = byReferee.get(key);
            List<String> roles = existing != null
                    ? new ArrayList<>(existing.roles())
                    : new ArrayList<>();
            if (mr.getRole() != null && !roles.contains(mr.getRole())) {
                roles.add(mr.getRole());
            }
            byReferee.put(key, new RefereeItem(
                    referee.getId(),
                    referee.getFullName(),
                    referee.getPhoto(),
                    referee.getCategory(),
                    referee.getCountry() != null ? referee.getCountry().getName() : null,
                    referee.getCountry() != null ? referee.getCountry().getFlagUrl() : null,
                    (existing != null ? existing.matchesCount() : 0L) + 1L,
                    roles));
        }
        return new ArrayList<>(byReferee.values());
    }

    @Transactional(readOnly = true)
    public List<CoachItem> seasonCoaches(String seasonId) {
        return teamCoachRepository.findBySeasonId(seasonId).stream()
                .map(this::toCoachItem)
                .toList();
    }

    private Specification<Match> matchSpec(LocalDate date, String seasonId, String competitionId, String teamId,
                                           Boolean live) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (date != null) {
                LocalDateTime start = date.atStartOfDay();
                LocalDateTime end = date.plusDays(1).atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("kickoff"), start));
                predicates.add(cb.lessThan(root.get("kickoff"), end));
            }
            if (seasonId != null && !seasonId.isBlank()) {
                predicates.add(cb.equal(root.get("season").get("id"), seasonId));
            }
            if (competitionId != null && !competitionId.isBlank()) {
                predicates.add(cb.equal(root.get("season").get("competition").get("id"), competitionId));
            }
            if (teamId != null && !teamId.isBlank()) {
                Predicate home = cb.equal(root.get("homeTeam").get("id"), teamId);
                Predicate away = cb.equal(root.get("awayTeam").get("id"), teamId);
                predicates.add(cb.or(home, away));
            }
            if (Boolean.TRUE.equals(live)) {
                predicates.add(root.get("status").in(LIVE_STATUSES));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private MatchCard toMatchCard(Match match) {
        Season season = match.getSeason();
        return new MatchCard(
                match.getId(),
                match.getKickoff(),
                match.getStatus(),
                match.getPeriod(),
                match.getMinute(),
                match.getMinuteExtra(),
                match.getHomeScore(),
                match.getAwayScore(),
                match.getHomeHtScore(),
                match.getAwayHtScore(),
                match.getHomeEtScore(),
                match.getAwayEtScore(),
                match.getHomePenaltyScore(),
                match.getAwayPenaltyScore(),
                match.getHomePenaltyForm(),
                match.getAwayPenaltyForm(),
                toTeamRef(match.getHomeTeam()),
                toTeamRef(match.getAwayTeam()),
                toCompetitionRef(match.getSeason().getCompetition(), false),
                toSeasonRef(season),
                match.getStage() != null ? match.getStage().getName() : null,
                match.getGroup() != null ? match.getGroup().getName() : null,
                match.getRound() != null ? match.getRound().getName() : null,
                match.getRound() != null ? match.getRound().getNumber() : null,
                match.getStadium() != null ? match.getStadium().getName() : null);
    }

    private MatchEventItem toMatchEventItem(MatchEvent event) {
        return new MatchEventItem(
                event.getId(),
                event.getMinute(),
                event.getExtraMinute(),
                event.getPeriod(),
                toTeamRef(event.getTeam()),
                toPlayerRef(event.getPlayer()),
                toPlayerRef(event.getRelatedPlayer()),
                event.getEventType(),
                event.getDetail(),
                event.getComments(),
                event.getVarReviewed());
    }

    private TeamStatItem toTeamStatItem(MatchStatisticsTeam stats) {
        return new TeamStatItem(
                toTeamRef(stats.getTeam()),
                stats.getPossession(),
                stats.getShots(),
                stats.getShotsOnTarget(),
                stats.getShotsOffTarget(),
                stats.getShotsBlocked(),
                stats.getCorners(),
                stats.getFreeKicks(),
                stats.getGoalKicks(),
                stats.getThrowIns(),
                stats.getOffsides(),
                stats.getFouls(),
                stats.getYellowCards(),
                stats.getYellowRedCards(),
                stats.getRedCards(),
                stats.getPasses(),
                stats.getPassesAccurate(),
                stats.getTackles(),
                stats.getInterceptions(),
                stats.getClearances(),
                stats.getSaves(),
                stats.getXg(),
                stats.getXga());
    }

    private PlayerStatItem toPlayerStatItem(MatchStatisticsPlayer stats) {
        return new PlayerStatItem(
                toPlayerRef(stats.getPlayer()),
                toTeamRef(stats.getTeam()),
                stats.getMinutesPlayed(),
                stats.getGoals(),
                stats.getAssists(),
                stats.getShots(),
                stats.getShotsOnTarget(),
                stats.getXg(),
                stats.getKeyPasses(),
                stats.getPasses(),
                stats.getPassesAccurate(),
                stats.getLongBalls(),
                stats.getCrosses(),
                stats.getDribblesAttempted(),
                stats.getDribblesSucceeded(),
                stats.getTackles(),
                stats.getInterceptions(),
                stats.getClearances(),
                stats.getFoulsCommitted(),
                stats.getFoulsDrawn(),
                stats.getYellowCards(),
                stats.getRedCards(),
                stats.getSaves(),
                stats.getGoalsConceded(),
                stats.getRating());
    }

    private LineupItem toLineupItem(Lineup lineup) {
        return new LineupItem(
                toTeamRef(lineup.getTeam()),
                toPlayerRef(lineup.getPlayer()),
                lineup.getStarter(),
                lineup.getCaptain(),
                lineup.getShirtNumber(),
                lineup.getPosition(),
                lineup.getPositionX(),
                lineup.getPositionY(),
                lineup.getFormationSlot());
    }

    private StandingItem toStandingItem(Standing standing) {
        return new StandingItem(
                standing.getId(),
                standing.getRankPosition(),
                toTeamRef(standing.getTeam()),
                standing.getPlayed(),
                standing.getWins(),
                standing.getDraws(),
                standing.getLosses(),
                standing.getGoalsFor(),
                standing.getGoalsAgainst(),
                standing.getGoalDifference(),
                standing.getPoints(),
                standing.getForm(),
                standing.getGroup() != null ? standing.getGroup().getId() : null,
                standing.getGroup() != null ? standing.getGroup().getName() : null);
    }

    private CompetitionRef toCompetitionRef(Competition competition, boolean includeSeason) {
        SeasonRef current = null;
        if (includeSeason) {
            List<SeasonRef> seasons = seasonRepository.findByCompetitionIdOrderByYearStartDesc(competition.getId())
                    .stream().map(this::toSeasonRef).toList();
            current = seasons.stream()
                    .filter(s -> Boolean.TRUE.equals(s.current()))
                    .findFirst()
                    .orElse(seasons.isEmpty() ? null : seasons.get(0));
        }
        Country country = competition.getCountry();
        Confederation confederation = competition.getConfederation();
        return new CompetitionRef(
                competition.getId(),
                competition.getName(),
                competition.getShortName(),
                competition.getType(),
                competition.getGender(),
                competition.getAgeLevel(),
                competition.getSport(),
                competition.getLogo(),
                competition.getLevel(),
                country != null ? country.getName() : null,
                country != null ? country.getIso2() : null,
                country != null ? country.getFlagUrl() : null,
                confederation != null ? confederation.getName() : null,
                confederation != null ? confederation.getAcronym() : null,
                current);
    }

    private SeasonRef toSeasonRef(Season season) {
        return new SeasonRef(
                season.getId(),
                season.getName(),
                season.getYearStart(),
                season.getYearEnd(),
                season.getStartDate(),
                season.getEndDate(),
                season.getCurrent(),
                season.getStatus());
    }

    private TeamRef toTeamRef(Team team) {
        if (team == null) {
            return null;
        }
        Country country = team.getCountry();
        return new TeamRef(
                team.getId(),
                team.getName(),
                team.getShortName(),
                team.getCode(),
                team.getLogo(),
                team.getKitPrimaryColor(),
                country != null ? country.getIso2() : null,
                country != null ? country.getFlagUrl() : null);
    }

    private PlayerSeasonStatItem toPlayerSeasonStatItem(PlayerSeasonStat stats) {
        return new PlayerSeasonStatItem(
                toPlayerRef(stats.getPlayer()),
                toTeamRef(stats.getTeam()),
                stats.getAppearances(),
                stats.getAppearancesAsStarter(),
                stats.getMinutesPlayed(),
                stats.getGoals(),
                stats.getAssists(),
                stats.getShots(),
                stats.getShotsOnTarget(),
                stats.getYellowCards(),
                stats.getRedCards(),
                stats.getSaves(),
                stats.getCleanSheets(),
                stats.getAvgRating());
    }

    private NewsItem toNewsItem(News news) {
        return new NewsItem(
                news.getId(),
                news.getTitle(),
                news.getExcerpt(),
                news.getContent(),
                news.getImage(),
                news.getAuthor(),
                news.getPublishedAt(),
                news.getCompetition() != null ? news.getCompetition().getName() : null,
                toTeamRef(news.getTeam()),
                toPlayerRef(news.getPlayer()));
    }

    private CoachItem toCoachItem(TeamCoach teamCoach) {
        Coach coach = teamCoach.getCoach();
        return new CoachItem(
                coach.getId(),
                coach.getFullName(),
                coach.getPhoto(),
                teamCoach.getRole(),
                toTeamRef(teamCoach.getTeam()),
                teamCoach.getStartDate(),
                teamCoach.getEndDate(),
                teamCoach.getInterim());
    }

    private PlayerRef toPlayerRef(Player player) {
        if (player == null) {
            return null;
        }
        return new PlayerRef(
                player.getId(),
                player.getFullName(),
                player.getFirstName(),
                player.getLastName(),
                player.getPosition(),
                player.getPhoto(),
                player.getPreferredFoot());
    }

    private SquadPlayerItem toSquadPlayerItem(PlayerSeasonRegistration registration) {
        Player player = registration.getPlayer();
        Country country = player.getNationality();
        return new SquadPlayerItem(
                player.getId(),
                player.getFullName(),
                player.getFirstName(),
                player.getLastName(),
                registration.getPosition() != null ? registration.getPosition() : player.getPosition(),
                player.getPhoto(),
                player.getPreferredFoot(),
                registration.getShirtNumber(),
                registration.getCaptain(),
                registration.getStatus(),
                country != null ? country.getName() : null,
                country != null ? country.getFlagUrl() : null);
    }

    private TeamSuspensionItem toTeamSuspensionItem(Suspension suspension) {
        return new TeamSuspensionItem(
                suspension.getId(),
                toPlayerRef(suspension.getPlayer()),
                suspension.getCardType(),
                suspension.getReason(),
                suspension.getStartDate(),
                suspension.getEndDate(),
                suspension.getMatchesBanned(),
                suspension.getMatchesRemaining(),
                suspension.getStatus(),
                suspension.getCompetition() != null ? suspension.getCompetition().getName() : null,
                suspension.getSeason() != null ? suspension.getSeason().getName() : null);
    }

    private TeamInjuryItem toTeamInjuryItem(Injury injury) {
        return new TeamInjuryItem(
                injury.getId(),
                toPlayerRef(injury.getPlayer()),
                injury.getInjuryType(),
                injury.getBodyPart(),
                injury.getSeverity(),
                injury.getStartDate(),
                injury.getExpectedReturn(),
                injury.getActualReturn(),
                injury.getStatus());
    }
}
