package com.fscore.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "matches")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Match {

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
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referee_id")
    private Referee referee;

    @Column(name = "kickoff", nullable = false)
    private LocalDateTime kickoff;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(length = 20)
    private String period;

    private Integer minute;

    @Column(name = "minute_extra")
    private Integer minuteExtra;

    @Column(name = "first_half_start")
    private LocalDateTime firstHalfStart;

    @Column(name = "second_half_start")
    private LocalDateTime secondHalfStart;

    @Column(name = "extra_time_start")
    private LocalDateTime extraTimeStart;

    @Column(name = "penalty_shootout_start")
    private LocalDateTime penaltyShootoutStart;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "away_score")
    private Integer awayScore;

    @Column(name = "home_ht_score")
    private Integer homeHtScore;

    @Column(name = "away_ht_score")
    private Integer awayHtScore;

    @Column(name = "home_et_score")
    private Integer homeEtScore;

    @Column(name = "away_et_score")
    private Integer awayEtScore;

    @Column(name = "home_penalty_score")
    private Integer homePenaltyScore;

    @Column(name = "away_penalty_score")
    private Integer awayPenaltyScore;

    @Column(name = "home_penalty_form", length = 30)
    private String homePenaltyForm;

    @Column(name = "away_penalty_form", length = 30)
    private String awayPenaltyForm;

    private Integer attendance;

    @Column(length = 100)
    private String weather;

    @Column(precision = 4, scale = 1)
    private BigDecimal temperature;

    @Column(name = "wind_speed", precision = 5, scale = 1)
    private BigDecimal windSpeed;

    @Column(length = 1000)
    private String note;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}