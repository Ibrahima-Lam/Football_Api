package com.fscore.app.service.impl;

import com.fscore.app.dto.response.LiveScoreResponse;
import com.fscore.app.dto.response.MatchEventResponse;
import com.fscore.app.dto.response.MatchStatisticsPlayerResponse;
import com.fscore.app.dto.response.MatchStatisticsTeamResponse;
import com.fscore.app.dto.response.NewsResponse;
import com.fscore.app.dto.response.StandingResponse;
import com.fscore.app.entity.Match;
import com.fscore.app.entity.MatchEvent;
import com.fscore.app.entity.MatchStatisticsPlayer;
import com.fscore.app.entity.MatchStatisticsTeam;
import com.fscore.app.entity.News;
import com.fscore.app.entity.Standing;
import com.fscore.app.service.LiveScoreService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LiveScoreServiceImpl implements LiveScoreService {

    public static final String TOPIC_LIVE = "/topic/live";
    public static final String TOPIC_EVENTS = "/topic/events";
    public static final String TOPIC_NEWS = "/topic/news";
    public static final String TOPIC_STATS = "/topic/stats";
    public static final String TOPIC_STANDINGS = "/topic/standings";

    private final SimpMessagingTemplate messagingTemplate;

    public LiveScoreServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void broadcastMatchUpdate(Match match) {
        LiveScoreResponse response = LiveScoreResponse.builder()
            .matchId(match.getId())
            .homeTeamId(match.getHomeTeam() != null ? match.getHomeTeam().getId() : null)
            .awayTeamId(match.getAwayTeam() != null ? match.getAwayTeam().getId() : null)
            .status(match.getStatus())
            .period(match.getPeriod())
            .minute(match.getMinute())
            .minuteExtra(match.getMinuteExtra())
            .homeScore(match.getHomeScore())
            .awayScore(match.getAwayScore())
            .homeHtScore(match.getHomeHtScore())
            .awayHtScore(match.getAwayHtScore())
            .homeEtScore(match.getHomeEtScore())
            .awayEtScore(match.getAwayEtScore())
            .homePenaltyScore(match.getHomePenaltyScore())
            .awayPenaltyScore(match.getAwayPenaltyScore())
            .updatedAt(LocalDateTime.now())
            .build();
        messagingTemplate.convertAndSend(TOPIC_LIVE, response);
    }

    @Override
    public void broadcastMatchEvent(MatchEvent event) {
        MatchEventResponse response = MatchEventResponse.builder()
            .id(event.getId())
            .matchId(event.getMatch() != null ? event.getMatch().getId() : null)
            .minute(event.getMinute())
            .extraMinute(event.getExtraMinute())
            .period(event.getPeriod())
            .teamId(event.getTeam() != null ? event.getTeam().getId() : null)
            .playerId(event.getPlayer() != null ? event.getPlayer().getId() : null)
            .relatedPlayerId(event.getRelatedPlayer() != null ? event.getRelatedPlayer().getId() : null)
            .eventType(event.getEventType())
            .detail(event.getDetail())
            .comments(event.getComments())
            .varReviewed(event.getVarReviewed())
            .build();
        messagingTemplate.convertAndSend(TOPIC_EVENTS, response);
    }

    @Override
    public void broadcastNews(News news) {
        NewsResponse response = NewsResponse.builder()
            .id(news.getId())
            .competitionId(news.getCompetition() != null ? news.getCompetition().getId() : null)
            .teamId(news.getTeam() != null ? news.getTeam().getId() : null)
            .playerId(news.getPlayer() != null ? news.getPlayer().getId() : null)
            .title(news.getTitle())
            .slug(news.getSlug())
            .content(news.getContent())
            .excerpt(news.getExcerpt())
            .image(news.getImage())
            .author(news.getAuthor())
            .language(news.getLanguage())
            .sourceUrl(news.getSourceUrl())
            .publishedAt(news.getPublishedAt())
            .build();
        messagingTemplate.convertAndSend(TOPIC_NEWS, response);
    }

    @Override
    public void broadcastTeamStats(MatchStatisticsTeam stats) {
        MatchStatisticsTeamResponse response = MatchStatisticsTeamResponse.builder()
            .id(stats.getId())
            .matchId(stats.getMatch() != null ? stats.getMatch().getId() : null)
            .teamId(stats.getTeam() != null ? stats.getTeam().getId() : null)
            .possession(stats.getPossession())
            .shots(stats.getShots())
            .shotsOnTarget(stats.getShotsOnTarget())
            .shotsOffTarget(stats.getShotsOffTarget())
            .shotsBlocked(stats.getShotsBlocked())
            .corners(stats.getCorners())
            .freeKicks(stats.getFreeKicks())
            .goalKicks(stats.getGoalKicks())
            .throwIns(stats.getThrowIns())
            .offsides(stats.getOffsides())
            .fouls(stats.getFouls())
            .yellowCards(stats.getYellowCards())
            .yellowRedCards(stats.getYellowRedCards())
            .redCards(stats.getRedCards())
            .passes(stats.getPasses())
            .passesAccurate(stats.getPassesAccurate())
            .tackles(stats.getTackles())
            .interceptions(stats.getInterceptions())
            .clearances(stats.getClearances())
            .saves(stats.getSaves())
            .xg(stats.getXg())
            .xga(stats.getXga())
            .build();
        messagingTemplate.convertAndSend(TOPIC_STATS, response);
    }

    @Override
    public void broadcastPlayerStats(MatchStatisticsPlayer stats) {
        MatchStatisticsPlayerResponse response = MatchStatisticsPlayerResponse.builder()
            .id(stats.getId())
            .matchId(stats.getMatch() != null ? stats.getMatch().getId() : null)
            .teamId(stats.getTeam() != null ? stats.getTeam().getId() : null)
            .playerId(stats.getPlayer() != null ? stats.getPlayer().getId() : null)
            .minutesPlayed(stats.getMinutesPlayed())
            .goals(stats.getGoals())
            .assists(stats.getAssists())
            .shots(stats.getShots())
            .shotsOnTarget(stats.getShotsOnTarget())
            .xg(stats.getXg())
            .keyPasses(stats.getKeyPasses())
            .passes(stats.getPasses())
            .passesAccurate(stats.getPassesAccurate())
            .longBalls(stats.getLongBalls())
            .crosses(stats.getCrosses())
            .dribblesAttempted(stats.getDribblesAttempted())
            .dribblesSucceeded(stats.getDribblesSucceeded())
            .tackles(stats.getTackles())
            .interceptions(stats.getInterceptions())
            .clearances(stats.getClearances())
            .foulsCommitted(stats.getFoulsCommitted())
            .foulsDrawn(stats.getFoulsDrawn())
            .yellowCards(stats.getYellowCards())
            .redCards(stats.getRedCards())
            .saves(stats.getSaves())
            .goalsConceded(stats.getGoalsConceded())
            .rating(stats.getRating())
            .build();
        messagingTemplate.convertAndSend(TOPIC_STATS, response);
    }

    @Override
    public void broadcastStanding(Standing standing) {
        StandingResponse response = StandingResponse.builder()
            .id(standing.getId())
            .seasonId(standing.getSeason() != null ? standing.getSeason().getId() : null)
            .stageId(standing.getStage() != null ? standing.getStage().getId() : null)
            .groupId(standing.getGroup() != null ? standing.getGroup().getId() : null)
            .teamId(standing.getTeam() != null ? standing.getTeam().getId() : null)
            .rankPosition(standing.getRankPosition())
            .played(standing.getPlayed())
            .wins(standing.getWins())
            .draws(standing.getDraws())
            .losses(standing.getLosses())
            .homeWins(standing.getHomeWins())
            .homeDraws(standing.getHomeDraws())
            .homeLosses(standing.getHomeLosses())
            .awayWins(standing.getAwayWins())
            .awayDraws(standing.getAwayDraws())
            .awayLosses(standing.getAwayLosses())
            .goalsFor(standing.getGoalsFor())
            .goalsAgainst(standing.getGoalsAgainst())
            .goalDifference(standing.getGoalDifference())
            .points(standing.getPoints())
            .form(standing.getForm())
            .build();
        messagingTemplate.convertAndSend(TOPIC_STANDINGS, response);
    }
}
