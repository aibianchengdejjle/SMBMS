package com.jjl.servlet.user;

import com.jjl.pojo.User;
import com.jjl.service.user.UserServiceImp1;
import com.jjl.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    //处理登陆请求的 servlet 要去调用业务层代码
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        //和数据库中的代码进行对比
        UserServiceImp1 userServiceImp1=new UserServiceImp1();
        User user = userServiceImp1.login(userCode, userPassword);
        if(user!=null&&user.getUserPassword()!=null){//查有此人  可以把用户的信息放入session
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            //这时我们干的就是要跳转页面了
            resp.sendRedirect("jsp/frame.jsp");
        }else{
            //转发回登陆页面
            req.setAttribute("error","用户名或者密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }
}
