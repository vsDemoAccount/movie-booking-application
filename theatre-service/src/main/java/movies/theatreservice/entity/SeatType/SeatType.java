package movies.theatreservice.entity.SeatType;


import jakarta.persistence.*;
import lombok.*;
import movies.theatreservice.utils.CodeGeneratorUtil;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seat_types")
public class SeatType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16, unique = true)
    private String code;

    @Column(nullable = false, unique = true) // E.g., "VIP", "PREMIUM", "STANDARD"
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedAt;

    @PrePersist
    public void onPrePersist() {
        if (this.code == null) {
            // UPDATED: Using the utility for safer, cleaner generation
            // "Cit-" is 4 chars, leaving 12 chars for randomness (Total 16)
            this.code = CodeGeneratorUtil.generate("SeTy", 11);
        }
        Instant now = Instant.now();
        if (this.createdAt == null) this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = Instant.now();
    }
}