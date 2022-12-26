package com.foodie.filter;

import com.foodie.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    UserDetailsService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String jwt = httpServletRequest.getHeader("JWT_TOKEN");
        if(jwt != null) {
            String username = null;
            try {
                username = jwtUtil.extractUserName(jwt);
            }catch(Exception ex) {
                throw new BadCredentialsException("Session Invalid or Expired");
            }
            if(username != null) {
                Authentication usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(username, null, detailsService.loadUserByUsername(username).getAuthorities());
                httpServletResponse.setHeader("JWT_TOKEN", jwt);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            else {
                throw new BadCredentialsException("Invalid Token");
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
