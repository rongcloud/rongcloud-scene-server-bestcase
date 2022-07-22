## 快速使用

请提前准备好 RongRTC-Server 所依赖的JDK（1.8版本）、 Mysql（>=5.7）、Redis (>=3.3.0) 等基础服务。

### 创建数据库并执行sql
sql 在resources/sql目录下

### 服务配置

服务所用到的配置信息统一在 application.yml 文件中管理维护。
通过spring.profiles.active 来配置prod、test环境

* 融云 IM 服务配置
  
请前往 [融云官网](https://www.rongcloud.cn/) 注册、申请 AppKey 和 Secret, 并替换 application.yml 文件中的`融云 IM 配置`，配置如下:

```
im:
  appKey: kkkkkk
  secret: ssssss
  host: http://api-cn.ronghub.com
```

* 融云 SMS 服务配置

RongRTC-Server 登录默认需短信验证，登录是否需短信验证可通过开关配置，配置项在 application.yml 文件中, 配置如下:

```
rongrtc:
  login:
    sms_verify: false 
```

如果您不需要登录短信验证，请直接跳转下一步操作。如您需要，请前往 [融云官网](https://www.rongcloud.cn/product/sms) 开通短信服务，并注册登录验证短信模板 Id，然后将 AppKey、Secret 和 TemplateId 替换 application.yml 文件中的`融云 SMS 配置`，配置如下:

```
sms:
  appKey: kkkkkk
  secret: ssssss
  host: http://api.sms.ronghub.com
  templateId: xxxxxxxx
```
短信模板示例:

```
【融云全球通信云】您的短信验证码是 xxxx，请在 15 分钟内输入使用。超时请重新申请。
```

* 数据库连接配置

请您将实际数据库连接配置替换 application.yml 文件中的 `数据库连接配置`，配置如下：

```
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:4306/您的数据库?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
    username: root
    password: root (您的密码)
    driver-class-name: com.mysql.cj.jdbc.Driver
```

* Redis 连接配置

请您将实际 Redis 连接配置替换 application.yml 文件中的 `Redis 连接配置`，配置如下：

```
redis:
  database： 0
  host: 127.0.0.1
  port: 6379
  pass: 
  maxIdle: 100
  maxTotal: 300
  testOnBorrow: true
```

* RongRTC-Server 服务访问地址配置

请将 RongRTC-Server 服务访问地址配置替换 application.yml 文件中的 `服务访问地址配置`，配置如下：

```
rongrtc:
  domain: http://120.92.13.89 # RongRTC Server 地址，协议://域名:端口
```

假设服务启动端口为 8080，则该地址需要配: http://127.0.0.1:8080

### 数据库初始化

执行如下命令，创建数据库:

```
create database rongrtc;
```

### 服务打包运行

通过 mvn package 编译出 jar 或者 IntelliJ IDE 直接运行工程

*  java -jar -Dspring.profiles.active=prod RongRTC-Server-1.0.0-SNAPSHOT.jar 启动服务
*  默认启用 8080 端口，默认 http 请求

### 其他配置

1、IM 聊天室状态同步设置

> IM 聊天室发生状态变化时，需将状态实时同步到 RongRTC-Server 的应用服务器，我们需要在 IM 开发者后台配置 RongRTC-Server 状态回调地址，详细参考：https://docs.cn.rongcloud.cn/v4/views/im/noui/guide/chatroom/manage/basic/status/serverapi.html
>
> RongRTC-Server 状态回调地址：http://{ip}:{port}/mic/room/status_sync