package com.project.filter;

import com.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class MarketFilter implements Filter {

    private final StringRedisTemplate redisTemplate;
    private final UserService userService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        if(httpRequest.getRequestURI().contains("user/infoByNumber")){
            String phoneNumber = httpRequest.getHeader("phoneNumber");
            String token = userService.userInfo(phoneNumber);
            httpResponse.addHeader("accessToken", token);
            return;
        }

        String token = httpRequest.getHeader("accessToken");

        if (token != null && checkKodInRedis(token)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            servletResponse.setContentType("application/json");
            servletResponse.setCharacterEncoding("UTF-8");
            servletResponse.getWriter().write("{\"message\": \"Unauthorized - Oturum süreniz dolmuştur.\"}");
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private boolean checkKodInRedis(String kod) {
        ValueOperations<String, String> ops = this.redisTemplate.opsForValue();
        return ops.get(kod) != null;
    }
}
