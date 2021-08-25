package com.second.maproject;

import jdk.jfr.Category;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path imageUploadDir = Paths.get("./user_profilePics");
        String imageUploadPath = imageUploadDir.toFile().getAbsolutePath();

        Path postUploadDir = Paths.get("./postUploads");
        String postUploadPath = postUploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/user_profilePics/**").addResourceLocations("file:/" + imageUploadPath + "/");
        registry.addResourceHandler("/postUploads/**").addResourceLocations("file:/" + postUploadPath + "/");
    }

}
