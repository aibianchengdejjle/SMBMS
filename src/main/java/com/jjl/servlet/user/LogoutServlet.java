package com.jjl.servlet.user;

import com.jjl.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       //移除session
        req.getSession().removeAttribute(Constants.USER_SESSION);
        //注销之后要回到相应的登陆界面
        resp.sendRedirect("../login.jsp");
    }
}
