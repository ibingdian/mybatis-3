MyBatis SQL Mapper Framework for Java
=====================================

[![Build Status](https://travis-ci.org/mybatis/mybatis-3.svg?branch=master)](https://travis-ci.org/mybatis/mybatis-3)
[![Coverage Status](https://coveralls.io/repos/mybatis/mybatis-3/badge.svg?branch=master&service=github)](https://coveralls.io/github/mybatis/mybatis-3?branch=master)
[![Maven central](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/oss.sonatype.org/org.mybatis/mybatis.svg)](https://oss.sonatype.org/content/repositories/snapshots/org/mybatis/mybatis/)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Stack Overflow](http://img.shields.io/:stack%20overflow-mybatis-brightgreen.svg)](http://stackoverflow.com/questions/tagged/mybatis)
[![Project Stats](https://www.openhub.net/p/mybatis/widgets/project_thin_badge.gif)](https://www.openhub.net/p/mybatis)

![mybatis](http://mybatis.github.io/images/mybatis-logo.png)

The MyBatis SQL mapper framework makes it easier to use a relational database with object-oriented applications.
MyBatis couples objects with stored procedures or SQL statements using a XML descriptor or annotations.
Simplicity is the biggest advantage of the MyBatis data mapper over object relational mapping tools.

Essentials
----------

* [See the docs](http://mybatis.github.io/mybatis-3)
* [Download Latest](https://github.com/mybatis/mybatis-3/releases)
* [Download Snapshot](https://oss.sonatype.org/content/repositories/snapshots/org/mybatis/mybatis/)








# 一、MyBatis 框架概述 

## 1、概述
mybatis是对JDBC的封装，可以加载驱动、创建连接、执行sql、将结果映射为 java 对象：

  * 有连接池，可以避免频繁的创建数据库的连接；
  * 可以在xml里配置sql语句，避免了sql语句的硬编码；
  * 可以把Java对象传递给PreparedStatement的占位符；
  * 也可以将结果集映射为Java对象；

连接池、硬编码

# 二、xml映射文件
# 1、参数
## 1、parameterType
用于指定传入参数的类型，可以是全限定类名或别名。

## 2、使用参数：#{}、${}
  * #{}：会使用? 替换掉，值传入预处理语句的参数中。
  * ${}：字符串替换

静态sql（#{}）：在解析配置文件的时候，翻译成？；执行sql的时候存器对？赋值；

动态sql（${}）：在解析的时候，不处理sql；在执行的时候才去处理（赋值）；

有一个$，就算是动态sql了，解析的时候，不做任何处理

![图片](https://uploader.shimo.im/f/DceCSqLys5MP2s5Z.png!thumbnail)

$ 可用在前面使用，表名、列名；

在前面写#，会翻译成？，不行，列名就不是列名了，就成字符串了

## 3、支持动态sql
避免了在个PreparedStatement的占位符赋值时的硬编码。

<if>标签、choose, when, otherwise

<where>标签

<trim>标签

<foreach>标签，用于遍历集合，

它的属性：  

collection:代表要遍历的集合元素，注意编写时不要写#{}  

open:代表语句的开始部分  

close:代表结束部分 

item:代表遍历集合的每个元素，生成的变量名  

sperator:代表分隔符

 

```
<select id="getList" resultMap="BaseResultMap" parameterType="java.util.Map">
    select
    <include refid="Base_Column_List"/>
    from conflict_clue
    <where>
        <trim prefixOverrides="and">
            <if test="id != null">
                and id = #{id}
            </if>
            <if test="conflictIds != null">
                and id in
                <foreach collection="conflictIds" item="item" open="(" separator="," close=")">
                    #{item}
                </foreach>
            </if>
        </trim>
    </where>
</select
```
# 2、结果映射
## 1、resultType 
指定结果集的类型。 它支持基本类型和引用类型。 如果列名属性名一样，可以使用这个。

## 2、resultMap
如果列名属性名不一样，可以建立列名和Java属性名之间的映射关系。

例子，如下：

```
<resultMap type="com.itheima.domain.User" id="userMap">  
  <id column="id" property="userId"/>
  <result column="username" property="userName"/>
  <result column="sex" property="userSex"/>
  <result column="address" property="userAddress"/>
  <result column="birthday" property="userBirthday"/>
 </resultMap> 
 
<select id="findAll" resultMap="userMap">  
    select * from user 
</select> 
```
其中，id 、result：将一个列的值映射到一个简单数据类型（String, int, double, Date 等）；
id 是对象的标识属性，这会在比较对象实例时用到。 这样可以提高整体的性能，尤其是进行缓存和嵌套结果映射（也就是连接映射）的时候。

 id 元素在嵌套结果映射中扮演着非常重要的角色。你应该总是指定一个或多个可以唯一标识结果的属性。 虽然，即使不指定这个属性，MyBatis 仍然可以工作，但是会产生严重的性能问题。 只需要指定可以唯一标识结果的最少属性。显然，你可以选择主键（复合主键也可以）。

### 1、对象的关系
在项目中，某些实体类之间肯定有关键关系，比如一对一，一对多等。

  * association: 一对一关联(has one)
  * collection:一对多关联(has many)
### 2、一对一对象查询的方式
关联对象查询，有两种实现方式：有联合查询、嵌套查询。association、collection一样。

**1、联合查询**

```
<resultMap id="blogResult" type="Blog">
  <id property="id" column="blog_id" />
  <result property="title" column="blog_title"/>
  <association property="author" column="blog_author_id" javaType="Author" resultMap="authorResult"/>
</resultMap>

<resultMap id="authorResult" type="Author">
  <id property="id" column="author_id"/>
  <result property="username" column="author_username"/>
  <result property="password" column="author_password"/>
  <result property="email" column="author_email"/>
  <result property="bio" column="author_bio"/>
</resultMap>
<select id="selectBlog" resultMap="blogResult">
  select
    B.id            as blog_id,
    B.title         as blog_title,
    B.author_id     as blog_author_id,
    A.id            as author_id,
    A.username      as author_username,
    A.password      as author_password,
    A.email         as author_email,
    A.bio           as author_bio
  from Blog B left outer join Author A on B.author_id = A.id
  where B.id = #{id}
</select
```
**2、嵌套查询**

通过执行另外一个 SQL语句来加载关联的对象；

先查一个表,根据这个表里面 的结果，去另外一个表里面查询数据

```
<resultMap id="blogResult" type="Blog">
  <association property="author" column="author_id" javaType="Author" select="selectAuthor"/>
</resultMap>

<select id="selectBlog" resultMap="blogResult">
  SELECT * FROM BLOG WHERE ID = #{id}
</select>

<select id="selectAuthor" resultType="Author">
  SELECT * FROM AUTHOR WHERE ID = #{id}
</selec
```
这种方式虽然很简单，但在大型数据集或大型数据表上表现不佳。这个问题被称为“N+1 查询问题”。 概括地讲，N+1 查询问题是这样子的：
* 你执行了一个单独的 SQL 语句来获取结果的一个列表（就是“+1”）。
* 对列表返回的每条记录，你执行一个 select 查询语句来为每条记录加载详细信息（就是“N”）。

**3、使用存储过程**

### 3、一对多对象的查询方式
实现方式：联合查询、嵌套查询、存储过程。同上。

```
<resultMap id="blogResult" type="Blog">
  <id property="id" column="blog_id" />
  <result property="title" column="blog_title"/>
  <collection property="posts" ofType="Post">
    <id property="id" column="post_id"/>
    <result property="subject" column="post_subject"/>
    <result property="body" column="post_body"/>
  </collection>
</resultMap>
```
ofType：指定集合里面JavaBean的类型
## 3、延迟加载
嵌套查询，会导致一个查询，会执行1+N次sql语句。

MyBatis 能够对这样的查询进行延迟加载，就是使用的时候才去加载sql。

1）Mybatis 仅支持 association 关联对象和 collection 关联集合对象的延迟加载。在 Mybatis 配置文件中，可以配置是否 启用延迟加载 lazyLoadingEnabled=true|false。 

2）它的原理是，使用 CGLIB 创建目标对象的代理对象，当调用目标方法时，进入拦截器方 法，比如调用 a.getB().getName()，拦截器 invoke()方法发现 a.getB()是 null 值，那么就会单 独发送事先保存好的查询关联 B 对象的 sql，把 B 查询上来，然后调用 a.setB(b)，于是 a 的 对象 b 属性就有值了，接着完成 a.getB().getName()方法的调用。这就是延迟加载的基本原理。

# 3、返回自增id
## 1、支持主键自增
可以设置 useGeneratedKeys=”true”

MyBatis 使用 JDBC 的 getGeneratedKeys 方法来取出由数据库内部生成的主键。

```
<insert id="insertAuthor" useGeneratedKeys="true" keyProperty="id">
  insert into Author (username,password,email,bio)
  values (#{username},#{password},#{email},#{bio})
</insert>
```
## 2、不支持自增
使用selectKey，由MyBatis生成主键。

```
<insert id="insertAuthor">
  <selectKey keyProperty="id" resultType="int" order="BEFORE">
    select CAST(RANDOM()*1000000 as INTEGER) a from SYSIBM.SYSDUMMY1
  </selectKey>
  insert into Author
    (id, username, password, email,bio, favourite_section)
  values
    (#{id}, #{username}, #{password}, #{email}, #{bio}, #{favouriteSection,jdbcType=VARCHAR})
</insert>
```
selectKey 元素中的语句将会首先运行，Author 的 id 会被设置，然后插入语句会被调用。

# 4、缓存
通过缓存策略来减少数据库的查询次数，从而提 高性能。 

## 1、一级缓存
基于SqlSession的，它仅仅对一个会话中的数据进行缓存。 默认开启的。只要 SqlSession 没有 commit或 close，它就存在。 

## 2、二级缓存
基于命名空间的，默认没有启用。

多个 SqlSession 去操作同一个 Mapper 映射的 sql 语句，多个 SqlSession 可以共用二级缓存，二级缓存是跨 SqlSession 的。 

## 3、为什么Spring中mybatis的一级缓存会失效？
在整合mybatis的时候，mybatis提供l一个mybatis-spring的插件包，在这个里面、spring中并没有直接使用mybatis的DefaultSqlSession，而是自己实现了SqlSessionTemplate，并且加了一层代理，在执行sql语句的时候，会通过代理调DefaultSqlSession，但是在代理里，这一条sql语句执行完之后，把session给关了。

增强了DefaultSqlSession。

mybatis-spring提供了一个扩展类SqlSessionTemplate，这个类在Spring容器启动的时候被注入进来，这个类替代类原理的DefaultSqlSession。它当中的所有查询方法不是直接查询，而是经过一个代理方法，增强类查询方法。主要是关闭session。

没办法解决。

为什么要关了？

难道用户关


![图片](https://uploader.shimo.im/f/DEwNldpezG8zlPLA.png!thumbnail)




# 4、事务
session.commit(); 

以 Mybatis 框架的事务控制方式，本身也是用 JDBC的 setAutoCommit()方法来设置事务提交方式的。

session默认是不提交的，需要手动提交

![图片](https://uploader.shimo.im/f/RDZyFRXunes0ZGXR.png!thumbnail)

![图片](https://uploader.shimo.im/f/JnG33yFhE2UxnjmL.png!thumbnail)


# 三、原理
# 1、初始化
## 1、获取SqlSessionFactory
```
SqlSessionFactory sqlSessionFactory = new 
                      SqlSessionFactoryBuilder().build(inputStream);
```
主要是将配置文件解析成一个Configuration对象，包括了：

  *  创建连接池DataSource   
  * 解析mapper文件里的sql语句，并把结果封装到MappedStatement里面
  * 创建mapper接口的MapperProxyFactory对象：创建mapper代理对象

最终返回一个DefaultSqlSessionFactory对象。![图片](https://uploader.shimo.im/f/StQaINac3AAn9tXo.png!thumbnail)

### 1、mybatis解析mapper文件几种方式？
* mapper：直接指定用哪个mapper。
  * 加载mapper的xml文件
  * 类型
    * resource：类路径，相对路径
    * url：绝对路径
    * class：接口
* package：指定mapper在哪个包下
  * *会扫描指定包下的mapper接口；*
  * *加载相同路径的xml资源；*
  * *解析mapper接口方法的注解，比如@Select…………*

![图片](https://uploader.shimo.im/f/Py7DtXkeONMihVEG.png!thumbnail)


### 2、如果xml、注解里面都配置sql了，会怎么样？
如果它们的id一样，会报错：

* 不管是通过解析xml还是注解，都会创建一个MappedStatement对象；
* 这个MappedStatement对象的id就是namespace+id；
* 这个MappedStatement对象会保存在Configuration的mappedStatements里面；
* mappedStatements继承自HashMap，如果id一样会报错；

![图片](https://uploader.shimo.im/f/eq9FBGxrwUgswVPY.png!thumbnail)

![图片](https://uploader.shimo.im/f/yKuG4Bx2wEU3nHfl.png!thumbnail)




## 2、获取SqlSession
```
SqlSession session = sqlSessionFactory.openSession()；
```
创建Executor

  * 先创建SimpleExecutor，之后会拿CachingExecutor和我们定义的插件Interceptor做一下包装。

最终创建一个DefaultSqlSession。

SimpleExecutor ——> CachingExecutor ——> Interceptor

![图片](https://uploader.shimo.im/f/BdS8vPJVBJoBNoLp.png!thumbnail)

![图片](https://uploader.shimo.im/f/Ibzk4JuRwn8UWQ7R.png!thumbnail)


# 2、创建mapper接口的代理对象
```
BlogMapper mapper = session.getMapper(BlogMapper.class);
```
使用MapperProxyFactory，可以通过反射创建mapper接口的代理对象；
![图片](https://uploader.shimo.im/f/d1in1xsYetQSbIqQ.png!thumbnail)

# 3、执行sql
```
Person person = mapper.list(10);
```
mapper是一个代理对象，在调用这个对象的过程中：
## 1、映射<参数名字：参数值>
我们需要从方法里获取参数的名字，并和参数的值成对的保存起来。

这个参数的名字，是我们在配mapper的xml文件里，写sql语句时要用到的。

在下面为动态sql赋值、为PreparedStatement赋值的时候，这些数值对会被用到。

顺序也是一种映射，定义方法和调用方法时的参数是按照顺序做映射的：

比如，方法里第一个、第二个参数名字是arg0、arg1，调用方法时第一、第二个参数是10、张三，则可用得到一个map：{arg0：10，arg1：张三}

![图片](https://uploader.shimo.im/f/BCfsL0MbcQknaB7H.png!thumbnail)

有三种方式设置参数的名字：

  * 1、使用param1、param2……这种形式，如上；
  * 2、使用注解@Param；
  * 3、使用反射获得方法上参数的名字
    * 在jdk8之前，是arg0、arg1……；
    * 之后，可用通过插件，保存方法的参数名字，可用获取到自己写的方法的名字


![图片](https://uploader.shimo.im/f/t3dtERrU02QiGPF5.png!thumbnail)

param1、param2 提供一种机制，不管用户写什么，都能拿到，一定有个参数可用

![图片](https://uploader.shimo.im/f/CvUxQSZQlnYrBNjM.png!thumbnail)

Java8之前通过反射只能拿到arg0、arg1

![图片](https://uploader.shimo.im/f/GysHGspEsLUkOHJB.png!thumbnail)

![图片](https://uploader.shimo.im/f/0LbNNVYTJXkAvYmh.png!thumbnail)

Java8之后，可以通过 名字直接找到了

![图片](https://uploader.shimo.im/f/68uaYyCWTwIeVZ1x.png!thumbnail)

![图片](https://uploader.shimo.im/f/V3greQ1hIJUaZeKk.png!thumbnail)

![图片](https://uploader.shimo.im/f/YFqKOq7naScOCezA.png!thumbnail)


## 2、调用SqlSession#select；
![图片](https://uploader.shimo.im/f/5Z7BUljnfRMAeSRw.png!thumbnail)


**1、根据sql的标识，拿到MappedStatement；**

**2、执行****executor**** （MappedStatement）；**

![图片](https://uploader.shimo.im/f/lJ9m1yvfHXsLIRbd.png!thumbnail)

通过executor执行sql，在调用executor的过程中，这一条链上的executor都会执行，SimpleExecutor ——> CachingExecutor ——> Interceptor。

## 3、执行Executor 
### 1、为动态sql ${} 赋值 ？？
为动态sql ${} 赋值，把#变成？（要通过jdbc的PreparedStatement的赋值）




？？？？？？

静态sql（#{}）：在解析配置文件的时候，翻译成？；执行sql的时候存器对？赋值；

动态sql（${}）：在解析的时候，不处理sql；在执行的时候才去处理（赋值）；

有一个$，就算是动态sql了，解析的时候，不做任何处理

![图片](https://uploader.shimo.im/f/DceCSqLys5MP2s5Z.png!thumbnail)

$ 可用在前面使用，表名、列名；

在前面写#，会翻译成？，不行，列名就不是列名了，就成字符串了

### 2、通过Connection获取PreparedStatement对象；
    * 并使用TypeHandler为PreparedStatement赋值；
### 3、执行PreparedStatement对象；
### 4、将结果集映射为Java对象；
？？？

![图片](https://uploader.shimo.im/f/OEetFUoT1UQGmo8y.png!thumbnail)

![图片](https://uploader.shimo.im/f/h86aV9yAwLomG5sF.png!thumbnail)



执行器有几种？

简单的、重复的、批量的

![图片](https://uploader.shimo.im/f/NkHFwUXckTYvGfxZ.png!thumbnail)

![图片](https://uploader.shimo.im/f/FKkQUjqRCLIfQFid.png!thumbnail)





mybatis的一级缓存默认是开启的




# 三、几个问题
### 1、ResultMap和ResultType在使用中的区别
ResultType：要求数据库的字段要和pojo对象字段一样；

ResultMap：可以进行pojo和相应表字段的对应。

### 2、当实体类中的属性名和表中的字段名不一样，如果将查询的结果封装到指定 pojo？ 
1）通过在查询的 sql 语句中定义字段名的别名。   

2）通过<resultMap>来映射字段名和实体类属性名的一一对应的关系。 

### 3、Mybatis 是如何将 sql 执行结果封装为目标对象并返回的？都有哪些映射形式？
第一种是使用<resultMap>标签，逐一定义列名和对象属性名之间的映射关系。 

第二种是使用 sql 列的别名功能，将列别名书写为对象属性名，比如 T_NAME AS NAME。

有了列名与属性名的映射关系后，

Mybatis 通过反射创建对象，同时使用反射给对象的属性 逐一赋值并返回，那些找不到映射关系的属性，是无法完成赋值的。 

### 4、在 mapper 中如何传递多个参数？ 
1）直接在方法中传递参数，xml 文件用#{0} #{1}来获取 

 2）使用 @param 注解，这样可以直接在 xml 文件中通过#{name}来获取 

### 5、[Mybatis 执行批量插入，能返回数据库主键列表吗？ ](https://blog.csdn.net/yjclsx/article/details/84318397)
在mysql数据库中支持批量插入，只要配置useGeneratedKeys和keyProperty就可以批量插入并返回主键了。

```
<insert id="insertList" parameterType="java.util.List">
    <selectKey resultType="Integer" order="AFTER" keyProperty="id">
        SELECT LAST_INSERT_ID() AS id
    </selectKey>
    insert into operation_dialog_result (op_type,status)
    values
    <foreach collection="list" item="item" open="(" close=")" separator=",">
        #{item.opType,jdbcType=INTEGER}, 
        #{item.result,jdbcType=VARCHAR},
        #{item.status,jdbcType=INTEGER}}
    </foreach>
</insert>
<insert id="batchInsertCameras" useGeneratedKeys="true" keyProperty="cameraNo">
    insert into camera (chanIndex,cameraName)
    values
    <foreach collection="list" item="c" separator=",">
        (#{c.chanIndex},#{c.cameraName})
    </foreach>
</insert>
```

### 6、模糊查询 like 语句该怎么写 ？
1）在 java 中拼接通配符，通过#{}赋值 

```
 LIKE CONCAT('%',#{name},'%')
```
2）在 Sql 语句中拼接通配符 （不安全 会引起 Sql 注入） 
```
like '%${name}%'
```

### 7、Xml 映射文件中，除了常见的 select|insert|updae|delete 标签之外，还有哪些标签？ 
还有很多其他的标签，<resultMap>、<parameterMap>、<sql>、<include>、 <selectKey>，加上动态 sql 的 9 个标签， trim|where|set|foreach|if|choose|when|otherwise|bind 等，其中<sql>为 sql 片段标签，通 过<include>标签引入 sql 片段，<selectKey>为不支持自增的主键生成策略标签。

### 8、Mybatis 动态 sql 
Mybatis 动态 sql 是做什么的？都有哪些动态 sql？能简述一下动态 sql 的执行原理不？ 

1）Mybatis 动态 sql 可以让我们在 Xml 映射文件内，以标签的形式编写动态 sql，完成逻辑 判断和动态拼接 sql 的功能。 

2）Mybatis 提供了 9 种动态 sql 标签： trim|where|set|foreach|if|choose|when|otherwise|bind。 通过 OGNL 语法来实现。

3）其执行原理为，使用 OGNL 从 sql 参数对象中计算表达式的值，根据表达式的值动态拼接 sql，以此来完成动态 sql 的功能。

trim 节点是用来判断如果动态语句是以 and 或 or 开始,那么会自动把这个 and 或者 or 取掉。 

### 9、#{}和${}的区别是什么？ 
1）#{}是预编译处理，${}是字符串替换。 

2）Mybatis 在处理#{}时，会将 sql 中的#{}替换为?号，调用 PreparedStatement 的 set 方法 来赋值； 

3）Mybatis 在处理${}时，就是把${}替换成变量的值。 

4）使用#{}可以有效的防止 SQL 注入，提高系统安全性。 

### 10、为什么说 Mybatis 是半自动 ORM 映射工具？
在查询时，需要手动编写 sql 来完成

### 11、什么是 MyBatis 的接口绑定,有什么好处？ 
接口映射就是在 MyBatis 中任意定义接口,然后把接口里面的方法和 SQL 语句绑定,我们 直接调用接口方法就可以,这样比起原来了 SqlSession 提供的方法我们可以有更加灵活的选 择和设置. 

### 12、接口绑定有几种实现方式? 
接口绑定有两种实现方式,一种是通过注解绑定,就是在接口的方法上面加上 @Select@Update 等注解里面包含 Sql 语句来绑定,另外一种就是通过 xml 里面写 SQL 来绑 定,在这种情况下,要指定 xml 映射文件里面的 namespace 必须为接口的全路径名.

### 13、什么情况下用注解绑定,什么情况下用 xml 绑定？
当 Sql 语句比较简单时候,用注解绑定；当 SQL 语句比较复杂时候,用 xml 绑定,一般用 xml 绑定的比较多 

### 14、通常一个 Xml 映射文件，都会写一个 Dao 接口与之对应, Dao 的工作原理，是否可以重载？ 
不能重载，因为通过 Dao 寻找 Xml 对应的 sql 的时候全限名+方法名的保存和寻找策 略。接口工作原理为 jdk 动态代理原理，运行时会为 dao 生成 proxy，代理对象会拦截接口方法，去执行对应的 sql 返回数据。 

### 15、Mybatis 映射文件中，如果 A 标签通过 include 引用了 B 标签的内容，请问，B 标签能 否定义在 A 标签的后面，还是说必须定义在 A 标签的前面？ 
虽然 Mybatis 解析 Xml 映射文件是按照顺序解析的，但是，被引用的 B 标签依然可以 定义在任何地方，Mybatis 都可以正确识别。原理是，Mybatis 解析 A 标签，发现 A 标签引 用了 B 标签，但是 B 标签尚未解析到，尚不存在，此时，Mybatis 会将 A 标签标记为未解 析状态，然后继续解析余下的标签，包含 B 标签，待所有标签解析完毕，Mybatis 会重新 解析那些被标记为未解析的标签，此时再解析 A 标签时，B 标签已经存在，A 标签也就可 以正常解析完成了。

### 16、Mybatis 的 Xml 映射文件中，不同的 Xml 映射文件，id 是否可以重复？ 
不同的 Xml 映射文件，如果配置了 namespace，那么 id 可以重复；如果没有配置 namespace，那么 id 不能重复；毕竟 namespace 不是必须的，只是最佳实践而已。原因就 是 namespace+id 是作为 Map<String, MappedStatement>的 key 使用的，如果没有 namespace，就剩下 id，那么，id 重复会导致数据互相覆盖。有了 namespace，自然 id 就 可以重复，namespace 不同，namespace+id 自然也就不同。 



### 20、简述 Mybatis 的插件运行原理，以及如何编写一个插件？ 
1）Mybatis 仅可以编写针对 ParameterHandler、ResultSetHandler、StatementHandler、 Executor 这 4 种接口的插件，Mybatis 通过动态代理，为需要拦截的接口生成代理对象以实现接口方法拦截功能，每当执行这 4 种接口对象的方法时，就会进入拦截方法，具体就是 InvocationHandler 的 invoke()方法，当然，只会拦截那些你指定需要拦截的方法。 

2）实现 Mybatis 的 Interceptor 接口并复写 intercept()方法，然后在给插件编写注解，指定 要拦截哪一个接口的哪些方法即可，记住，别忘了在配置文件中配置你编写的插件。 


### 21、Mybatis 是如何进行分页的？分页插件的原理是什么？ 
1）Mybatis 使用 RowBounds 对象进行分页，也可以直接编写 sql 实现分页，也可以使用 Mybatis 的分页插件。 

2）分页插件的原理：实现 Mybatis 提供的接口，实现自定义插件，在插件的拦截方法内拦截待执行的 sql，然后重写 sql。 

举例：select * from student，拦截 sql 后重写为：select t.* from （select * from student）t limit 0，10 



### 22、Mybatis的执行器？ 
使用 BatchExecutor 完成批处理。 

Mybatis 有三种基本的 Executor 执行器，SimpleExecutor、ReuseExecutor、 BatchExecutor。

1）SimpleExecutor：每执行一次 update 或 select，就开启一个 Statement 对 象，用完立刻关闭 Statement 对象。

2）ReuseExecutor：执行 update 或 select，以 sql 作为 key 查找 Statement 对象，存在就使用，不存在就创建，用完后，不关闭 Statement 对象， 而是放置于 Map

3）BatchExecutor：完成批处理。 

Mybatis 中如何指定使用哪一种 Executor 执行器？ 

在 Mybatis 配置文件中，可以指定默认的 ExecutorType 执行器类型，也可以手动给 DefaultSqlSessionFactory 的创建 SqlSession 的方法传递 ExecutorType 类型参数。 


简述 Mybatis 的 Xml 映射文件和 Mybatis 内部数据结构之间的映射关系？ 

Mybatis 将所有 Xml 配置信息都封装到 All-In-One 重量级对象 Configuration 内部。在 Xml 映射文件中，<parameterMap>标签会被解析为 ParameterMap 对象，其每个子元素会 被解析为 ParameterMapping 对象。<resultMap>标签会被解析为 ResultMap 对象，其每个子 元素会被解析为 ResultMapping 对象。每一个<select>、<insert>、<update>、<delete>标签 均会被解析为 MappedStatement 对象，标签内的 sql 会被解析为 BoundSql 对象。


Mybatis 是否可以映射 Enum 枚举类？ 答：Mybatis 可以映射枚举类，不单可以映射枚举类，Mybatis 可以映射任何对象到表的一 列上。映射方式为自定义一个 TypeHandler，实现 TypeHandler 的 setParameter()和 getResult()接口方法。TypeHandler 有两个作用，一是完成从 javaType 至 jdbcType 的转换， 二是完成 jdbcType 至 javaType 的转换，体现为 setParameter()和 getResult()两个方法，分别 代表设置 sql 问号占位符参数和获取列查询结果。 

 

以重复，namespace 不同，namespace+id 自然也就不同。 

# 三、mybatis-spring概述
## 1、spring-mybatis的初始化步奏
目标是得到一个mapper代理对象

1、在spring中mybatis的初始化开始于@MapperScan，通过这个注解，会扫描指定包下的接口，并得到对应接口的MapperFactoryBean对象；

2、在MapperFactoryBean对象创建完之后，会调用其afterPropertiesSet方法，会得到MapperProxyFactory对象，通过这个对象可以创建mapper的代理对象；同时解析注解或者xml封装成statement；

3、在每一个获取mapper对象的时候，执行的都是MapperFactoryBean#getObject方法，最终会得到一个代理对象

### 1、创建MapperFactoryBean
@MapperScan会通过@Import注解引入一个ImportBeanDefinitionRegistrar类，在这个类里面会扫描指定包下的接口，得到每个mapper的BD，并替换bd中实现类的类名为MapperFactoryBean，这个类里有个属性就是这个mapper接口。

![图片](https://uploader.shimo.im/f/MRHX3aVmGDQqUg3d.png!thumbnail)

### 2、创建MapperProxyFactory
在MapperFactoryBean对象创建完之后，会调用其afterPropertiesSet方法，会得到MapperProxyFactory对象，通过这个对象可以创建mapper的代理对象；同时也会mybatis里的方法：把注解或者xml里面配置的sql语句封装成一个statement；

![图片](https://uploader.shimo.im/f/QjGgpeC9keAsdQOq.png!thumbnail)

![图片](https://uploader.shimo.im/f/hHlkA1udDl4tSh9k.png!thumbnail)

### 3、创建代理对象
在每一个获取mapper对象的时候，执行的都是MapperFactoryBean#getObject方法，最终会得到一个代理对象（通过jdk动态代理），通过这个代理对象，可以执行sql语句。

![图片](https://uploader.shimo.im/f/ToUAbIMaIBQn4jRP.png!thumbnail)



MapperFactoryBean、MapperProxyFactory它们的类型都是Mapper接口类型。



## 2、mapper的方法，如何执行sql？
>在调用dao.list()方法时做了什么？

由上面可知，在初始化的时候，为每个mapper生成了一个代理对象，在调用mapper的方法的时候，实际上最终调用了这个代理对象，在代理对象里，最终调用sqlSession的方法，执行sql语句。

在调用SqlSession时，先通过一个SqlSession的代理，最终调了SqlSession真实对象。

![图片](https://uploader.shimo.im/f/oY8c78gj0iY8cYta.png!thumbnail)


最终是通过SqlSession的Executor去执行MappedStatement。



Mapper执行的过程是通过Executor、StatementHandler、ParameterHandler和ResultHandler来完成数据库操作和结果返回的，理解他们是编写插件的关键：

Executor：执行器，由它统一调度其他三个对象来执行对应的SQL；

StatementHandler：使用数据库的Statement执行操作；

ParameterHandler：用于SQL对参数的处理；

ResultHandler：进行最后数据集的封装返回处理；

在MyBatis中存在三种执行器：

SIMPLE：简易执行器，默认的执行器；

REUSE：执行重用预处理语句；

BATCH:执行重用语句和批量更新，针对批量专用的执行器；



reuse













**DefaultSqlSession是怎么被替换调的？**

AUTOWIRE_NO ：默认不自动装配，@autowire注解调仍然会自动装配。

AUTOWIRE_BY_NAME：根据名字自动装配。通过set方法。

AUTOWIRE_BY_TYPE：根据类型自动装配。通过set方法。

mapper接口所代表调对象都是通过这个对象返回的：MapperFactoryBean。

mapper是一个代理，handler是MapperProxy。

自动装配的时候，如果是class类型，直接忽略。

解耦spring。


![图片](https://uploader.shimo.im/f/9bKz9Bx6T10sRG2t.png!thumbnail)

![图片](https://uploader.shimo.im/f/by1lhcPrbsQLP2tm.png!thumbnail)


通过MapperScan扫描包。

两个代理对象：

mapper的代理对象，handler是MapperProxy，注入SqlSessionTemplate对象。

SqlSessionTemplate有有个代理，把一级缓存关闭了。


MapperMethod ，跟 bd差不多

 sql语句什么时候被放进去的？

在mapper被实例化的过程中，afterPropertiesSet中，sql语句会被拿出来，还有id等等，放到一个MappedStatement对象里。当调用某个方法的时候，就根据id把sql语句拿出来执行了。




SqlSessionFactoryBean










# 

---
# 

---
# 

---
# 

---
# 





mybatis解析mapper有几种方法？

url、resource、class、package

mybatis的日志![图片](https://uploader.shimo.im/f/dGPuKFoQRpwak4a2.png!thumbnail)




注意：

1、#{}

>>#{}
>它代表占位符，相当于原来 jdbc 部分所学的?，都是用于执行语句时替换实际的数据。    具体的数据是由#{}里面的内容决定的。 它用的是 ognl 表达式。 
>>ognl 表达式
>它是 apache 提供的一种表达式语言，全称是：   Object Graphic Navigation Language  对象图导航语言   它是按照一定的语法格式来获取数据的。   语法格式就是使用  #{对象.对象}的方式 。

2、新增用户 id 的返回值 

 新增用户后，同时还要返回当前新增用户的 id 值，因为 id 是由数据库的自动增长来实现的，所以就相 当于我们要在新增后将自动增长 auto_increment 的值返回。 

```
<insert id="saveUser" parameterType="USER"> 
<!-- 配置保存时获取插入的 id --> 
<selectKey keyColumn="id" keyProperty="id" resultType="int"> 
 select last_insert_id(); 
</selectKey> 
insert into user(username,birthday,sex,address) 
 values(#{username},#{birthday},#{sex},#{address})
</insert> 
```







参考：

[Mybatis第一天讲义.pdf](https://uploader.shimo.im/f/qpYRwsUm1zs2nVZS.pdf)

[Mybatis第四天讲义.pdf](https://uploader.shimo.im/f/loHLHXsfyr4QZLYT.pdf)






mybatis是一个基于 java 的持久层框架，它内部封装了 jdbc，使开发者只需要关注 sql语句本身， 而不需要花费精力去处理加载驱动、创建连接、创建 statement 等繁杂的过程。 mybatis通过xml 或注解的方式将要执行的各种statement配置起来，并通过java对象和statement 中 sql 的动态参数进行映射生成最终执行的 sql 语句，最后由 mybatis 框架执行 sql 并将结果映射为 java 对象并返回。 采用 ORM 思想解决了实体和数据库映射的问题，对 jdbc进行了封装，屏蔽了 jdbc api 底层访问细节，使我们不用与 jdbc api 打交道，就可以完成对数据库的持久化操作。

在mybatis容器初始化的时候，会自动进行驱动注册，并把xml中配置的sql语句按照命名空间（就是接口名）加sql ID的方式作为key，sql语句作为value放入hashMap中存储起来，等到使用的时候从hashmap中取出，经过反射处理得到原生的sql语句，在使用jdbc executor进行执行！得到数据操作结果以后，使用resultmap中的映射关系把数据映射到JAVA实体类中，并创建相应的实例对象！



二、使用

1、 Mybatis 的参数

 1、parameterType 属性： 

用于指定传入参数的类型。

该属性的取值可以是基本类型、引用类型、实体类的包装类。

基本类型、String类型， 我们可以直接写类型名称，也可以使用全限定类名的方式 ，mybaits 在加载时已经把常用的数据类型注册了别名，从而我们在使用时可以不写包名， 而我们的是实体类并没有注册别名，所以必须写全限定类名。

例如 ： java.lang.String。  

 2、resultType 属性：

用于指定结果集的类型。 它支持基本类型和实体类类型。 

它和 parameterType 一样，如果注册过类型别名的，可以直接使用别名。没有注册过的必须 使用全限定类名。

3、resultMap 结果类型 

resultMap 标签可以建立查询的列名和实体类的属性名称不一致时建立对应关系。从而实现封装。 在 select 标签中使用 resultMap 属性指定引用即可。同时 resultMap 可以实现将查询结果映射为复杂类 型的 pojo，比如在查询结果映射对象中包括 pojo 和 list 实现一对一查询和一对多查询。 

```
<resultMap type="com.itheima.domain.User" id="userMap">  
  <id column="id" property="userId"/>
  <result column="username" property="userName"/>
  <result column="sex" property="userSex"/>
  <result column="address" property="userAddress"/>
  <result column="birthday" property="userBirthday"/>
 </resultMap> 
 
<select id="findAll" resultMap="userMap">  
    select * from user 
</select> 
```
type 属性：指定实体类的全限定类名  
id 属性：给定一个唯一标识，是给查询 select 标签引用用的

id 标签：用于指定主键字段 

result 标签：用于指定非主键字段 

column 属性：用于指定数据库列名

property 属性：用于指定实体类属性名称 



3、[多表查询](https://www.jianshu.com/p/018c0f083501)




