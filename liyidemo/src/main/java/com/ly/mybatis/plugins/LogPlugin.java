package com.ly.mybatis.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

/**
 * 可以在sql语句执行的过程中拦截。
 *
 * 默认情况下，MyBatis 允许使用插件来拦截的方法调用包括：
 *
 * Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
 * ParameterHandler (getParameterObject, setParameters)
 * ResultSetHandler (handleResultSets, handleOutputParameters)
 * StatementHandler (prepare, parameterize, batch, update, query)
 *
 */
@Intercepts({@Signature(
  type= Executor.class,
  method = "update",
  args = {MappedStatement.class,Object.class})})
public class LogPlugin implements Interceptor {
  public Object intercept(Invocation invocation) throws Throwable {
    // implement pre processing if need
    Object returnObject = invocation.proceed();
    // implement post processing if need
    System.out.println("------ly---Interceptor----");
    return returnObject;
  }
}
