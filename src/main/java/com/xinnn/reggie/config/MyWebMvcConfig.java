package com.xinnn.reggie.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.xinnn.reggie.base.ReggieException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * webmvc配置类
 */
@Configuration
@Slf4j
public class MyWebMvcConfig implements WebMvcConfigurer {
    /**
     * 配置拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //使用Sa-Token的多账号鉴权进行登陆验证
        registry.addInterceptor(new SaInterceptor(handler -> {
            //拦截路径 放行路径 使用什么进行鉴权
            SaRouter.match("/employee/**", "/dish/**", "/setmeal/**", "/category/**", "/order/**")
                    .notMatch("/dish/list", "/setmeal/list", "/category/list","/employee/login",
                            "/order/submit", "/order/userPage", "/order/again")
                    .check(r -> StpUtil.checkLogin());
            SaRouter.match("/user/**", "/addressBook/**", "/shoppingCart/**", "/order/submit",
                    "/order/userPage", "/order/again")
                    .notMatch("/user/login", "/user/sendMsg")
                    .check(r -> StpUserUtil.checkLogin());
            SaRouter.match("/dish/list", "/setmeal/list", "/category/list")
                    .check(r -> {
                        if (!StpUtil.isLogin() && !StpUserUtil.isLogin()){
                            throw new ReggieException("NOTLOGIN");
                        }
                    });
            //关闭注解鉴权支持
        }).isAnnotation(false));
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
