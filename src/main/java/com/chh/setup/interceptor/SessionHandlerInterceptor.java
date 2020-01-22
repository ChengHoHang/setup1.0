package com.chh.setup.interceptor;

import com.chh.setup.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author chh
 * @date 2020/1/21 22:01
 */
@Component
public class SessionHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserEntity user = (UserEntity) request.getSession().getAttribute("user");
        if (user == null) { /*若没登陆跳转至首页*/
//            response.sendRedirect("/");
            returnJson(response,"{\"code\":201,\"msg\":\"用户未登录\"}");
            return false;
        }
        return true;
    }
    
    /*返回客户端数据*/
    private void returnJson(HttpServletResponse response, String json) throws Exception{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(json);
        }  catch (IOException ignored) {
        }
    }

}
