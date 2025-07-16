package com.inova.pfms.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.inova.pfms.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Value("${auth.enabled:true}")
    private boolean isAuthEnabled;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {
    	if (!isAuthEnabled) {
            filterChain.doFilter(request, response);
            return;
        }
    	
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;
        String role = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " prefix
            try {
                username = jwtUtil.extractEmail(token);
                role = jwtUtil.extractRole(token);
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.getWriter().write("Token expired");
                return;
            } catch (io.jsonwebtoken.JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.getWriter().write("Invalid token");
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.isTokenValid(token, username)) {
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
