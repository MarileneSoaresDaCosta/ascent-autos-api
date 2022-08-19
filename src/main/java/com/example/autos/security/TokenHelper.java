package com.example.autos.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TokenHelper {

    private final JwtProperties jwtProperties;

    public TokenHelper(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String getToken(String username, List<GrantedAuthority> authorities){
        String token = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(username)
                .claim("first_name", "first name")
                .claim("last_name", "last name")
                .claim("email", "email@gmail.com")
                .claim("guid", (long)999)
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .claim("authorities", authorities.stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration() * 1000L))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret().getBytes())
                .compact();
        return jwtProperties.getPrefix()+" "+token;
    }

}
