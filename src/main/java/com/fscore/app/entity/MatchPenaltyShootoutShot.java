package com.fscore.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_penalty_shootout_shots")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchPenaltyShootoutShot {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goalkeeper_id", nullable = false)
    private Player goalkeeper;

    @Column(name = "shot_order", nullable = false)
    private Integer shotOrder;

    @Column(nullable = false)
    private Integer round;

    @Column(nullable = false, length = 30)
    private String status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}