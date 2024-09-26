package com.spring_security.impls;

import com.spring_security.entities.User;
import com.spring_security.services.JWTService;
import com.spring_security.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTImplementation implements JWTService {


    private static final String SECRET_KEY= "Bf56xdqWO5khb1OOYPSfZCr1hHVWT2DFnVjrRg5ZB7Y=";


    @Override
    public String generateToken(UserDetails userDetails) throws NoSuchAlgorithmException {
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUserName(String token) throws NoSuchAlgorithmException {
        return extractClaim(token,Claims::getSubject);
    }


    private <T> T extractClaim(String token, Function<Claims, T> claimsTResolver) throws NoSuchAlgorithmException {
        final Claims claims = extractAllClaims(token);
    return  claimsTResolver.apply(claims);
    }



    private Claims extractAllClaims(String token) throws NoSuchAlgorithmException {
      return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }


    @Override
    public boolean isTokenValid(String token,UserDetails userDetails) throws NoSuchAlgorithmException {
      final String userEmail = extractUserName(token);
      return (userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public String generateRefreshToken(HashMap<String, Object> extractClaims, UserDetails userDetails) throws NoSuchAlgorithmException {
        return Jwts.builder().setClaims(extractClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604000000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) throws NoSuchAlgorithmException {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }


    private Key getSignKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


//    private String keyGenerator() throws NoSuchAlgorithmException {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//        SecretKey secretKey = keyGenerator.generateKey();
//        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
//    }
}
