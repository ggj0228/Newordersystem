package com.example.ordersystem.common.Auth;

import com.example.ordersystem.member.domain.Member;
import com.example.ordersystem.member.domain.Role;
import com.example.ordersystem.member.repository.MemeberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final MemeberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;


    // Qualifier는 기본적으로 매서드를 통한 주입 가능, 그래서 이 경우 생성자 주입방식을 해야 Qualifier를 사용 가능
    public JwtTokenProvider(MemeberRepository memberRepository, RedisTemplate<String, String> redisTemplate) {
        this.memberRepository = memberRepository;
        this.redisTemplate = redisTemplate;
    }

    @Value("${jwt.expirationAt}")
    private int expirationAt;

    @Value("${jwt.secretKeyAt}")
    private String secretKeyAt;
    @Value("${jwt.expirationRt}")
    private int expirationRt;

    @Value("${jwt.secretKeyRt}")
    private String secretKeyRt;

    private Key secretKeyAtToken;
    private Key secretKeyRtToken;


    @PostConstruct
    public void init() {
        secretKeyAtToken = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKeyAt), SignatureAlgorithm.HS512.getJcaName());
        secretKeyRtToken = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKeyRt), SignatureAlgorithm.HS512.getJcaName());

    }

    public String createAtToken(Member member) {
        String email = member.getEmail();
        Role role = member.getRole();
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationAt*60*1000L)) // 지금 현재 expirationAt분으로 세팅 (밀리초 단위임)
                // secret키를 통해 signiture 생성
                .signWith(secretKeyAtToken)
                .compact();
        return accessToken;
    }
    public String createRtToken(Member member) {
        // 유효기간이 긴 rt 토큰 생성
        String email = member.getEmail();
        Role role = member.getRole();
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationRt*60*1000L)) // 지금 현재 expirationAt분으로 세팅 (밀리초 단위임)
                // secret키를 통해 signiture 생성
                .signWith(secretKeyRtToken)
                .compact();


        // rt 토큰을 redis에 저장
        redisTemplate.opsForValue().set(member.getEmail(), refreshToken);
//        redisTemplate.opsForValue().set(member.getEmail(), refreshToken, 200, Time.Unit.DAYS); // 200일 ttl, 토큰 검증애서 걸림
        return refreshToken;




    }

    public Member validateRefreshToken(String refreshToken) {
        //rt 토큰 검증
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeyRtToken)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();
        String email = claims.getSubject();
        Member member = this.memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));

        //redis의 값과 비교하는 검증
        String redisRt = redisTemplate.opsForValue().get(member.getEmail());
        if(!refreshToken.equals(redisRt)){
            throw new IllegalArgumentException("rt 토큰가 일치하지 않습니다.");
        }
        return member;

    }

}
