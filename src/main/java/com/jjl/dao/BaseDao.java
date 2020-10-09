package com.jjl.dao;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private  static  String driver;
    private static String url;
    private static String username;
    private static String password;
    //类加载的时候就初始化
    static {
        //利用反射来读取流
        Properties properties = new Properties();
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            //读取properties流
            properties.load(is);
        }catch (Exception e){
            e.printStackTrace();
        }
        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
        //获取数据库的链接

    }
    public  static Connection getConnection(){
        Connection connection=null;
        try {
            Class.forName(driver);
             connection= DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return  connection;
    }
    //编写查询公共类
    public  static ResultSet excute(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet, String sql, Object []params) throws SQLException {

        try {
                 preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i <params.length ; i++) {
                //占位符从1开始 数组从0开始
                preparedStatement.setObject(i+1,params[i]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //预编译的sql 语句直接在后面执行就可以了
         resultSet = preparedStatement.executeQuery();
        return resultSet;
    }
    //编写增删改工具类
    public  static  int update(Connection connection, String sql, Object []params,PreparedStatement preparedStatement) throws SQLException {
        try {
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i <params.length ; i++) {
                //占位符从1开始 数组从0开始
                preparedStatement.setObject(i+1,params[i]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //预编译的sql 语句直接在后面执行就可以了
        int row=preparedStatement.executeUpdate();
        return row;
    }
    //关闭链接
    public  static  boolean close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet){
        boolean flag= true;
        if(resultSet!=null){
            try {
                resultSet.close();
                //GC回收
                resultSet=null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }
        }
        if (preparedStatement!=null){
            try {
                preparedStatement.close();
                //GC回收
                preparedStatement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (connection!=null){
            try {
                connection.close();
                //GC回收
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}
