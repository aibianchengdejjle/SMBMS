package com.jjl.service.user;

import com.jjl.pojo.Role;
import com.jjl.pojo.User;

import java.util.List;

public interface userservice {
    //用户登陆
    public User login(String userCode, String password);
    //根据用户id修改密码
    public boolean updatePwd(int id,String password);
    //查询记录用户的总数
    public  int getUserCount(String username,int userRole);
    //根据条件查询用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);
    //获取角色查询
}
