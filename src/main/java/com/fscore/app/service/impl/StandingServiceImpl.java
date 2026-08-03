package com.fscore.app.service.impl;

import com.fscore.app.dto.response.StandingResponse;
import com.fscore.app.entity.*;
import com.fscore.app.exception.ResourceNotFoundException;
import com.fscore.app.repository.GroupRepository;
import com.fscore.app.repository.GroupTeamRepository;
import com.fscore.app.repository.MatchRepository;
import com.fscore.app.repository.StandingRepository;
import com.fscore.app.service.LiveScoreService;
import com.fscore.app.service.StandingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class StandingServiceImpl implements StandingService {

    private final StandingRepository repository;
    private final LiveScoreService liveScoreService;
    private final GroupRepository groupRepository;
    private final GroupTeamRepository groupTeamRepository;
    private final MatchRepository matchRepository;

    public StandingServiceImpl(StandingRepository repository, LiveScoreService liveScoreService,
                               GroupRepository groupRepository, GroupTeamRepository groupTeamRepository,
                               MatchRepository matchRepository) {
        this.repository = repository;
        this.liveScoreService = liveScoreService;
        this.groupRepository = groupRepository;
        this.groupTeamRepository = groupTeamRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public Page<Standing> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Standing> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Standing save(Standing entity) {
        Standing saved = repository.save(entity);
        liveScoreService.broadcastStanding(saved);
        return saved;
    }

    @Override
    public Standing update(String id, Standing entity) {
        Standing existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Standing not found with id: " + id));
        entity.setId(existing.getId());
        Standing updated = repository.save(entity);
        liveScoreService.broadcastStanding(updated);
        return updated;
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Standing not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<StandingResponse> calculate(String groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));

        Map<String, Row> rows = new LinkedHashMap<>();

        for (GroupTeam gt : groupTeamRepository.findByGroupId(groupId)) {
            if (gt.getTeam() != null) {
                rows.putIfAbsent(gt.getTeam().getId(), new Row(gt.getTeam()));
            }
        }

        List<Match> played = matchRepository.findByGroupId(groupId).stream()
            .filter(m -> m.getDeletedAt() == null)
            .filter(m -> m.getHomeScore() != null && m.getAwayScore() != null)
            .collect(Collectors.toList());

        for (Match m : played) {
            Team home = m.getHomeTeam();
            Team away = m.getAwayTeam();
            Row hr = rows.computeIfAbsent(home.getId(), id -> new Row(home));
            Row ar = rows.computeIfAbsent(away.getId(), id -> new Row(away));
            int hs = m.getHomeScore();
            int as = m.getAwayScore();
            applyResult(hr, hs, as, true, m.getKickoff());
            applyResult(ar, as, hs, false, m.getKickoff());
        }

        List<Row> sorted = rows.values().stream()
            .sorted(Comparator
                .comparingInt((Row r) -> r.points).reversed()
                .thenComparingInt((Row r) -> r.goalDifference()).reversed()
                .thenComparing((a, b) -> confrontation(a, b, played))
                .thenComparingInt((Row r) -> r.goalsFor).reversed()
                .thenComparingInt((Row r) -> r.goalsAgainst)
                .thenComparing(r -> r.team.getId()))
            .collect(Collectors.toList());

        List<StandingResponse> result = new ArrayList<>();
        int rank = 1;
        for (Row r : sorted) {
            result.add(r.toResponse(group, rank++));
        }
        return result;
    }

    @Override
    public List<StandingResponse> saveCalculated(String groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
        List<StandingResponse> calculated = calculate(groupId);

        List<Standing> entities = new ArrayList<>();
        for (StandingResponse r : calculated) {
            Standing s = Standing.builder()
                .season(group.getStage().getSeason())
                .stage(group.getStage())
                .group(group)
                .team(Team.builder().id(r.getTeamId()).build())
                .rankPosition(r.getRankPosition())
                .played(r.getPlayed())
                .wins(r.getWins())
                .draws(r.getDraws())
                .losses(r.getLosses())
                .homeWins(r.getHomeWins())
                .homeDraws(r.getHomeDraws())
                .homeLosses(r.getHomeLosses())
                .awayWins(r.getAwayWins())
                .awayDraws(r.getAwayDraws())
                .awayLosses(r.getAwayLosses())
                .goalsFor(r.getGoalsFor())
                .goalsAgainst(r.getGoalsAgainst())
                .goalDifference(r.getGoalDifference())
                .points(r.getPoints())
                .form(r.getForm())
                .build();
            entities.add(s);
        }

        repository.deleteByGroupId(groupId);
        if (!entities.isEmpty()) {
            repository.saveAll(entities);
            for (Standing s : entities) {
                liveScoreService.broadcastStanding(s);
            }
        }
        return calculated;
    }

    private void applyResult(Row row, int scored, int conceded, boolean home, LocalDateTime kickoff) {
        row.played++;
        row.goalsFor += scored;
        row.goalsAgainst += conceded;
        char letter;
        if (scored > conceded) {
            row.wins++;
            if (home) row.homeWins++; else row.awayWins++;
            row.points += 3;
            letter = 'V';
        } else if (scored == conceded) {
            row.draws++;
            if (home) row.homeDraws++; else row.awayDraws++;
            row.points += 1;
            letter = 'N';
        } else {
            row.losses++;
            if (home) row.homeLosses++; else row.awayLosses++;
            letter = 'D';
        }
        row.formHistory.add(new FormEntry(kickoff, letter));
    }

    private int confrontation(Row a, Row b, List<Match> played) {
        if (a.team.getId().equals(b.team.getId())) return 0;
        List<Match> between = played.stream()
            .filter(m -> m.getHomeTeam().getId().equals(a.team.getId())
                    && m.getAwayTeam().getId().equals(b.team.getId())
                || m.getHomeTeam().getId().equals(b.team.getId())
                    && m.getAwayTeam().getId().equals(a.team.getId()))
            .collect(Collectors.toList());
        if (between.isEmpty() || between.size() > 2) return 0;
        if (between.size() == 2) {
            int aGoals = 0;
            int bGoals = 0;
            for (Match m : between) {
                if (m.getHomeTeam().getId().equals(a.team.getId())) {
                    aGoals += m.getHomeScore();
                    bGoals += m.getAwayScore();
                } else {
                    aGoals += m.getAwayScore();
                    bGoals += m.getHomeScore();
                }
            }
            return aGoals - bGoals;
        }
        Match m = between.get(0);
        if (m.getHomeTeam().getId().equals(a.team.getId())) {
            return m.getHomeScore() - m.getAwayScore();
        }
        return m.getAwayScore() - m.getHomeScore();
    }

    private record FormEntry(LocalDateTime kickoff, char letter) {}

    private static class Row {
        final Team team;
        int played;
        int wins;
        int draws;
        int losses;
        int homeWins;
        int homeDraws;
        int homeLosses;
        int awayWins;
        int awayDraws;
        int awayLosses;
        int goalsFor;
        int goalsAgainst;
        int points;
        final List<FormEntry> formHistory = new ArrayList<>();

        Row(Team team) {
            this.team = team;
        }

        int goalDifference() {
            return goalsFor - goalsAgainst;
        }

        String form() {
            return formHistory.stream()
                .sorted(Comparator.comparing(FormEntry::kickoff))
                .map(e -> String.valueOf(e.letter()))
                .collect(Collectors.joining());
        }

        StandingResponse toResponse(Group group, int rank) {
            return StandingResponse.builder()
                .seasonId(group.getStage().getSeason().getId())
                .stageId(group.getStage().getId())
                .groupId(group.getId())
                .teamId(team.getId())
                .teamName(team.getName())
                .rankPosition(rank)
                .played(played)
                .wins(wins)
                .draws(draws)
                .losses(losses)
                .homeWins(homeWins)
                .homeDraws(homeDraws)
                .homeLosses(homeLosses)
                .awayWins(awayWins)
                .awayDraws(awayDraws)
                .awayLosses(awayLosses)
                .goalsFor(goalsFor)
                .goalsAgainst(goalsAgainst)
                .goalDifference(goalDifference())
                .points(points)
                .form(form())
                .build();
        }
    }
}
