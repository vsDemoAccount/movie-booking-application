package movies.userservice.entity.UserProfile;

import jakarta.persistence.*;
import lombok.*;
import movies.userservice.entity.User.User;
import java.time.Instant;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String avatarUrl;

    // Critical for Booking App: "Show movies in Mumbai by default"
    private String preferredCityCode;

    private String locale;
    private String timezone;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}