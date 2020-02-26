package com.chh.setup.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author chh
 * @date 2020/1/21 21:58
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    SessionHandlerInterceptor sessionHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) { // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
        registry.addInterceptor(sessionHandlerInterceptor)
                .addPathPatterns("/u/*", "/my-notice/*");
    }
}
