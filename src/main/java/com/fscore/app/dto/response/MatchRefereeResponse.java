package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchRefereeResponse {
    private String id;
    private String matchId;
    private String refereeId;
    private String role;
}
