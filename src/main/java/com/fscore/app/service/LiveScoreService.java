package com.fscore.app.service;

import com.fscore.app.entity.Match;
import com.fscore.app.entity.MatchEvent;
import com.fscore.app.entity.MatchStatisticsPlayer;
import com.fscore.app.entity.MatchStatisticsTeam;
import com.fscore.app.entity.News;
import com.fscore.app.entity.Standing;

public interface LiveScoreService {
    void broadcastMatchUpdate(Match match);
    void broadcastMatchEvent(MatchEvent event);
    void broadcastNews(News news);
    void broadcastTeamStats(MatchStatisticsTeam stats);
    void broadcastPlayerStats(MatchStatisticsPlayer stats);
    void broadcastStanding(Standing standing);
}
