package movies.userservice.dtos.UserProfileDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String avatarUrl;
    private String preferredCityCode;
    private String locale;
    private String timezone;
}