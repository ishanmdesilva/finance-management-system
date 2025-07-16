package com.inova.pfms.config;

import com.inova.pfms.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
    private JwtAuthenticationFilter jwtFilter;
	@Value("${auth.enabled:true}")
    private boolean isAuthEnabled;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	if (!isAuthEnabled) {
            // Disable authentication
    		return  http
					.cors(cors -> {}) // Enable CORS - all origins
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/api/auth/login", "/api/users/register-initiate","/api/users/register-verification",
                                "/api/auth/forget-password-initiate", "/api/auth/forget-password-verification").permitAll()
                        .anyRequest().permitAll()
                    ).build();
        } else {
			// Enable authentication
	        return http
                    .cors(cors -> {}) // Enable CORS - all origins
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html","/api/auth/login", "/api/users/register-initiate","/api/users/register-verification",
                                    "/api/auth/forget-password-initiate", "/api/auth/forget-password-verification").permitAll()
                        .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }
    }
}
