package com.myweb.firstboot.config;

import com.myweb.firstboot.com.MenuInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private MenuInterceptor menu;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
    //    registry.addInterceptor(menu);
    }
}
