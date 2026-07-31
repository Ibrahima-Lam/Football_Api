package com.fscore.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "rate_limits")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RateLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_key_id", nullable = false)
    private ApiKey apiKey;

    @Column(name = "requests_per_minute", nullable = false)
    private Integer requestsPerMinute;

    @Column(name = "requests_per_day", nullable = false)
    private Integer requestsPerDay;

    @Column(name = "requests_per_month", nullable = false)
    private Integer requestsPerMonth;

    @Column(name = "current_minute_count", nullable = false)
    private Integer currentMinuteCount = 0;

    @Column(name = "current_day_count", nullable = false)
    private Integer currentDayCount = 0;

    @Column(name = "current_month_count", nullable = false)
    private Integer currentMonthCount = 0;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}