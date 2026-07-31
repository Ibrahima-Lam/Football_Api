package com.fscore.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "head_to_head")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HeadToHead {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team1_id", nullable = false)
    private Team team1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team2_id", nullable = false)
    private Team team2;

    @Column(name = "total_matches", nullable = false)
    private Integer totalMatches = 0;

    @Column(name = "team1_wins", nullable = false)
    private Integer team1Wins = 0;

    @Column(name = "team2_wins", nullable = false)
    private Integer team2Wins = 0;

    @Column(nullable = false)
    private Integer draws = 0;

    @Column(name = "team1_goals", nullable = false)
    private Integer team1Goals = 0;

    @Column(name = "team2_goals", nullable = false)
    private Integer team2Goals = 0;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}