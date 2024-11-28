package cn.msa.msa_museum_server.service;


import cn.msa.msa_museum_server.entity.UserEntity;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private String SECRET_KEY = "this_is_a_very_secure_example_key_12345";
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
