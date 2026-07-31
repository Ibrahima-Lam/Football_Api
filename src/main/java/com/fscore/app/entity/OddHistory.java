package com.fscore.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "odds_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OddHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmaker_id", nullable = false)
    private Bookmaker bookmaker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(nullable = false, length = 100)
    private String market;

    @Column(nullable = false, length = 100)
    private String selection;

    @Column(nullable = false, precision = 8, scale = 3)
    private BigDecimal odd;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;
}