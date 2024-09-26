package com.spring_security.services;

import com.spring_security.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public interface JWTService {


    String generateToken(UserDetails userDetails) throws NoSuchAlgorithmException;

    String extractUserName(String token) throws NoSuchAlgorithmException;

    boolean isTokenValid(String token,UserDetails userDetails) throws NoSuchAlgorithmException;


    String generateRefreshToken(HashMap<String, Object> extractClaims, UserDetails userDetails) throws NoSuchAlgorithmException;
}
