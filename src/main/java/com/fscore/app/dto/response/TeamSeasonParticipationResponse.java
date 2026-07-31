package com.fscore.app.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamSeasonParticipationResponse {
    private String id;
    private String seasonId;
    private String teamId;
    private String entryType;
    private String entryFromCompetitionId;
    private Integer finalRank;
    private String outcome;
    private Boolean withdrawn;
    private String withdrawalDate;
}
