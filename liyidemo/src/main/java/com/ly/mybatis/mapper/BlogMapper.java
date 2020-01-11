package com.ly.mybatis.mapper;

import com.ly.mybatis.bean.Person;

public interface BlogMapper {
  Person list(int id);
  Person list2(int id,String name);
  int add(Person person);
}
