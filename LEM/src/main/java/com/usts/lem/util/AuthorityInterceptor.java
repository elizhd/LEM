package com.usts.lem.util;


import com.usts.lem.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 权限拦截器
 * @Author Haodong Zhao
 */
public class AuthorityInterceptor implements HandlerInterceptor {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    // 不需要拦截的请求
    private static final String[] IGNORE_URI = {"/user/login", "/user/logout",
            "/user/isRegistered", "/user/register", "/index.html",
            "/vendor/js", "/register.html", "/vendor/css", "/vendor/img",
            "not_login.html", "not_manager.html"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        boolean flag = false; // 默认用户没有登录
        String servletPath = request.getServletPath(); // 获得请求的ServletPath
        log.debug("SERVER[AuthorityInterceptor] ServletPath: " + servletPath);
        String requestUri = request.getRequestURI(); //请求完整路径，可用于登陆后跳转
        String contextPath = request.getContextPath();  //项目下完整路径
        String url = requestUri.substring(contextPath.length()); //请求页面
        log.debug("SERVER[AuthorityInterceptor] Request Uri: " + requestUri);
        log.debug("SERVER[AuthorityInterceptor] Url: " + url);

        //判断请求是否需要拦截
        for (String s : IGNORE_URI) {
            if (servletPath.contains(s)) {
                log.debug("SERVER do not intercept: " + servletPath);
                return true;
            }
        }

        log.debug("SERVER intercept");
        User user = (User) request.getSession().getAttribute("userObj");

        if (user == null) {
            log.debug("SERVER do not find user");
            log.debug("SERVER to " + "not_login.html");
            if (request.getHeader("x-requested-with") != null
                    && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                response.setHeader("SESSIONSTATUS", "TIMEOUT");
                response.setHeader("CONTEXTPATH", contextPath + "/index.html");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {
                // 如果不是 ajax 请求，则直接跳转即可
                response.sendRedirect(contextPath + "/html/not_login.html");
            }
            // request.getRequestDispatcher( "/html/not_login.html").forward(request, response);
            // response.sendRedirect(contextPath + "/html/not_login.html");
        } else if (!user.isRole() && servletPath.contains("/apply.html")) {
            log.debug("User is not manager");
            log.debug("SERVER to " + "not_login.html");
            request.getRequestDispatcher("/html/not_manager.html").forward(request, response);
            // response.sendRedirect(contextPath + "/html/not_manager.html");
        } else {
            log.debug("SERVER Get User: " + user.toString());
            flag = true;
        }


        return flag;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
