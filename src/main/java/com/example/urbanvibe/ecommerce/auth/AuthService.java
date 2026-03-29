package com.example.urbanvibe.ecommerce.auth;

import com.example.urbanvibe.ecommerce.common.dto.AuthRequest;
import com.example.urbanvibe.ecommerce.common.dto.AuthResponse;
import com.example.urbanvibe.ecommerce.common.dto.RegisterRequest;
import com.example.urbanvibe.ecommerce.common.dto.UserResponse;
import com.example.urbanvibe.ecommerce.user.Role;
import com.example.urbanvibe.ecommerce.user.User;
import com.example.urbanvibe.ecommerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        // Encriptar contraseña antes de guardar
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        // verificar email y password automáticamente
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public UserResponse me(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(user.getId(), user.getEmail(), user.getRole());
    }
}
