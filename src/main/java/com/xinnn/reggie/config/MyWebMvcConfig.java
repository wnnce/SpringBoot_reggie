package com.xinnn.reggie.config;

import com.xinnn.reggie.interceptor.EmployeeLoginInterceptor;
import com.xinnn.reggie.interceptor.UserLoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@Slf4j
public class MyWebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        EmployeeLoginInterceptor loginCheckInterceptor = new EmployeeLoginInterceptor();
        UserLoginInterceptor userLoginInterceptor = new UserLoginInterceptor();
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/employee/**", "/dish/**", "/setmeal/**", "/category/**", "/order/**")
                .excludePathPatterns("/employee/login", "/dish/list", "/setmeal/list",
                        "/category/list", "/order/submit", "/order/userPage");
        registry.addInterceptor(userLoginInterceptor)
                .addPathPatterns("/user/**", "/addressBook/**", "/shoppingCart/**", "/order/submit", "/order/userPage")
                .excludePathPatterns("/user/login", "/user/sendMsg");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        //String转LocalDateTime格式转换器
        registry.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTime = LocalDateTime.parse(source, dateTimeFormatter);
                return localDateTime;
            }
        });
    }
}