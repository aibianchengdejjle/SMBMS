package com.jjl.dao.user;

import com.jjl.dao.BaseDao;
import com.jjl.pojo.User;
import com.jjl.pojo.Role;
import com.mysql.jdbc.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class userDaoimp implements  UserDao {

    @Override
    public User getloginuser(Connection connection, String userCode, String userPassword) {
        PreparedStatement pstm=null;
        ResultSet rs=null;
        User user=null;
        if(connection!=null){
            String sql="select * from smbms_user where userCode=? and userPassword=?";
            Object []parms={userCode,userPassword};
            try {
                rs= BaseDao.excute(connection,pstm,rs,sql,parms);
                user=new User();
                if(rs.next()){
                    user.setId(rs.getInt("id"));
                    user.setUserCode(rs.getString("userCode"));
                    user.setUserName(rs.getString("userName"));
                    user.setUserPassword(rs.getString("userPassword"));
                    user.setGender(rs.getInt("gender"));
                    user.setBirthday(rs.getDate("birthday"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setUserRole(rs.getInt("userRole"));
                    user.setCreatedBy(rs.getInt("createdBy"));
                    user.setCreationDate(rs.getTimestamp("creationDate"));
                    user.setModifyBy(rs.getInt("modifyBy"));
                    user.setModifyDate(rs.getTimestamp("modifyDate"));
                }
                //链接可能存在事务 我们要在业务层对其进行关闭
                BaseDao.close(null,pstm,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
      return user;
    }

    @Override
    public int uppdataPwd(Connection connection, int id, String userPassword) {
        int row=0;
        String sql="update smbms_user set userPassword=? where id=?";
        PreparedStatement preparedStatement=null;
        Object []parms={userPassword,id};
        if(connection!=null){
            try {
                row= BaseDao.update(connection,sql,parms,preparedStatement);
                //connection要在
                BaseDao.close(null,preparedStatement,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return  row;
    }
    //根据用户名和用户总数将其查出来
    @Override
    public int getUserCount(Connection connection, String username, int userRole) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        if (connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id");
            ArrayList<Object> list = new ArrayList<Object>();//存放我们的参数
            if (!StringUtils.isNullOrEmpty(username)){
                sql.append(" and u.userName like ?");
                list.add("%"+username+"%"); //index:0
            }
            if (userRole>0){
                sql.append(" and u.userRole = ?");
                list.add(userRole); //index:1
            }
            //怎么把List转换为数组
            Object[] params = list.toArray();
            System.out.println("UserDaoImpl->getUserCount:"+sql.toString()); //输出最后完整的SQL语句
            rs = BaseDao.excute(connection, pstm, rs, sql.toString(), params);

            if (rs.next()){
                count = rs.getInt("count"); //从结果集中获取最终的数量
            }
            BaseDao.close(null,pstm,rs);
        }
        return count;
    }

    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<User>();
        if(connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole > 0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            //在数据库中用到了分页的方式
            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("sql ----> " + sql.toString());
            rs = BaseDao.excute(connection, pstm, rs, sql.toString(), params);
            while(rs.next()){
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.close(null, pstm, rs);
        }
        return userList;
    }
    //获取角色列表
    @Override
    public List<Role> getRoleList(Connection connection) {
        ArrayList<Role> roleList = new ArrayList<Role>();
        if(connection!=null){
            PreparedStatement preparedStatement=null;
            ResultSet resultSet=null;
            String sql="select * from smbms_role";
            Object []parms={};
            try {
               resultSet= BaseDao.excute(connection,preparedStatement,resultSet,sql,parms);
               while (resultSet.next()){
                   Role _role=new Role();
                    _role.setId(resultSet.getInt("id"));
                   _role.setRoleCode(resultSet.getString("roleCode"));
                   _role.setRoleName(resultSet.getString("roleName"));
                   roleList.add(_role);
               }
            } catch (Exception e) {
                e.printStackTrace();
            }
            BaseDao.close(null, preparedStatement, resultSet);
        }
        return roleList;
    }
}
