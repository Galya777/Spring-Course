package homework2.dto.request;

import homework2.model.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank(message = "Display name is required")
    @Size(max = 50, message = "Display name must be at most 50 characters")
    private String displayName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers and underscores")
    private String username;

    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9]).*$")
    private String password;

    @NotNull
    private Gender gender;

    @Size(max = 512)
    private String bio;

    private String profilePictureUrl;
}
