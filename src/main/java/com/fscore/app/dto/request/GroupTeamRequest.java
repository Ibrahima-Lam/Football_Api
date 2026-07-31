package com.fscore.app.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupTeamRequest {
    private String groupId;
    private String teamId;
    private Integer seed;
    private Integer pot;
    private String qualifiedFrom;
    private String qualification;
}
