package com.management.accommodation.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.management.accommodation.auth.entity.user.Role.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/user/forgot-password").permitAll()
                        .requestMatchers("/api/v1/user/verify-otp").permitAll()
                        .requestMatchers("/api/v1/user/new-password").permitAll()
                        .requestMatchers("/api/v1/accommodation/student/**").permitAll()
                        .requestMatchers("/api/v1/accommodation/staff/**").permitAll()
                        .requestMatchers("/api/v1/accommodation/admin/getAllStudentAccommodations").hasRole(ADMIN.name())
                        .requestMatchers("/api/v1/accommodation/admin/getStudentAccommodation").hasRole(ADMIN.name())
                        .requestMatchers("/api/v1/accommodation/admin/getAllStaffAccommodations").hasRole(ADMIN.name())
                        .requestMatchers("/api/v1/accommodation/admin/getStaffAccommodation").hasRole(ADMIN.name())
                        .requestMatchers("/api/v1/accommodation/admin/staff-update-status").hasRole(ADMIN.name())
                        .requestMatchers("/api/v1/room").hasRole(ADMIN.name())
                                .requestMatchers("/api/v1/room/revoke-staff-room-space").hasRole(ADMIN.name())
//                        .requestMatchers("/api/v1/accommodation/admin/getAllStudentAccommodations").permitAll()
//                        .requestMatchers("/api/v1/accommodation/admin/getStudentAccommodation").permitAll()
//                        .requestMatchers("/api/v1/accommodation/admin/getAllStaffAccommodations").permitAll()
//                        .requestMatchers("/api/v1/accommodation/admin/getStaffAccommodation").permitAll()
//                        .requestMatchers("/api/v1/accommodation/admin/staff-update-status").permitAll()
//                        .requestMatchers("/api/v1/room").permitAll()
//                                .requestMatchers("/api/v1/room/revoke-staff-room-space").permitAll()
                        .requestMatchers("/api/v1/room/revoke-male-student-room-space").hasRole(BOY_WARDEN.name())
                        .requestMatchers("/api/v1/room/revoke-female-student-room-space").hasRole(GIRL_WARDEN.name())
                        .requestMatchers("/api/v1/accommodation/admin/getAllMaleStudentAccommodations").hasRole(BOY_WARDEN.name())
                        .requestMatchers("/api/v1/accommodation/admin/getMaleStudentAccommodation").hasRole(BOY_WARDEN.name())
                        .requestMatchers("/api/v1/accommodation/admin/getAllFemaleStudentAccommodations").hasRole(GIRL_WARDEN.name())
                        .requestMatchers("/api/v1/accommodation/admin/getFemaleStudentAccommodation").hasRole(GIRL_WARDEN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session-> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout->logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
                .build();
    }
}
