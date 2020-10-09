package com.jjl.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.jjl.pojo.Role;
import com.jjl.pojo.User;
import com.jjl.service.role.RoleServiceImp;
import com.jjl.service.user.UserServiceImp1;
import com.jjl.util.Constants;
import com.jjl.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取他需要做的操作 实现servlet的复用
        String method=req.getParameter("method");
        //应为整个前端后过程中所有的req.getParameter都取不到相应的值，个人感觉应该是前后端编码不一致造成的。
        method="savepwd";
        if(method.equals("savepwd")){
            //更新密码操作
            this.updatePwd(req, resp);
        }else if (method.equals("pwdmodify")&&method!=null){
           //验证旧密码
            this.pwdmodify(req,resp);
        }else if(method.equals("query")&&method!=null){
            this.query(req, resp);

        }

    }
    //因为后面已经搞定了 所以我们只需要搞前端就行了
    public  void query(HttpServletRequest req, HttpServletResponse resp){
        //查询用户列表
        //从前端获取数据 取用户名
        String queryUserName = req.getParameter("queryname");
       // queryUserName="queryname";
        //取下拉列表当中的角色选项
        String temp = req.getParameter("queryUserRole");
        //temp="queryUserRole";
        //下面那个用来判断一页多少行的个数
        String pageIndex = req.getParameter("pageIndex");
        //pageIndex="pageIndex";
        int queryUserRole = 0;
        //获取用户列表
        UserServiceImp1 userServiceImp1=new UserServiceImp1();
        //第一次走这个请求，一定是第一页，页面大小固定
        int pageSize=5;//一页有多少行
        int currentPageNo = 1;//当前再多少行





        //判断前端的东西
        if(queryUserName==null){
          queryUserName="";
        }
        if(temp!=null&&!temp.equals("")){
            queryUserRole = Integer.parseInt(temp);
        }
        if(pageIndex!=null){
            currentPageNo=Integer.parseInt(pageIndex);
        }



        //获取用户的总数  分页（上一页，下一页） 因为非常的麻烦 所以我们用到了工具类PageSupport
        int total = userServiceImp1.getUserCount(queryUserName, queryUserRole);
        //总页数支持
        PageSupport pageSupport=new PageSupport();
        //当前的页面
        pageSupport.setCurrentPageNo(currentPageNo);
        //一页当中能有多少行
        pageSupport.setPageSize(pageSize);
        //
        pageSupport.setTotalPageCount(total);
        int totalPageCount = ((int)(total/pageSize))+1;
        //控制首页和尾页
        //如果页面要小于1了，就显示第一页的东西
        if (currentPageNo<1){
            currentPageNo = 1;
        }else if (currentPageNo>totalPageCount){ //当前页面大于了最后一页；
            currentPageNo = totalPageCount;
        }


        //获取用户列表展示
        List<User> userList = userServiceImp1.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList",userList);
        //获取角色列表
        RoleServiceImp roleServiceImp=new RoleServiceImp();
        List<Role> roleList = roleServiceImp.getRoleList();
        req.setAttribute("roleList",roleList);
        //也需要把相应的参数传进去
        req.setAttribute("totalCount",total);
        req.setAttribute("currentPageNo",currentPageNo);
        //总共有几页
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);
        //上述都是再后端的操作 吐血了 现在来到了简单的一步回到前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        newpassword="1234";
        System.out.println("UserServlet:"+newpassword);
     /*   System.out.println(o);
        System.out.println(StringUtils.isNullOrEmpty(newpassword));*/
        boolean flag = false;
        if (o!=null ){
            UserServiceImp1 userService=new UserServiceImp1();
            flag = userService.updatePwd(((User) o).getId(), newpassword);
            System.out.println(flag);
            if (flag){
                req.setAttribute("message","修改密码成功，请退出，使用新密码登录");
                //密码修改成功，移除当前Session
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else {
                req.setAttribute("message","密码修改失败");
            }
        }else {
            req.setAttribute("message","新密码有问题");
        }
        try {
            //?
            req.getRequestDispatcher("../pwdmodify.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //验证旧密码
    public  void pwdmodify(HttpServletRequest req, HttpServletResponse resp){
        Object attribute = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword=req.getParameter("oldpasswrod");
          oldpassword="1234";
        //用来存放信息
        HashMap<String, String> resultMap = new HashMap<>();
        if(attribute==null){//session过期了
            resultMap.put("result","sessionerror");
        }else if (StringUtils.isNullOrEmpty(oldpassword)){ //输入的密码为空
            resultMap.put("result","error");
        }else {
            String userPassword = ((User) attribute).getUserPassword(); //Session中用户的密码
              userPassword="1234";
            if (oldpassword.equals(userPassword)){
                resultMap.put("result","true");
            }else {
                resultMap.put("result","false");
            }
        }
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            //阿里巴巴的json工具类，转换格式
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
