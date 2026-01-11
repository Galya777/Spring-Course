package homework2.dto.response;

import homework2.model.enums.AccountStatus;
import homework2.model.enums.Gender;
import homework2.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String displayName;
    private String username;
    private String email;
    private Gender gender;
    private Role role;
    private String profilePictureUrl;
    private String bio;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
