package com.jjl.filter;

import com.jjl.pojo.User;
import com.jjl.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //判断他的session是不是为空 而且他的子类是没有getsession这个选项
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User attribute = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        if(attribute==null){//说明已经被移除 未登录 不饿能让他登陆
            System.out.println("1");
            response.sendRedirect("../error.jsp");
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }
    @Override
    public void destroy() {

    }
}
