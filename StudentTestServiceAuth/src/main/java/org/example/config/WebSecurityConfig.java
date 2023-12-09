package org.example.config;

import org.example.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    JwtFilter jwtFilter;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                 //Добавляю свой фильтр
                .addFilterBefore(jwtFilter, AnonymousAuthenticationFilter.class)

                .csrf().disable()
                .cors().disable()

                .authorizeRequests()
                .antMatchers("/content")
                .authenticated()
                .anyRequest()
                .permitAll()

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return  http.build();
    }
}