package com.fscore.app.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TeamResponse {
    private String id;
    private String countryId;
    private String stadiumId;
    private String type;
    private Boolean nationalTeam;
    private String name;
    private String shortName;
    private String code;
    private Integer founded;
    private String logo;
    private String kitPrimaryColor;
    private String kitSecondaryColor;
    private String website;
    private String address;
    private String phone;
    private String email;
    private String description;
    private Boolean active;
}
