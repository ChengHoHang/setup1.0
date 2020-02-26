package com.chh.setup.interceptor;

import com.chh.setup.entity.UserEntity;
import com.chh.setup.exception.CustomizeErrorCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @author chh
 * @date 2020/1/21 22:01
 */
@Component
public class SessionHandlerInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String[] urlSplit = request.getRequestURI().split("/");
        if ("u".equals(urlSplit[1]) && !StringUtils.isNumeric(urlSplit[2])) {
            response.sendRedirect("/error.html");
        }
        UserEntity sessionUser = (UserEntity) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            response.sendRedirect("/error.html?"
                    + CustomizeErrorCode.USER_LOG_OUT.getErrorCode()
                    + "&"
                    + URLEncoder.encode(CustomizeErrorCode.USER_LOG_OUT.getErrorMsg(), "UTF-8"));
        }
        return true;
    }
}
