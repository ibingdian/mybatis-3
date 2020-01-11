package com.ly.jdbc;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCTest {
  public static void main(String[] args) throws Exception {
    // 1、加载驱动
//    Class.forName("com.mysql.jdbc.Driver");
    // 2、获取数据库的连接对象
//    Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/tmp_ly?serverTimezone=UTC","root","123456ly");
    Connection connection = getConnection();
    // 3、定义sql
    String sql = "select * from person where id = ?";
    // 4、获取执行sql语句的对象 PreparedStatement
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1,1);
    // 5、执行sql，接收返回的结果
    ResultSet rs = statement.executeQuery();
    //循环判断游标是否是最后一行末尾。
    while(rs.next()){
      // 6、获取数据
      int id = rs.getInt(1);
      String name = rs.getString("name");
      System.out.println(id + "---" + name + "---" );
    }
    // 7、释放资源
    rs.close();
    statement.close();
    connection.close();
  }

  /**
   * 获取数据库的连接对象
   */
  private static Connection getConnection() throws Exception {
    //3.加载配置文件
    Properties pro = new Properties();
    InputStream is = JDBCTest.class.getClassLoader().getResourceAsStream("jdbc/druid.properties");
    pro.load(is);
    //4.获取连接池对象
    DataSource ds = DruidDataSourceFactory.createDataSource(pro);
    //5.获取连接
    Connection conn = ds.getConnection();
    return conn;
  }

}
