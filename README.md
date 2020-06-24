# Brick

一个轻量级的Java Web框架，实现功能：

- IOC
- MVC（异常处理和模板技术兼容未完善）
- ORM（未实现）
- 数据类类型转换和JSON转换等工具类
- 内嵌Tomcat容器，应用自启动



### 实际使用

使用Brick进行一个简单的查询测试（一个有id，username，password属性的对象）

@Bean：把当前类放入容器

@Controller：把当前类作为一个MVC控制器并放入容器

@Autowired：自动为当前字段注入Bean实例

@RequestMapping：绑定请求路径和当前方法

@RequestParam：请求参数绑定方法参数

@ResponseBody：以JSON的形式返回数据

Brick.run()：直接启动Web应用

#### Dao层

由于ORM功能模块还为实现，所以Dao层还不能用Brick实现太复杂的操作，可以用数据访问器做简单的数据库访问操作

```java
@Bean
public class UserDao {
    private DataAccessor dataAccessor = new DefaultDataAccessor();

    public User getUserByName(String username) {
        String sql = "select username, password from user where username = ?";
        return dataAccessor.query(User.class, sql, username);
    }

    public User getUserById(Integer id) {
        String sql = "select id, username, password from user where id = ?";
        return dataAccessor.query(User.class, sql, id);
    }
}
```



#### Service层

定义一个接口

```java
public interface UserService {
    User get(Integer id);

    boolean login(String username, String password);
}
```

实现接口

```java
@Bean
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public com.bricktest.entity.User get(Integer id) {
        User user = userDao.getUserById(id);
        return user;
    }

    @Override
    public boolean login(String username, String password) {
        User user = userDao.getUserByName(username);
        if (user == null) return false;
        if (password.equals(user.getPassword())) {
            return true;
        }
        return false;
    }
}
```



#### Controller层

实现一个控制器

```java
@Controller("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/get")
    @ResponseBody
    public User get(@RequestParam(value = "id") Integer id) {
        return userService.get(id);
    }

    @RequestMapping(path = "/login")
    @ResponseBody
    public String Login(@RequestParam(value = "username") String username,
                      @RequestParam(value = "password") String password) {
        if (userService.login(username, password)) {
            return "success";
        }
        return "fail";
    }
}
```



#### 启动应用

配置文件brick.properties（强制）

```properties
brick.base_package=com.bricktest

brick.datasource.driver=com.mysql.cj.jdbc.Driver
brick.datasource.url=jdbc:mysql:///brick_test?serverTimezone=UTC
brick.datasource.username=root
brick.datasource.password=199810

server.port=8080
```

启动类

```
public class App {
    public static void main(String[] args) {
        Brick.run();
    }
}
```



#### 访问测试

get操作

```http
http://localhost:8080/get?id=1
```

查询结果

```json
{"id":1,"username":"a","password":"123456"}
```



login操作

```http
http://localhost:8080/login?username=a&password=123456
```

结果

```json
"success"
```