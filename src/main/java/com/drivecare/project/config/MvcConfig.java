package com.drivecare.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @SuppressWarnings("null")
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Esta configuração mapeia a URL para o diretório no sistema de arquivos
        registry
            .addResourceHandler("/uploads/**") // O padrão de URL que o navegador vai usar
            .addResourceLocations("file:uploads/"); // Onde os arquivos estão fisicamente no servidor
    }
}