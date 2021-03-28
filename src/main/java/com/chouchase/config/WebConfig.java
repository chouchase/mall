package com.chouchase.config;

import com.chouchase.interceptor.ManagerAuthorizationInterceptor;
import com.chouchase.interceptor.UserAuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserAuthorizationInterceptor())
                .addPathPatterns("/user/**", "/cart/**","/shipping/**").
                excludePathPatterns("/user/register","/user/login","/user/check/**","/user/forget/**");
        registry.addInterceptor(new ManagerAuthorizationInterceptor())
                .addPathPatterns("/manage/**").excludePathPatterns("/manage/user/login","/manage/user/register");
    }
}
