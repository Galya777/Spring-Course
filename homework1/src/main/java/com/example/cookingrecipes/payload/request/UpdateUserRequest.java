package com.example.cookingrecipes.payload.request;

import com.example.cookingrecipes.model.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateUserRequest {
    @Size(max = 30)
    private String displayName;

    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9]).*$", 
             message = "Password must contain at least one number and one special character")
    private String password;

    private User.Gender gender;
    
    @Size(max = 512)
    private String bio;
}
