package com.project.eat.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private String connectPath = "/uploadimgPath/**";
 //   private String resourcePath = "file:///C:/upload/";
//    private String resourcePath = "file:///root/upload/";
    @Value("${upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("{}",uploadDir);
        registry.addResourceHandler(connectPath)
                .addResourceLocations("file:///"+uploadDir);
    }
}
