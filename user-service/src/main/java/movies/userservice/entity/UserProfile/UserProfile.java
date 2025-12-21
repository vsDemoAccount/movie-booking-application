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

    // Stores the S3 URL
    private String avatarUrl;

    @Column(length = 16)
    private String preferredCityCode;

    @Column(length = 10)
    private String locale;

    @Column(length = 50)
    private String timezone;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}