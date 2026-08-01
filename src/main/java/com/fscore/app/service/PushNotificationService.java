package com.fscore.app.service;

import com.fscore.app.dto.PushMessage;
import com.fscore.app.entity.Match;
import com.fscore.app.entity.MatchEvent;
import com.fscore.app.entity.News;
import com.fscore.app.entity.Player;
import com.fscore.app.entity.Team;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class PushNotificationService {

    private static final Set<String> NOTIFIABLE_EVENT_TYPES = Set.of(
        "GOAL", "RED_CARD", "YELLOW_RED", "PENALTY_MISS"
    );

    private static final Set<String> NOTIFIABLE_STATUSES = Set.of(
        "IN_PLAY", "PAUSED", "EXTRA_TIME", "PENALTY_SHOOTOUT", "FINISHED"
    );

    private final FcmService fcmService;

    public PushNotificationService(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    public void notifyMatchEvent(MatchEvent event) {
        if (event == null || !NOTIFIABLE_EVENT_TYPES.contains(event.getEventType())) {
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.put("type", "MATCH_EVENT");
        data.put("eventId", event.getId());
        data.put("matchId", idOf(event.getMatch()));
        data.put("teamId", idOf(event.getTeam()));
        data.put("playerId", idOf(event.getPlayer()));
        data.put("eventType", event.getEventType());
        data.put("minute", event.getMinute() != null ? event.getMinute().toString() : "");
        data.put("period", event.getPeriod());

        PushMessage message = PushMessage.builder()
            .title(eventTitle(event.getEventType()))
            .body(eventBody(event))
            .data(data)
            .build();
        fcmService.sendToAllTokens(message);
    }

    public void notifyNews(News news) {
        if (news == null || news.getId() == null) {
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.put("type", "NEWS");
        data.put("newsId", news.getId());
        data.put("slug", news.getSlug());

        PushMessage message = PushMessage.builder()
            .title(news.getTitle())
            .body(news.getExcerpt() != null ? news.getExcerpt() : news.getTitle())
            .data(data)
            .build();
        fcmService.sendToAllTokens(message);
    }

    public void notifyMatchUpdate(Match previous, Match current) {
        if (previous == null || current == null || current.getId() == null) {
            return;
        }
        if (!NOTIFIABLE_STATUSES.contains(current.getStatus())) {
            return;
        }
        boolean scoreChanged = !Objects.equals(previous.getHomeScore(), current.getHomeScore())
            || !Objects.equals(previous.getAwayScore(), current.getAwayScore());
        boolean statusChanged = !Objects.equals(previous.getStatus(), current.getStatus());
        if (!scoreChanged && !statusChanged) {
            return;
        }

        Map<String, String> data = new HashMap<>();
        data.put("type", "MATCH_UPDATE");
        data.put("matchId", current.getId());
        data.put("homeTeamId", idOf(current.getHomeTeam()));
        data.put("awayTeamId", idOf(current.getAwayTeam()));
        data.put("homeScore", String.valueOf(current.getHomeScore() != null ? current.getHomeScore() : 0));
        data.put("awayScore", String.valueOf(current.getAwayScore() != null ? current.getAwayScore() : 0));
        data.put("status", current.getStatus());
        data.put("period", current.getPeriod());
        data.put("minute", current.getMinute() != null ? current.getMinute().toString() : "");

        String title = scoreChanged ? scoreLine(current) : statusTitle(current);
        PushMessage message = PushMessage.builder()
            .title(title)
            .body(statusBody(current))
            .data(data)
            .build();
        fcmService.sendToAllTokens(message);
    }

    private String eventTitle(String eventType) {
        return switch (eventType) {
            case "GOAL" -> "But !";
            case "RED_CARD" -> "Carton rouge";
            case "YELLOW_RED" -> "Exclusion (2e carton jaune)";
            case "PENALTY_MISS" -> "Penalty manqué";
            default -> "Événement match";
        };
    }

    private String eventBody(MatchEvent event) {
        StringBuilder body = new StringBuilder();
        if (event.getMinute() != null) {
            body.append(event.getMinute());
            if (event.getExtraMinute() != null) {
                body.append("+").append(event.getExtraMinute());
            }
            body.append("' ");
        }
        body.append(event.getEventType());
        if (event.getDetail() != null) {
            body.append(" - ").append(event.getDetail());
        }
        return body.toString();
    }

    private String scoreLine(Match match) {
        return (match.getHomeScore() != null ? match.getHomeScore() : 0)
            + " - "
            + (match.getAwayScore() != null ? match.getAwayScore() : 0);
    }

    private String statusTitle(Match match) {
        return switch (match.getStatus()) {
            case "IN_PLAY", "PAUSED" -> "Match en cours";
            case "EXTRA_TIME" -> "Prolongations";
            case "PENALTY_SHOOTOUT" -> "Tirs au but";
            case "FINISHED" -> "Match terminé";
            default -> "Mise à jour match";
        };
    }

    private String statusBody(Match match) {
        StringBuilder body = new StringBuilder(scoreLine(match));
        if (match.getPeriod() != null) {
            body.append(" - ").append(match.getPeriod());
        }
        if (match.getMinute() != null) {
            body.append(" ").append(match.getMinute()).append("'");
        }
        return body.toString();
    }

    private String idOf(Match match) {
        return match != null ? match.getId() : null;
    }

    private String idOf(MatchEvent event) {
        return event != null ? event.getId() : null;
    }

    private String idOf(Team team) {
        return team != null ? team.getId() : null;
    }

    private String idOf(Player player) {
        return player != null ? player.getId() : null;
    }
}
