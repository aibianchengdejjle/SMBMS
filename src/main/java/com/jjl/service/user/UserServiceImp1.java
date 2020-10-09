package com.jjl.service.user;

import com.jjl.dao.BaseDao;
import com.jjl.dao.user.UserDao;
import com.jjl.dao.user.userDaoimp;
import com.jjl.pojo.User;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImp1 implements userservice {
    //业务层都会调用dao层
    private UserDao userDao;
    public  UserServiceImp1(){
        userDao=new userDaoimp();

    }
    //通过业务层放入到Dao层
    @Override
    public User login(String userCode, String userPassword) {
        Connection connection=null;
        User user=null;
        connection= BaseDao.getConnection();
        //通过接口传进相应的参数
         user= userDao.getloginuser(connection, userCode,userPassword);
         //其他的亮相在Dao层就已经的到了关闭
         BaseDao.close(connection,null,null);
         return user;
    }

    @Override
    public int getUserCount(String username, int userRole) {
        Connection connection=BaseDao.getConnection();
        int count=0;
        try {
            count=  userDao.getUserCount(connection,username,userRole);
            BaseDao.close(connection,null,null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  count;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        System.out.println("queryUserName ---- > " + queryUserName);
        System.out.println("queryUserRole ---- > " + queryUserRole);
        System.out.println("currentPageNo ---- > " + currentPageNo);
        System.out.println("pageSize ---- > " + pageSize);
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, queryUserName,queryUserRole,currentPageNo,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDao.close(connection, null, null);
        }
        return userList;
    }

    @Override
    public boolean updatePwd(int id, String password) {
        //有可能出现事务 所以把connection放在业务层
        Connection connection=null;
        boolean flag=false;
        connection= BaseDao.getConnection();
        //修改密码
        int row=0;
        row=userDao.uppdataPwd(connection,id,password);
        if(row>0){
            flag=true;
        }else {
            flag=false;
        }
        BaseDao.close(connection,null,null);
        return flag;
    }
    //测试成功
 /*  @Test
    public  void test(){
        UserServiceImp1 userServiceImp1=new UserServiceImp1();
        int x=userServiceImp1.getUserCount(null,0);
       System.out.println(x);
    }*/
}
