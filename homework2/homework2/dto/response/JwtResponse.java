package homework2.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String displayName;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String displayName, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.roles = roles;
    }
}
