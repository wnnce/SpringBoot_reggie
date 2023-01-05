package com.xinnn.reggie.interceptor;

import com.xinnn.reggie.utils.BaseContext;
import com.xinnn.reggie.utils.BaseUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户登陆拦截器
 */
@ControllerAdvice
public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if(session.getAttribute("user") != null){
            Long id = (Long) session.getAttribute("user");
            //在本地线程中设置用户id
            BaseContext.setCurrentUserId(id);
            return true;
        }
        BaseUtil.sendMessage(response);
        return false;
    }
}
