package com.ly.mybatis.bean;

public class Person {
  private String name;
  private String id;
  private SexEnum sex;

  public SexEnum getSex() {
    return sex;
  }

  public void setSex(SexEnum sex) {
    this.sex = sex;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
