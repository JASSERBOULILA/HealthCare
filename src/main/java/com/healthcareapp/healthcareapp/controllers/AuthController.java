package com.healthcareapp.healthcareapp.controllers;

import com.healthcareapp.healthcareapp.DTO.SignInRequest;
import com.healthcareapp.healthcareapp.DTO.SignUpRequest;
import com.healthcareapp.healthcareapp.DTO.UserResponse;
import com.healthcareapp.healthcareapp.Repository.UserRepository;
import com.healthcareapp.healthcareapp.models.User;
import com.healthcareapp.healthcareapp.security.UserPrincipal;
import com.healthcareapp.healthcareapp.services.AuthService;
import com.healthcareapp.healthcareapp.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;



    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody SignUpRequest req) {
        User user = authService.signUp(req);
        String token = jwtService.generateToken(UserPrincipal.from(user));

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, jwtService.createCookie(token).toString())
                .body(UserResponse.from(user));
    }

    @PostMapping("/signin")
    public ResponseEntity<UserResponse> signIn(@Valid @RequestBody SignInRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password()));

        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        String token = jwtService.generateToken(principal);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtService.createCookie(token).toString())
                .body(new UserResponse(principal.getId(), null, null, principal.getEmail(), principal.getRole()));
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> signOut() {
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, jwtService.clearCookie().toString())
                .build();
    }

    /** The frontend calls this on page load to know who is logged in. No id, no token. */
    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal UserPrincipal principal) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );

        return ResponseEntity.ok(response).getBody();
    }
}
