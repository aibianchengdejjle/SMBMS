package com.jjl.dao.user;

import com.jjl.pojo.Role;
import com.jjl.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    //得到要登陆的用户
    public User getloginuser(Connection connection, String userCode, String userPassword);
    //修改当前用户密码
    public int uppdataPwd(Connection connection,int id,String userPassword);
    //查询用户总数
    public  int getUserCount(Connection connection,String username,int userRole) throws SQLException;
    //通过条件查询用户
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize)throws Exception;
    //获取角色列表
    public List<Role> getRoleList(Connection connection);
}
