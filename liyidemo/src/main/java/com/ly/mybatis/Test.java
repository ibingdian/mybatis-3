package com.ly.mybatis;

import com.ly.mybatis.bean.Person;
import com.ly.mybatis.bean.SexEnum;
import com.ly.mybatis.mapper.BlogMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class Test {
  public static void main(String[] args) throws IOException {
    String resource = "mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    // 可以通过 SqlSession 实例来直接执行已映射的 SQL 语句。
    try (SqlSession session = sqlSessionFactory.openSession()) {
      // DefaultSqlSession
      BlogMapper mapper = session.getMapper(BlogMapper.class);
      Person person = mapper.list2(10,"ly");
      System.out.println(person);
//      Blog blog = mapper.selectBlog(101);

      Person person2 = session.selectOne("com.ly.mybatis.mapper.BlogMapper.list",1);

      System.out.println(person2);


      Person p = new Person();
      p.setName("ls");
      p.setSex(SexEnum.nan);
      int i = mapper.add(p);
      System.out.println(i);
      session.commit();
    }

  }
}
