package com.spring_security.services;

import com.spring_security.dtos.LoginDto;
import com.spring_security.dtos.RefreshToken;
import com.spring_security.dtos.SignUpDto;

import java.security.NoSuchAlgorithmException;

public interface AuthService {

    String signUp(SignUpDto dto);

    LoginDto login(LoginDto dto) throws NoSuchAlgorithmException;

    LoginDto refreshTokenHandler(RefreshToken refreshToken) throws NoSuchAlgorithmException;
}
