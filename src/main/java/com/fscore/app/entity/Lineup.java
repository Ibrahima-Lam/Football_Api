package com.fscore.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "lineups")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Lineup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private Boolean starter;

    @Column(nullable = false)
    private Boolean captain = false;

    @Column(name = "shirt_number")
    private Integer shirtNumber;

    @Column(length = 30)
    private String position;

    @Column(name = "position_x", precision = 5, scale = 2)
    private java.math.BigDecimal positionX;

    @Column(name = "position_y", precision = 5, scale = 2)
    private java.math.BigDecimal positionY;

    @Column(name = "formation_slot")
    private Integer formationSlot;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}