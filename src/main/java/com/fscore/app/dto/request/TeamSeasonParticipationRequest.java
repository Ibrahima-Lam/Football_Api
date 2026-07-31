package com.fscore.app.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamSeasonParticipationRequest {
    private String seasonId;
    private String teamId;
    private String entryType;
    private String entryFromCompetitionId;
    private Integer finalRank;
    private String outcome;
    private Boolean withdrawn;
    private String withdrawalDate;
}
