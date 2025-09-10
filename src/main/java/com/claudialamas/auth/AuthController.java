package com.claudialamas.auth;

import com.claudialamas.auth.dto.LoginRequestDto;
import com.claudialamas.auth.dto.TokenResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService users;
    private final PasswordEncoder encoder;
    private final JwtIssuer jwt;

    public AuthController(UserService users, PasswordEncoder encoder, JwtIssuer jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto body) {
        return users.findByUsername(body.getUsername())
                .filter(u -> encoder.matches(body.getPassword(), u.passwordHash))
                .map(u -> {
                    String token = jwt.issue(u.username, u.role);
                    return ResponseEntity.ok(new TokenResponseDto(token, "Bearer"));
                })
                .orElseGet(() -> ResponseEntity.status(401).build());
    }
}