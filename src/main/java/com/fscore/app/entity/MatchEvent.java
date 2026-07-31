package com.fscore.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_events")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(nullable = false)
    private Integer minute;

    @Column(name = "extra_minute")
    private Integer extraMinute;

    @Column(nullable = false, length = 20)
    private String period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_player_id")
    private Player relatedPlayer;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(length = 100)
    private String detail;

    @Column(length = 2000)
    private String comments;

    @Column(name = "var_reviewed", nullable = false)
    private Boolean varReviewed = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}