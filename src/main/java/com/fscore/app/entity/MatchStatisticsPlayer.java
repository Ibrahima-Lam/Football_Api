package com.fscore.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_statistics_player")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchStatisticsPlayer {

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

    @Column(name = "minutes_played", nullable = false)
    private Integer minutesPlayed = 0;

    @Column(nullable = false)
    private Integer goals = 0;

    @Column(nullable = false)
    private Integer assists = 0;

    @Column(nullable = false)
    private Integer shots = 0;

    @Column(name = "shots_on_target", nullable = false)
    private Integer shotsOnTarget = 0;

    @Column(precision = 6, scale = 3)
    private BigDecimal xg;

    @Column(name = "key_passes", nullable = false)
    private Integer keyPasses = 0;

    @Column(nullable = false)
    private Integer passes = 0;

    @Column(name = "passes_accurate", nullable = false)
    private Integer passesAccurate = 0;

    @Column(name = "long_balls", nullable = false)
    private Integer longBalls = 0;

    @Column(nullable = false)
    private Integer crosses = 0;

    @Column(name = "dribbles_attempted", nullable = false)
    private Integer dribblesAttempted = 0;

    @Column(name = "dribbles_succeeded", nullable = false)
    private Integer dribblesSucceeded = 0;

    @Column(nullable = false)
    private Integer tackles = 0;

    @Column(nullable = false)
    private Integer interceptions = 0;

    @Column(nullable = false)
    private Integer clearances = 0;

    @Column(name = "fouls_committed", nullable = false)
    private Integer foulsCommitted = 0;

    @Column(name = "fouls_drawn", nullable = false)
    private Integer foulsDrawn = 0;

    @Column(name = "yellow_cards", nullable = false)
    private Integer yellowCards = 0;

    @Column(name = "red_cards", nullable = false)
    private Integer redCards = 0;

    @Column(nullable = false)
    private Integer saves = 0;

    @Column(name = "goals_conceded", nullable = false)
    private Integer goalsConceded = 0;

    @Column(precision = 4, scale = 2)
    private BigDecimal rating;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}