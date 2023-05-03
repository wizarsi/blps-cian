package com.example.blpscian.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.access-secret}")
    private String accessTokenSecret;

    @Value("${jwt.refresh-secret}")
    private String refreshTokenSecret;
    @Value("${token.access-expire-time}")
    private Long accessTokenExpirationMs;

    @Value("${token.refresh-expire-time}")
    private Long refreshTokenExpirationMs;

    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expired = new Date(now.getTime() + accessTokenExpirationMs);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS256, accessTokenSecret)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expired = new Date(now.getTime() + refreshTokenExpirationMs);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS256, refreshTokenSecret)
                .compact();
    }

    public boolean checkAccessToken(String token) {
        return checkJwtToken(token,accessTokenSecret);
    }

    public boolean checkRefreshToken(String token) {
        return checkJwtToken(token,refreshTokenSecret);
    }

    private boolean checkJwtToken(String token, String secretKey){
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            long expirationTime = claims.getExpiration().getTime();
            long currentTime = System.currentTimeMillis();
            return currentTime <= expirationTime;
        } catch (Exception e) {
            return false;
        }
    }

    public String usernameFromAccessToken(String token){
        return Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String usernameFromRefreshToken(String token) {
        return Jwts.parser().setSigningKey(refreshTokenSecret).parseClaimsJws(token).getBody().getSubject();
    }
}
