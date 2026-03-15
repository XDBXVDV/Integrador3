package com.integrador.toishan;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Esto permite que al entrar a http://localhost:8080/uploads/...
        // Spring busque los archivos en la carpeta física 'uploads'
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}