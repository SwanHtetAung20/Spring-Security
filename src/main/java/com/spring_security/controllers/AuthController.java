package com.spring_security.controllers;

import com.spring_security.dtos.LoginDto;
import com.spring_security.dtos.RefreshToken;
import com.spring_security.dtos.SignUpDto;
import com.spring_security.services.AuthService;
import com.spring_security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUpHandler(@RequestBody SignUpDto dto) {
        return new ResponseEntity<>(authService.signUp(dto), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginDto> loginHandler(@RequestBody LoginDto dto) throws NoSuchAlgorithmException {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(dto));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<LoginDto> refreshToken(@RequestBody RefreshToken refreshToken) throws NoSuchAlgorithmException {
        return ResponseEntity.status(HttpStatus.OK).body(authService.refreshTokenHandler(refreshToken));
    }
}
