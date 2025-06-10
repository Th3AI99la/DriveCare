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
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público a estas páginas e recursos estáticos
                        .requestMatchers("/login", "/register", "/styles/**", "/scripts/**", "/images/**").permitAll()
                        // Qualquer outra requisição (incluindo a raiz "/") precisa de autenticação
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") 
                        .loginProcessingUrl("/login") 
                        .defaultSuccessUrl("/", true) 
                        .failureUrl("/login?error=true") 
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL para acionar o logout
                        .logoutSuccessUrl("/login?logout") 
                        .invalidateHttpSession(true) 
                        .deleteCookies("JSESSIONID") 
                        .permitAll()
                );

        return http.build();
    }
}
