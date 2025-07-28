package com.example.ordersystem.common.Auth;

import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.member.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.expirationAt}")
    private int expirationAt;

    @Value("${jwt.secretKeyAt}")
    private String secretKeyAt;

    private Key secretKey;

    @PostConstruct
    public void init() {
        secretKey = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKeyAt), SignatureAlgorithm.HS512.getJcaName());
    }

    public String crateAt(Member member) {
        String email = member.getEmail();
        Role role = member.getRole();
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();
        String token  = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationAt*60*1000L)) // 지금 현재 expirationAt분으로 세팅 (밀리초 단위임)
                // secret키를 통해 signiture 생성
                .signWith(secretKey)
                .compact();
        return token;
    }
}
