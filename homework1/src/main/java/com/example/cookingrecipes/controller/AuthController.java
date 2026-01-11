package com.example.cookingrecipes.controller;

import com.example.cookingrecipes.model.User;
import com.example.cookingrecipes.payload.request.LoginRequest;
import com.example.cookingrecipes.payload.request.SignupRequest;
import com.example.cookingrecipes.payload.response.JwtResponse;
import com.example.cookingrecipes.payload.response.MessageResponse;
import com.example.cookingrecipes.repository.UserRepository;
import com.example.cookingrecipes.security.JwtTokenProvider;
import com.example.cookingrecipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                         UserRepository userRepository,
                         UserService userService,
                         PasswordEncoder passwordEncoder,
                         JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getRole().name()
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByDisplayName(signUpRequest.getDisplayName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Display Name is already in use!"));
        }

        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setDisplayName(signUpRequest.getDisplayName());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setGender(signUpRequest.getGender());
        user.setBio(signUpRequest.getBio());
        user.setRole(User.UserRole.USER);
        user.setStatus(User.AccountStatus.ACTIVE);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
