package com.ly.mybatis.typehandlers;

import com.ly.mybatis.bean.SexEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类型处理器（typeHandlers）：
 * 1、可以使用类型处理器为PreparedStatement设置参数；
 * 2、在得到结果集的时候，使用PreparedStatement将获取到的值转变成Java类型。
 */
public class StringEnumTypeHandler extends BaseTypeHandler<SexEnum> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, SexEnum parameter, JdbcType jdbcType)
    throws SQLException {
    ps.setString(i, parameter.getSex());
  }

  @Override
  public SexEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String val = rs.getString(columnName);
    return getEnum(val);
  }

  @Override
  public SexEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return getEnum(rs.getString(columnIndex));
  }

  @Override
  public SexEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return getEnum(cs.getNString(columnIndex));
  }

  /**
   * 根据值获得对应的枚举
   * @param val
   * @return
   */
  private SexEnum getEnum(String val){
    Class<SexEnum> sexClass = SexEnum.class;
    SexEnum[] sexs = sexClass.getEnumConstants();

    for(SexEnum se:sexs){
      if(se.getSex().equals(val)){
        return se;
      }
    }
    return null;
  }
}
