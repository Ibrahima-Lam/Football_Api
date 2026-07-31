package com.fscore.app.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchRefereeRequest {
    private String matchId;
    private String refereeId;
    private String role;
}
