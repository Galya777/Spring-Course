package com.example.cookingrecipes.payload.request;

import com.example.cookingrecipes.model.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 15)
    @Pattern(regexp = "^\\w+$", message = "Username can only contain letters, numbers, and underscores")
    private String username;

    @NotBlank
    @Size(max = 30)
    private String displayName;

    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9]).*$", 
             message = "Password must contain at least one number and one special character")
    private String password;

    private User.Gender gender;
    
    @Size(max = 512)
    private String bio;
}
