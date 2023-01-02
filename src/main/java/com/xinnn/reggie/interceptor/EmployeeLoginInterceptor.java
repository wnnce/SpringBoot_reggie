package com.xinnn.reggie.interceptor;

import com.xinnn.reggie.utils.BaseContext;
import com.xinnn.reggie.utils.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登陆拦截器
 */
@Slf4j
public class EmployeeLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if(session.getAttribute("employee") != null){
            Long id = (Long) session.getAttribute("employee");
            BaseContext.setCurrentUserId(id);
            return true;
        }
        BaseUtil.sendMessage(response);
        return false;
    }
}
