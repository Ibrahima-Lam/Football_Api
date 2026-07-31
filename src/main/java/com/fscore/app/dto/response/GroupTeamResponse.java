package com.fscore.app.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupTeamResponse {
    private String id;
    private String groupId;
    private String teamId;
    private Integer seed;
    private Integer pot;
    private String qualifiedFrom;
    private String qualification;
}
