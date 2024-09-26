package com.spring_security.impls;

import com.spring_security.dtos.LoginDto;
import com.spring_security.dtos.RefreshToken;
import com.spring_security.dtos.SignUpDto;
import com.spring_security.entities.User;
import com.spring_security.repositories.UserRepository;
import com.spring_security.services.AuthService;
import com.spring_security.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthImplementation implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Transactional
    @Override
    public String signUp(SignUpDto dto) {
        var user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(User.ROLE.USER)
                .isActive(true)
                .build();
        userRepository.save(user);
        return "Successfully Created";
    }


    @Override
    public LoginDto login(LoginDto dto) throws NoSuchAlgorithmException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        var user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email or Password is incorrect.!"));
        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        return LoginDto.builder()
                .email(dto.getEmail())
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public LoginDto refreshTokenHandler(RefreshToken refreshToken) throws NoSuchAlgorithmException {
        String email = jwtService.extractUserName(refreshToken.getToken());
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found.!"));
        if (jwtService.isTokenValid(refreshToken.getToken(), user)) {
            var jwt = jwtService.generateToken(user);
            return LoginDto.builder()
                    .email(user.getEmail())
                    .token(jwt)
                    .refreshToken(refreshToken.getToken())
                    .build();
        }
        return null;
    }
}
