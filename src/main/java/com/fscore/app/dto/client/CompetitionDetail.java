package com.fscore.app.dto.client;

import java.util.List;

public record CompetitionDetail(
        CompetitionRef competition,
        List<SeasonRef> seasons) {
}
