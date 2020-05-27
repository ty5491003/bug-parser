package com.ty.bugparser.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("dashboard");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/SuspiciousResults/list").setViewName("/SuspiciousResults/list");
//        registry.addViewController("/main.html").setViewName("dashboard");
//        registry.addViewController("/main").setViewName("dashboard");
//        registry.addViewController("/employee/list").setViewName("/employee/list");

    }

}
