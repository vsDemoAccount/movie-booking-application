package movies.moviesservice.entity.FailedEvent;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "failed_events")
public class FailedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload; // Stores the JSON of the event

    @Column(nullable = false)
    private String status; // "PENDING", "SENT", "FAILED_PERMANENTLY"

    @Column(length = 2000)
    private String errorMessage;

    @Builder.Default
    private int retryCount = 0;

    @Column(nullable = false)
    private Instant createdAt;
}