package com.example.ordersystem.common.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JwtTokenFilter extends GenericFilter {
    @Value("${jwt.secretKeyAt}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            String bearerToken = req.getHeader("Authorization");
            if (bearerToken == null) {
                // token이 없는 경우 다시 filterchain으로 되돌아가는 로직
                chain.doFilter(request, response);
                return;
            }
            String token = bearerToken.substring(7);
            // token 검증 및 claims 추출
            Claims claims =  Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


            List<GrantedAuthority> authorities = new ArrayList<>();
            //authentication객체를 만들 때 권한은 ROlE_ 라는 키워드를 붙여서 만들어 주는 것이 추후 문제 발생 X
            authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
            Authentication authentication  = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        chain.doFilter(request,response);
    }
}
