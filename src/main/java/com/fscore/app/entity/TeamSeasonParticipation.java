package com.fscore.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "team_season_participations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TeamSeasonParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "entry_type", nullable = false, length = 50)
    private String entryType;

    @Column(name = "entry_from_competition_id")
    private String entryFromCompetitionId;

    @Column(name = "final_rank")
    private Integer finalRank;

    @Column(nullable = false, length = 50)
    private String outcome;

    @Column(name = "is_withdrawn", nullable = false)
    private Boolean withdrawn = false;

    @Column(name = "withdrawal_date")
    private LocalDate withdrawalDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}