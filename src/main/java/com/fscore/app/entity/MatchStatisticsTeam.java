package com.fscore.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_statistics_team")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchStatisticsTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(precision = 5, scale = 2)
    private BigDecimal possession;

    @Column(nullable = false)
    private Integer shots = 0;

    @Column(name = "shots_on_target", nullable = false)
    private Integer shotsOnTarget = 0;

    @Column(name = "shots_off_target", nullable = false)
    private Integer shotsOffTarget = 0;

    @Column(name = "shots_blocked", nullable = false)
    private Integer shotsBlocked = 0;

    @Column(nullable = false)
    private Integer corners = 0;

    @Column(nullable = false)
    private Integer freeKicks = 0;

    @Column(name = "goal_kicks", nullable = false)
    private Integer goalKicks = 0;

    @Column(name = "throw_ins", nullable = false)
    private Integer throwIns = 0;

    @Column(nullable = false)
    private Integer offsides = 0;

    @Column(nullable = false)
    private Integer fouls = 0;

    @Column(name = "yellow_cards", nullable = false)
    private Integer yellowCards = 0;

    @Column(name = "yellow_red_cards", nullable = false)
    private Integer yellowRedCards = 0;

    @Column(name = "red_cards", nullable = false)
    private Integer redCards = 0;

    @Column(nullable = false)
    private Integer passes = 0;

    @Column(name = "passes_accurate", nullable = false)
    private Integer passesAccurate = 0;

    @Column(nullable = false)
    private Integer tackles = 0;

    @Column(nullable = false)
    private Integer interceptions = 0;

    @Column(nullable = false)
    private Integer clearances = 0;

    @Column(nullable = false)
    private Integer saves = 0;

    @Column(precision = 6, scale = 3)
    private BigDecimal xg;

    @Column(precision = 6, scale = 3)
    private BigDecimal xga;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}