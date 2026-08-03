package com.fscore.app.controller;

import com.fscore.app.dto.client.*;
import com.fscore.app.service.ClientApiService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientApiService service;

    public ClientController(ClientApiService service) {
        this.service = service;
    }

    @GetMapping("/matches")
    public ResponseEntity<PageInfo<MatchCard>> matches(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String seasonId,
            @RequestParam(required = false) String competitionId,
            @RequestParam(required = false) String teamId,
            @RequestParam(required = false) Boolean live,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.matches(date, seasonId, competitionId, teamId, live, page, size));
    }

    @GetMapping("/matches/{id}")
    public ResponseEntity<MatchDetail> matchDetail(@PathVariable String id) {
        return ResponseEntity.ok(service.matchDetail(id));
    }

    @PostMapping("/matches/{id}/events")
    public ResponseEntity<MatchEventItem> addMatchEvent(
            @PathVariable String id,
            @RequestBody MatchEventCreateRequest request) {
        return ResponseEntity.ok(service.addMatchEvent(id, request));
    }

    @GetMapping("/competitions")
    public ResponseEntity<List<CompetitionRef>> competitions() {
        return ResponseEntity.ok(service.competitions());
    }

    @GetMapping("/competitions/{id}")
    public ResponseEntity<CompetitionDetail> competitionDetail(@PathVariable String id) {
        return ResponseEntity.ok(service.competitionDetail(id));
    }

    @GetMapping("/seasons")
    public ResponseEntity<List<SeasonRef>> seasons(@RequestParam String competitionId) {
        return ResponseEntity.ok(service.seasons(competitionId));
    }

    @GetMapping("/standings")
    public ResponseEntity<List<StandingItem>> standings(
            @RequestParam String seasonId,
            @RequestParam(required = false) String stageId) {
        return ResponseEntity.ok(service.standings(seasonId, stageId));
    }

    @GetMapping("/teams")
    public ResponseEntity<List<TeamRef>> teams(@RequestParam(required = false) String seasonId) {
        return ResponseEntity.ok(service.competitionTeams(seasonId));
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamDetail> teamDetail(@PathVariable String id) {
        return ResponseEntity.ok(service.teamDetail(id));
    }

    @GetMapping("/teams/{id}/players")
    public ResponseEntity<List<SquadPlayerItem>> teamPlayers(
            @PathVariable String id,
            @RequestParam(required = false) String seasonId) {
        return ResponseEntity.ok(service.teamSquad(id, seasonId));
    }

    @GetMapping("/teams/{id}/stats")
    public ResponseEntity<List<PlayerSeasonStatItem>> teamStats(
            @PathVariable String id,
            @RequestParam(required = false) String seasonId) {
        return ResponseEntity.ok(service.teamPlayerStats(id, seasonId));
    }

    @GetMapping("/teams/{id}/suspensions")
    public ResponseEntity<List<TeamSuspensionItem>> teamSuspensions(@PathVariable String id) {
        return ResponseEntity.ok(service.teamSuspensions(id));
    }

    @GetMapping("/teams/{id}/injuries")
    public ResponseEntity<List<TeamInjuryItem>> teamInjuries(@PathVariable String id) {
        return ResponseEntity.ok(service.teamInjuries(id));
    }

    @GetMapping("/player-stats")
    public ResponseEntity<List<PlayerSeasonStatItem>> playerStats(
            @RequestParam String seasonId,
            @RequestParam(defaultValue = "scorers") String stat) {
        return ResponseEntity.ok(service.playerSeasonStats(seasonId, stat));
    }

    @GetMapping("/news")
    public ResponseEntity<List<NewsItem>> news(@RequestParam(required = false) String competitionId,
                                               @RequestParam(required = false) String teamId) {
        return ResponseEntity.ok(service.competitionNews(competitionId, teamId));
    }

    @GetMapping("/referees")
    public ResponseEntity<List<RefereeItem>> referees(@RequestParam String seasonId) {
        return ResponseEntity.ok(service.seasonReferees(seasonId));
    }

    @GetMapping("/coaches")
    public ResponseEntity<List<CoachItem>> coaches(@RequestParam String seasonId) {
        return ResponseEntity.ok(service.seasonCoaches(seasonId));
    }
}
