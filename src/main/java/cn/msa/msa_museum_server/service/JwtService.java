package cn.msa.msa_museum_server.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import cn.msa.msa_museum_server.entity.UserEntity;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private String SECRET_KEY = "https://www.youtube.com/watch?v=vw5AOn6FDzw&list=PLxRSX8UqzWccPUnMBBYdxe2v55eOgQxuj&index=12";

    private SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String setToken(UserEntity userEntity) {
        return Jwts
                .builder()
                .subject(userEntity.getId().toString())
                .signWith(key)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .compact();
    }

    public String decodeJwt(String token) throws JwtException {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
