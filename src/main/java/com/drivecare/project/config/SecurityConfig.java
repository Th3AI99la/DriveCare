package com.drivecare.project.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/login", "/register", "/css/**", "/js/**").permitAll() // Permite acesso à página de login, registro e recursos estáticos
                        .anyRequest().authenticated() // Requer autenticação para todas as outras requisições
                )
                .formLogin((form) -> form
                        .loginPage("/login") // Define a página de login personalizada
                        .defaultSuccessUrl("/index", true) // Redireciona para /home após o login
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }
}