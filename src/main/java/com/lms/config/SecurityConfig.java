

package com.lms.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

import com.lms.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.lms.filter.JwtAuthFilter;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http        
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:5173", "https://lms-backend-ol4a.onrender.com", "https://upskillnow.netlify.app"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                //for swagger ui 
                .requestMatchers(
                    "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                                ).permitAll()


                // ADMIN-only endpoints
                .requestMatchers(HttpMethod.POST, "/category/add").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/category/update").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/category/delete").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/enrollment/fetch/all").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/user/mentor/delete").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/user/fetch/role-wise").hasRole("ADMIN")

                // MENTOR-only endpoints
                .requestMatchers(HttpMethod.POST, "/courses/add").hasRole("MENTOR")
                .requestMatchers(HttpMethod.POST, "/courses/section/add").hasRole("MENTOR")
                .requestMatchers(HttpMethod.POST, "/courses/section/topic/add").hasRole("MENTOR")
                .requestMatchers(HttpMethod.GET, "/courses/mentor/dashboard").hasRole("MENTOR")
                .requestMatchers(HttpMethod.GET, "/courses/fetch/mentor-wise").hasRole("MENTOR")
                .requestMatchers(HttpMethod.DELETE, "/courses/delete").hasRole("MENTOR")
                .requestMatchers(HttpMethod.POST, "/user/mentordetail/add").hasRole("MENTOR")
                .requestMatchers(HttpMethod.GET, "/user/fetch/mentor-id").hasRole("MENTOR")
                .requestMatchers(HttpMethod.GET, "/enrollment/fetch/mentor-wise").hasRole("MENTOR")
                

                // LEARNER-only endpoints
                .requestMatchers(HttpMethod.POST, "/enrollment/enroll").hasRole("LEARNER")
                .requestMatchers(HttpMethod.GET, "/enrollment/fetch/learner-wise").hasRole("LEARNER")
                .requestMatchers(HttpMethod.GET, "/courses/fetch/course-user-id").hasRole("LEARNER")
                .requestMatchers(HttpMethod.GET, "/courses/fetch/youtube").hasRole("LEARNER")

                // Public endpoints
                .requestMatchers(HttpMethod.POST, "/user/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/category/fetch/all").permitAll()
                .requestMatchers(HttpMethod.GET, "/courses/fetch/status-wise").permitAll()
                .requestMatchers(HttpMethod.GET, "/courses/fetch/name-wise").permitAll()
                .requestMatchers(HttpMethod.GET, "/courses/fetch/category-wise").permitAll()
                .requestMatchers(HttpMethod.GET, "/courses/fetch/course-id").permitAll()
                .requestMatchers(HttpMethod.GET, "/courses/fetch/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/user/fetch/*").permitAll()

                // CORS preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // All other endpoints require authentication
                .anyRequest().authenticated()

                )
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        // provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}


