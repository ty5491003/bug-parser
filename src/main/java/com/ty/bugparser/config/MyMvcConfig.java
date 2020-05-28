package com.ty.bugparser.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/SuspiciousResults/list").setViewName("/SuspiciousResults/list");
        registry.addViewController("/User/user").setViewName("/User/user");
        registry.addViewController("/BugAnalyse/BugAnalyse.html").setViewName("/BugAnalyse/BugAnalyse");
        registry.addViewController("/main.html").setViewName("dashboard");
        registry.addViewController("/main").setViewName("dashboard");

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/index.html", "/", "/user/login",
                        "/css/**", "/img/**", "/js/**");
    }

}
