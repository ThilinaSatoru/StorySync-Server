package com.satoru.pdfadmin.config;

import com.satoru.pdfadmin.utils.StringToTimestampConverter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.root-dir}")
    private String rootDir;

    @Value("${player.path}")
    private String playerDir;

    @PostConstruct
    public void init() {
        File directory = new File(rootDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToTimestampConverter());
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all endpoints
                        .allowedOrigins("http://localhost:3000") // Allow your frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific HTTP methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow cookies or authentication headers
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/**")
                        .addResourceLocations("file:" + rootDir + "/")
                        .setCachePeriod(3600)
                        .resourceChain(true)
                        .addResolver(new PathResourceResolver());
            }
        };
    }

    public static ResponseEntity<Resource> getResourceResponseVideoEntity(Resource resource) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");

        return ResponseEntity.ok()
                .headers(headers)
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:3000")
                .header("Cross-Origin-Resource-Policy", "same-site") // Add this for COEP
                .header("Cross-Origin-Embedder-Policy", "require-corp") // Optional
                .body(resource);
    }
}