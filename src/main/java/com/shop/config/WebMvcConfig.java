package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);

//        registry
//            .addResourceHandler("/upload/**") //jsp 페이지에서 /upload/**  이런주소 패턴이 나오면 발동
//            .addResourceLocations("file:///"+uploadPath)
//            .setCachePeriod(60*10*6)  // 1시간
//            .resourceChain(true)
//            .addResolver(new PathResourceResolver());
    }

}