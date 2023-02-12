package com.lt.gulimall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName GulimallWebConfig
 * @Description:
 * @Author lite
 * @Date 2023/2/12
 * @Version V1.0
 **/
@Configuration
public class GulimallWebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("").setViewName("login");
        registry.addViewController("reg.html").setViewName("reg");
    }
}
