package com.example.ordersystem.common.Auth;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JwtTokenFilter extends GenericFilter {
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
        } catch (Exception e) {
            e.getMessage();
        }
        chain.doFilter(request,response);
    }
}
