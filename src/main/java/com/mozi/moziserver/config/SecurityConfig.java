package com.mozi.moziserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozi.moziserver.common.Constant;
import com.mozi.moziserver.log.ApiLogFilter;
import com.mozi.moziserver.repository.RememberMeTokenRepository;
import com.mozi.moziserver.security.*;
import com.mozi.moziserver.security.UserEmailSignInService;
import com.mozi.moziserver.security.RememberMeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@SuppressWarnings("unused")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserSocialAuthenticationProvider userSocialAuthenticationProvider;

    @Autowired
    private RememberMeTokenRepository rememberMeTokenRepository;

    @Autowired
    private UserEmailSignInService userEmailSignInService;

    @Autowired
    private RememberMeService rememberMeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userEmailSignInService)
                .and()
                .authenticationProvider(userSocialAuthenticationProvider)
                .authenticationProvider(new UserRememberAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String activeProfiles = String.join(",", env.getActiveProfiles());

        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .addFilterBefore(new ApiLogFilter(activeProfiles, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(Constant.PERMIT_ALL_PATHS).permitAll()
                .antMatchers(Constant.AUTHENTICATED_PATHS).authenticated()
                .antMatchers(Constant.ROLE_USER_PATHS).hasRole(Constant.ROLE_USER)
                .anyRequest().permitAll()
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new DefaultAuthenticationEntryPoint())
                .accessDeniedHandler(new DefaultAccessDeniedHandler())
                .and()
                .rememberMe(configurer -> configurer
                        .alwaysRemember(true)
                        .rememberMeServices(rememberMeService))
                .logout()
                .logoutUrl("/api/v1/users/signout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                })
                .deleteCookies("JSESSIONID",  "remember-me");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @SuppressWarnings("unused")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
