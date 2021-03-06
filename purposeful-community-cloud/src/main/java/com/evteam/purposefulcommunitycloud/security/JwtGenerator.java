package com.evteam.purposefulcommunitycloud.security;

import com.evteam.purposefulcommunitycloud.model.entity.User;
import com.evteam.purposefulcommunitycloud.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtGenerator {

    private static final long JWT_TOKEN_VALIDITY = 10 * 60 * 60; // 10 hours

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private UserRepository userRepository;

    // Generates a token with the given user's id and current time
    public String generateToken(UUID id) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(id.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String generateTokenByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return generateToken(user.getId());
    }

}
