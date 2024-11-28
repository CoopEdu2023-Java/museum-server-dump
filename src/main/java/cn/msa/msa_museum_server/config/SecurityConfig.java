package cn.msa.msa_museum_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 禁用 CSRF（适用于无状态的 REST API）
                .csrf(csrf -> csrf.disable())
                // 允许所有请求，无需认证
                .authorizeHttpRequests(req -> req.anyRequest().permitAll())
                // 不需要会话管理
                .sessionManagement(session -> session.disable())
                // 构建 SecurityFilterChain
                .build();
    }
}