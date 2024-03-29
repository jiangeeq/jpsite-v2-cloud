## jpsite-v2-cloud 企业级微服务快速开发解决方案

### 2020年最新微服务架构版本（发布日期：2021.1.1）

随着现代应用的架构演变, 微服务的概念逐渐兴起, 相关的技术栈与架构体系也日渐成熟, 本项目基于apache与alibaba的开源框架, 构建一套企业级的微服务开发快速解决方案,
该套架构是目前2020年主流微服务开发框架, 后续有机会将改造成Service Mesh架构

### 2021年最新微服务架构版本（发布日期：2022.1.1）
时过境迁，转眼就要来到2020年了，程序的发展进步越来越快，微服务已成过去，云原生已经逐渐普及，
众多开发语言齐头并进，Java生态体系也有了一些新的变化，为了顺应时代的发展，框架做了部分版本升级已新的
技术融合，该套架构是目前依然是2021年主流微服务开发框架，后续有机会将改造成Service Mesh架构


### 技术栈：
|相关技术|版本|介绍|
|---|---|---|
|nacos| 1.3.2 -> 2.0.3 | 一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台|
|dubbo| 3.0.0| 一款高性能、轻量级的开源 Java 服务通信框架|
|jdk | 15 | 2020年Java开发员的软件开发工具包|
|elasticSearch| 7.6 | 一个分布式多用户能力的全文搜索引擎|
|spring-security|  5.3.4.RELEASE | 一个功能强大且高度可定制的身份验证和访问控制框架|
|spring   |    5.2.9.RELEASE | 为现代基于 Java 的企业应用程序提供了一个全面的编程和配置模型|
|spring-boot  |  2.3.4.RELEASE | 一个框架，一种全新的编程规范，其设计目的是用来简化新Spring应用的初始搭建以及开发过程|
|spring-security-oauth2 | 2.3.4.RELEASE| 提供最新的 OAuth 2.0 支持|
|mybatis-plus |   3.4.0 | 一个 MyBatis (opens new window)的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生|
|spring-mvc |   5.2.9.RELEASE | 最初建立在 Servlet API 之上的 Web 框架
|knife4j  |            2.0.4| Knife4j是一个为Swagger接口文档赋能的工具|
|lombok  |        1.18.2| 一种 Java 工具，可用来帮助开发人员消除 Java 的冗长|
|lettuce  |  5.3.4.RELEASE|一个Redis的Java驱动包|
|spring-cloud-gateway| 3.0.3|一个用于在 Spring WebFlux 之上构建 API 网关的库|
|Reactor| 3.4.8	 |第四代响应式库，基于Reactive Streams规范，用于在 JVM 上构建非阻塞应用程序|
|RocketMQ| -> 4.9.2 | 一个统一消息引擎、轻量级数据处理平台|
|skywalking| |分布式系统的应用程序性能监控工具，专为微服务、云原生和基于容器（Docker、Kubernetes、Mesos）架构而设计|
|docker| 20.10.8 | 一个虚拟环境容器，可以将你的开发环境、代码、配置文件等一并打包到这个容器中，并发布和应用到任意平台中|
|sentinel| -> 1.8.2 | 一个强大的流量控制组件，可实现微服务的可靠性、弹性和监控|
|seata| | 一款开源的分布式事务解决方案，致力于在微服务架构下提供高性能和简单易用的分布式事务服务|
|<h4><font color="red">云原生套件| <h4><font color="red">版本| <h4><font color="red">DevOps企业级云原生架构|
| Prometheus |  |  一个开源系统监控和警报工具包   | 
| kubernetes | v1.21 |一个开源系统，用于自动化部署、扩展和管理容器化应用程序|
| Grafana |  | 一个完全开源的度量分析与可视化平台，可对来自各种各种数据源的数据进行查询、分析、可视化处理以及配置告警 | 
| Alertmanager |  | 一个独立的告警模块,接收Prometheus等客户端发来的警报    | 
| jenkins |  |  一个开源的、提供友好操作界面的持续集成(CI)工具， 主要用于持续、自动的构建/测试软件项目 | 
| go|  |  一个开源的编程语言,它能让构造简单、可靠且高效的软件变得容易 | 
| Istio |  |  提供一种统一化的微服务连接、安全保障、管理与监控方式。 | 
| EFK |  |  提供了一整套开源实时日志分析解决方案  |
| Rancher |  | 多云混合云多集群Kubernetes管理平台 | 
| harbor |  | 为企业用户设计的容器镜像仓库 | 
| nexus |  | 一个强大的Maven仓库管理器 | 
| sonarqube |  | 一个开源的代码分析平台, 用来持续分析和评测项目源代码的质量 | 
| kubesphere | 3.2.0 | 在 Kubernetes 之上构建的面向云原生应用的容器混合云开源平台 | 

### 未来计划：
**关于技术栈替换**

|原技术|未来技术|介绍|
|---|---|---|
|spring-mvc | spring-webFlux| 一个采用响应式堆的 Web 框架 |
|lettuce | redission | 一个高级的分布式协调 Redis 客户端 |

![alt jpsite-cloud集成dubbo&nacos&getway架构图](http://assets.processon.com/chart_image/5fa16bd1e0b34d28c56a29d2.png)

![alt oauth2 授权中心流程](http://assets.processon.com/chart_image/5f8d4d04e401fd06fd932ec5.png)

### 本地开发流程：
首先安装相关依赖项目模块
```
dove-common  mvn install
rbac-api mvn install
```

1. 启动dubbo-rbac   
    1.2 启动dubbo-shop
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
