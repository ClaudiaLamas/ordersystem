package com.claudialamas.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder encoder;

    public UserService(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Procura utilizador por username.
     * Aqui devolvemos um "demo/demo" com ROLE_USER só para testes.
     */
    public Optional<UserDto> findByUsername(String username) {
        if ("demo".equalsIgnoreCase(username)) {
            // password "demo" codificada em BCRYPT (gerada a cada arranque)
            String hash = encoder.encode("demo");
            return Optional.of(new UserDto("demo", hash, "ROLE_USER"));
        }
        return Optional.empty();
    }

    /** DTO simples do utilizador (não é @Entity). */
    public static class UserDto {
        public final String username;
        public final String passwordHash;
        public final String role;

        public UserDto(String username, String passwordHash, String role) {
            this.username = username;
            this.passwordHash = passwordHash;
            this.role = role;
        }
    }
}
