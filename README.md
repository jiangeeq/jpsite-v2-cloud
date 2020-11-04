## jpsite-v2-cloud 企业级微服务快速开发解决方案

随着现代应用的架构演变, 微服务的概念逐渐兴起, 相关的技术栈与架构体系也日渐成熟, 本项目基于apache与alibaba的开源框架, 构建一套企业级的微服务开发快速解决方案,
该套架构是目前2020年主流微服务开发框架, 后续有机会将改造成Service Mesh架构

### 技术栈：
|相关技术|版本|
|---|---|
|kubernetes|xxx|
|docker|xxx|
|sentinel|xxx|
|seata|xxx|
|nacos|xxx|
|dubbo|3.0.0|
|RocketMQ|xxx|
|skywalking|xxx|
|Prometheus|xxx|
|jdk |14|
|elasticSearch|xxx|
|spring-security|  5.3.4.RELEASE|
|spring   |    5.2.9.RELEASE|
|spring-boot  |  2.3.4.RELEASE|
|spring-security-oauth2 | 2.3.4.RELEASE|
|mybatis-plus |   3.4.0|
|spring-mvc |   5.2.9.RELEASE|
|knife4j  |            2.0.4|
|lombok  |        1.18.2|
|lettuce  |  5.3.4.RELEASE|

![alt jpsite-cloud集成dubbo&nacos&getway架构图](http://assets.processon.com/chart_image/5fa16bd1e0b34d28c56a29d2.png)

![alt oauth2 授权中心流程](http://assets.processon.com/chart_image/5f8d4d04e401fd06fd932ec5.png)

### 本地开发流程：
首先安装相关依赖项目模块
```
dove-common  mvn install
rbac-api mvn install
```

1. 启动dubbo-rbac
2. 启动auth-server
3. 启动client

访问localhost, 登录用户名jiangpeng， 密码123456

nacos地址：http://106.52.26.148:8848/nacos    
client api doc：http://localhost/doc.html

### 代码提交规范
每次提交的代码必须保证是测试通过的， 且提交commit必须符合如下规范，便于其他开发者审阅与学习。

git commit 描述使用如下形式前缀，冒号后面加空格隔开   
fix: 、feat: 、refactor: 、     

示例：
```
fix: 修复了字段缺失
feat: 新增短信验证码校验
refactor: 移除多余代码块
```
