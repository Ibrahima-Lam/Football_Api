package com.fscore.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "injuries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Injury {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @Column(name = "injury_type", nullable = false, length = 100)
    private String injuryType;

    @Column(name = "body_part", length = 100)
    private String bodyPart;

    @Column(nullable = false, length = 50)
    private String severity;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "expected_return")
    private LocalDate expectedReturn;

    @Column(name = "actual_return")
    private LocalDate actualReturn;

    @Column(nullable = false, length = 20)
    private String status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}