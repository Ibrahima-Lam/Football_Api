package com.fscore.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "standings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Standing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false)
    private Stage stage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "rank_position", nullable = false)
    private Integer rankPosition;

    @Column(nullable = false)
    private Integer played = 0;

    @Column(nullable = false)
    private Integer wins = 0;

    @Column(nullable = false)
    private Integer draws = 0;

    @Column(nullable = false)
    private Integer losses = 0;

    @Column(name = "home_wins", nullable = false)
    private Integer homeWins = 0;

    @Column(name = "home_draws", nullable = false)
    private Integer homeDraws = 0;

    @Column(name = "home_losses", nullable = false)
    private Integer homeLosses = 0;

    @Column(name = "away_wins", nullable = false)
    private Integer awayWins = 0;

    @Column(name = "away_draws", nullable = false)
    private Integer awayDraws = 0;

    @Column(name = "away_losses", nullable = false)
    private Integer awayLosses = 0;

    @Column(name = "goals_for", nullable = false)
    private Integer goalsFor = 0;

    @Column(name = "goals_against", nullable = false)
    private Integer goalsAgainst = 0;

    @Column(name = "goal_difference", nullable = false)
    private Integer goalDifference = 0;

    @Column(nullable = false)
    private Integer points = 0;

    @Column(length = 10)
    private String form;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}