package com.trackmyteam.backend.service;

import com.trackmyteam.backend.model.Role;
import com.trackmyteam.backend.model.User;
import com.trackmyteam.backend.repository.UserRepository;
import com.trackmyteam.backend.util.JwtUtil;
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
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public String register(User request) {
        var user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return "User Registered Successfully! \nYour JWT token is : "+jwtUtil.generateToken((org.springframework.security.core.userdetails.UserDetails) user);
    }

    public String login(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        return jwtUtil.generateToken((org.springframework.security.core.userdetails.UserDetails) user);
    }
}
