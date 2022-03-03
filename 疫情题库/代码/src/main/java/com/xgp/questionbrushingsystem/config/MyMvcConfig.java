package com.xgp.questionbrushingsystem.config;//package com.xgp.springboot.config;

import com.xgp.questionbrushingsystem.component.LoginHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//使用WebMvcConfigurer可以来扩展springmvc的功能
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //将路径/映射到登陆页面
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/register.html").setViewName("register");
        registry.addViewController("/findmyuser").setViewName("findmyuser");
        registry.addViewController("/findmyuser.html").setViewName("findmyuser");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/main").setViewName("main");
        registry.addViewController("/main.html").setViewName("main");
        registry.addViewController("/personInfo").setViewName("personInfo");
        registry.addViewController("/personInfo.html").setViewName("personInfo");
        registry.addViewController("/changepwd.html").setViewName("changepwd");
        registry.addViewController("/changepwd").setViewName("changepwd");
        registry.addViewController("/randpage").setViewName("randpage");
        registry.addViewController("/randpage.html").setViewName("randpage");
        registry.addViewController("/orderpage").setViewName("orderpage");
        registry.addViewController("/orderpage.html").setViewName("orderpage");
        registry.addViewController("/showranderror").setViewName("showranderror");
        registry.addViewController("/showranderror.html").setViewName("showranderror");
        registry.addViewController("/showordererror").setViewName("showordererror");
        registry.addViewController("/showordererror.html").setViewName("showordererror");
        registry.addViewController("/showranderror2").setViewName("showranderror2");
        registry.addViewController("/showranderror2.html").setViewName("showranderror2");
        registry.addViewController("/randpage2").setViewName("randpage2");
        registry.addViewController("/randpage2.html").setViewName("randpage2");
        registry.addViewController("/tip1").setViewName("tip1");
        registry.addViewController("/tip1.html").setViewName("tip1");
        registry.addViewController("/errorexport").setViewName("errorexport");
        registry.addViewController("/errorexport.html").setViewName("errorexport");
        registry.addViewController("/errorspace").setViewName("errorspace");
        registry.addViewController("/errorspace.html").setViewName("errorspace");
        registry.addViewController("/errorsbin").setViewName("errorsbin");
        registry.addViewController("/errorsbin.html").setViewName("errorsbin.html");
        registry.addViewController("/searchlib").setViewName("searchlib");
        registry.addViewController("/searchlib.html").setViewName("searchlib");
        registry.addViewController("/searchlib2").setViewName("searchlib2");
        registry.addViewController("/searchlib2.html").setViewName("searchlib2");

    }

    //            注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//                WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns(
                "/login","/login.html","/register","/register.html","/findmyuser","/findmyuser.html","/mail/**","/captcha/**",
                "/common/**","/css/**","/easyui/**","/images/**","/js/**","/json/**","/jsplug/**","/favicon.ico",
                "/user/loginMethod","/user/usernameisonly","/user/emailisonly","/user/register","/user/updateByEmail","/error/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/**");
    }
}




