package com.mozi.moziserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.log.ApiLogFilter;
import com.mozi.moziserver.repository.RememberMeTokenRepository;
import com.mozi.moziserver.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@SuppressWarnings("unused")
public class SecurityConfig {

    private final UserSocialAuthenticationProvider userSocialAuthenticationProvider;

    private final RememberMeTokenRepository rememberMeTokenRepository;

    private final UserEmailSignInService userEmailSignInService;

    private final RememberMeService rememberMeService;

    private final ObjectMapper objectMapper;

    private final Environment env;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userEmailSignInService);
        authenticationManagerBuilder.authenticationProvider(userSocialAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(new UserRememberAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // 3
        final String activeProfiles = String.join(",", env.getActiveProfiles());

        http
                .authenticationManager(authManager(http))
                .csrf(AbstractHttpConfigurer::disable)
                .headers((headerConfig) ->
                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable
                        )
                )
                .addFilterBefore(new ApiLogFilter(activeProfiles, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling.authenticationEntryPoint(new DefaultAuthenticationEntryPoint())
                                .accessDeniedHandler(new DefaultAccessDeniedHandler())
                )
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers(Constant.PERMIT_ALL_PATHS).permitAll()
                                .requestMatchers(Constant.AUTHENTICATED_PATHS).authenticated()
                                .requestMatchers(Constant.ROLE_ADMIN_PATHS).hasRole(Constant.ROLE_ADMIN)
                                .requestMatchers(Constant.ROLE_USER_PATHS).hasRole(Constant.ROLE_USER)
                                .anyRequest().permitAll()
                )
                .rememberMe((rememberMe) ->
                        rememberMe
                                .alwaysRemember(true)
                                .rememberMeServices(rememberMeService)
                )
                .logout((logout) ->
                        logout
                                .logoutUrl("/api/v1/users/signout")
                                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                                .deleteCookies("JSESSIONID", "remember-me")
                );

        return http.build();
    }

    @Bean
    @SuppressWarnings("unused")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
