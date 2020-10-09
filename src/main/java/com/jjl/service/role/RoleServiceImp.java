package com.jjl.service.role;

import com.jjl.dao.BaseDao;
import com.jjl.dao.user.UserDao;
import com.jjl.dao.user.userDaoimp;
import com.jjl.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

public class RoleServiceImp implements  RoleService {
    private UserDao userDao;
    public  RoleServiceImp(){
        userDao=new userDaoimp();

    }
    @Override
    public List<Role> getRoleList() {
        Connection connection=null;
        List<Role> roleList = null;
        try {
            connection = BaseDao.getConnection();
             roleList = userDao.getRoleList(connection);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            BaseDao.close(connection,null,null);
        }
        return roleList;
    }
   /* @Test
    public  void  test(){
        RoleServiceImp roleServiceImp=new RoleServiceImp();
        List<Role> roleList = roleServiceImp.getRoleList();
        for (Role r:roleList
             ) {
            System.out.println(r.getRoleName());
        }
    }*/
}
