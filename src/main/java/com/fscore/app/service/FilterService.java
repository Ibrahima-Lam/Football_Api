package com.fscore.app.service;

import com.fscore.app.util.GenericSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FilterService {

    private static final Map<String, String[]> SEARCH_PATHS = Map.ofEntries(
        Map.entry("apiKeyRepository", new String[]{"name", "user.name"}),
        Map.entry("apiUserRepository", new String[]{"name", "email"}),
        Map.entry("auditLogRepository", new String[]{"endpoint", "method", "apiKey.name"}),
        Map.entry("bookmakerRepository", new String[]{"name"}),
        Map.entry("cityRepository", new String[]{"name", "country.name"}),
        Map.entry("coachRepository", new String[]{"fullName", "firstName", "lastName", "country.name"}),
        Map.entry("competitionRepository", new String[]{"name", "shortName", "country.name", "confederation.name"}),
        Map.entry("confederationRepository", new String[]{"name", "acronym", "continent.name"}),
        Map.entry("continentRepository", new String[]{"code", "name"}),
        Map.entry("contractRepository", new String[]{"player.fullName", "team.name"}),
        Map.entry("countryRepository", new String[]{"name", "officialName", "iso2", "iso3", "fifaCode", "continent.name"}),
        Map.entry("groupRepository", new String[]{"name", "stage.name"}),
        Map.entry("groupTeamRepository", new String[]{"group.name", "team.name"}),
        Map.entry("headToHeadRepository", new String[]{"team1.name", "team2.name"}),
        Map.entry("injuryRepository", new String[]{"player.fullName", "team.name", "injuryType"}),
        Map.entry("lineupRepository", new String[]{"player.fullName", "team.name", "position"}),
        Map.entry("matchEventRepository", new String[]{"player.fullName", "team.name", "eventType"}),
        Map.entry("matchFormationRepository", new String[]{"team.name", "formation"}),
        Map.entry("matchPenaltyShootoutShotRepository", new String[]{"player.fullName", "team.name", "status"}),
        Map.entry("matchRefereeRepository", new String[]{"referee.fullName"}),
        Map.entry("matchRepository", new String[]{"homeTeam.name", "awayTeam.name", "status", "period"}),
        Map.entry("matchStatisticsPlayerRepository", new String[]{"player.fullName", "team.name"}),
        Map.entry("matchStatisticsTeamRepository", new String[]{"team.name"}),
        Map.entry("mediaRepository", new String[]{"title", "url", "entityType"}),
        Map.entry("newsRepository", new String[]{"title", "slug", "author", "team.name", "player.fullName"}),
        Map.entry("oddHistoryRepository", new String[]{"market", "selection", "bookmaker.name"}),
        Map.entry("oddRepository", new String[]{"market", "selection", "bookmaker.name"}),
        Map.entry("playerAwardRepository", new String[]{"player.fullName", "trophy.name"}),
        Map.entry("playerRepository", new String[]{"fullName", "firstName", "lastName", "birthPlace", "country.name"}),
        Map.entry("playerSeasonRegistrationRepository", new String[]{"player.fullName", "team.name", "position"}),
        Map.entry("playerSeasonStatRepository", new String[]{"player.fullName", "team.name"}),
        Map.entry("rateLimitRepository", new String[]{"apiKey.name"}),
        Map.entry("refereeRepository", new String[]{"fullName", "firstName", "lastName", "country.name"}),
        Map.entry("roundRepository", new String[]{"name", "slug", "stage.name"}),
        Map.entry("seasonRepository", new String[]{"name", "competition.name"}),
        Map.entry("sponsorLinkRepository", new String[]{"sponsor.nom"}),
        Map.entry("sponsorRepository", new String[]{"nom", "websiteUrl"}),
        Map.entry("stadiumRepository", new String[]{"name", "address", "country.name", "city.name"}),
        Map.entry("stageRepository", new String[]{"name", "season.name"}),
        Map.entry("standingRepository", new String[]{"team.name", "form"}),
        Map.entry("suspensionRepository", new String[]{"player.fullName", "team.name", "reason"}),
        Map.entry("teamCoachRepository", new String[]{"team.name", "coach.fullName"}),
        Map.entry("teamRepository", new String[]{"name", "shortName", "code", "country.name", "stadium.name"}),
        Map.entry("teamSeasonParticipationRepository", new String[]{"team.name", "season.name", "entryType"}),
        Map.entry("teamTrophyRepository", new String[]{"team.name", "trophy.name"}),
        Map.entry("transferRepository", new String[]{"player.fullName", "fromTeam.name", "toTeam.name"}),
        Map.entry("translationRepository", new String[]{"entityType", "fieldName", "translatedValue", "language"}),
        Map.entry("trophyRepository", new String[]{"name", "competition.name"})
    );

    private final Map<String, JpaSpecificationExecutor<?>> repositories;

    public FilterService(Map<String, JpaSpecificationExecutor<?>> repositories) {
        this.repositories = repositories;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> Page<T> find(String repoBeanName, Pageable pageable, Map<String, String> params) {
        JpaSpecificationExecutor<T> repository = (JpaSpecificationExecutor<T>) repositories.get(repoBeanName);
        if (repository == null) {
            throw new IllegalStateException("Unknown repository: " + repoBeanName);
        }
        String[] searchPaths = SEARCH_PATHS.getOrDefault(repoBeanName, new String[0]);
        Specification<T> spec = GenericSpecifications.build(params, searchPaths);
        return repository.findAll(spec, pageable);
    }
}
