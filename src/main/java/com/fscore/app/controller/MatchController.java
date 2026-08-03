package com.fscore.app.controller;

import com.fscore.app.dto.request.MatchLiveUpdateRequest;
import com.fscore.app.dto.request.MatchRequest;
import com.fscore.app.dto.response.MatchResponse;
import com.fscore.app.dto.response.PageResponse;
import com.fscore.app.entity.Match;
import com.fscore.app.entity.*;
import com.fscore.app.service.MatchService;
import com.fscore.app.service.FilterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.net.URI;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService service;
    private final FilterService filterService;

    public MatchController(MatchService service, FilterService filterService) {
        this.service = service;
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<PageResponse> findAll(Pageable pageable, @RequestParam Map<String, String> params) {
        Page<Match> page = filterService.find("matchRepository", pageable, params);
        return ResponseEntity.ok(PageResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponse> findById(@PathVariable String id) {
        Match entity = service.findById(id)
            .orElseThrow(() -> new com.fscore.app.exception.ResourceNotFoundException("Match not found with id: " + id));
        return ResponseEntity.ok(mapToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<MatchResponse> save(@Valid @RequestBody MatchRequest request) {
        Match entity = mapToEntity(request);
        Match saved = service.save(entity);
        return ResponseEntity.created(URI.create("/api/matches/" + saved.getId()))
            .body(mapToResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchResponse> update(@PathVariable String id, @Valid @RequestBody MatchRequest request) {
        Match entity = mapToEntity(request);
        Match updated = service.update(id, entity);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MatchResponse> partialUpdate(@PathVariable String id,
                                                       @RequestBody MatchLiveUpdateRequest request) {
        Match updated = service.applyLiveUpdate(id, request);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private MatchResponse mapToResponse(Match entity) {
        MatchResponse response = MatchResponse.builder()
            .attendance(entity.getAttendance())
            .awayEtScore(entity.getAwayEtScore())
            .awayHtScore(entity.getAwayHtScore())
            .awayPenaltyForm(entity.getAwayPenaltyForm())
            .awayPenaltyScore(entity.getAwayPenaltyScore())
            .awayScore(entity.getAwayScore())
            .awayTeamId(entity.getAwayTeam() != null ? entity.getAwayTeam().getId() : null)
            .groupId(entity.getGroup() != null ? entity.getGroup().getId() : null)
            .homeEtScore(entity.getHomeEtScore())
            .homeHtScore(entity.getHomeHtScore())
            .homePenaltyForm(entity.getHomePenaltyForm())
            .homePenaltyScore(entity.getHomePenaltyScore())
            .homeScore(entity.getHomeScore())
            .homeTeamId(entity.getHomeTeam() != null ? entity.getHomeTeam().getId() : null)
            .id(entity.getId())
            .kickoff(entity.getKickoff())
            .minute(entity.getMinute())
            .minuteExtra(entity.getMinuteExtra())
            .note(entity.getNote())
            .period(entity.getPeriod())
            .firstHalfStart(entity.getFirstHalfStart())
            .secondHalfStart(entity.getSecondHalfStart())
            .extraTimeStart(entity.getExtraTimeStart())
            .penaltyShootoutStart(entity.getPenaltyShootoutStart())
            .temperature(entity.getTemperature())
            .windSpeed(entity.getWindSpeed())
            .refereeId(entity.getReferee() != null ? entity.getReferee().getId() : null)
            .roundId(entity.getRound() != null ? entity.getRound().getId() : null)
            .seasonId(entity.getSeason() != null ? entity.getSeason().getId() : null)
            .stadiumId(entity.getStadium() != null ? entity.getStadium().getId() : null)
            .stageId(entity.getStage() != null ? entity.getStage().getId() : null)
            .status(entity.getStatus())
            .weather(entity.getWeather())
            .build();
        return response;
    }

    private Match mapToEntity(MatchRequest request) {
        Match match = Match.builder()
            .awayTeam(request.getAwayTeamId() != null ? Team.builder().id(request.getAwayTeamId()).build() : null)
            .group(request.getGroupId() != null ? Group.builder().id(request.getGroupId()).build() : null)
            .homeTeam(request.getHomeTeamId() != null ? Team.builder().id(request.getHomeTeamId()).build() : null)
            .referee(request.getRefereeId() != null ? Referee.builder().id(request.getRefereeId()).build() : null)
            .round(request.getRoundId() != null ? Round.builder().id(request.getRoundId()).build() : null)
            .season(request.getSeasonId() != null ? Season.builder().id(request.getSeasonId()).build() : null)
            .stadium(request.getStadiumId() != null ? Stadium.builder().id(request.getStadiumId()).build() : null)
            .stage(request.getStageId() != null ? Stage.builder().id(request.getStageId()).build() : null)
            .attendance(request.getAttendance())
            .awayEtScore(request.getAwayEtScore())
            .awayHtScore(request.getAwayHtScore())
            .awayPenaltyForm(request.getAwayPenaltyForm())
            .awayPenaltyScore(request.getAwayPenaltyScore())
            .awayScore(request.getAwayScore())
            .homeEtScore(request.getHomeEtScore())
            .homeHtScore(request.getHomeHtScore())
            .homePenaltyForm(request.getHomePenaltyForm())
            .homePenaltyScore(request.getHomePenaltyScore())
            .homeScore(request.getHomeScore())
            .minute(request.getMinute())
            .minuteExtra(request.getMinuteExtra())
            .note(request.getNote())
            .period(request.getPeriod())
            .status(request.getStatus())
            .weather(request.getWeather())
            .kickoff(request.getKickoff())
            .firstHalfStart(request.getFirstHalfStart())
            .secondHalfStart(request.getSecondHalfStart())
            .extraTimeStart(request.getExtraTimeStart())
            .penaltyShootoutStart(request.getPenaltyShootoutStart())
            .temperature(request.getTemperature())
            .windSpeed(request.getWindSpeed())
            .build();
        return match;
    }
}
