# 苍穹外卖

*语言*：中文/[英文](README.md)



> 参考视频：【黑马程序员Java项目实战《苍穹外卖》，最适合新手的SpringBoot+SSM的企业级Java项目实战】https://www.bilibili.com/video/BV1TP411v7v6?vd_source=b7f14ba5e783353d06a99352d23ebca9



### 1.项目介绍

苍穹外卖是一个用于练习的前后端分离的外卖项目。

#### 1.1 功能架构

+ **管理端**：员工管理、分类管理、菜品管理、套餐管理、订单管理、工作台、数据统计、来单提醒。
+ **用户端**：微信登录、商户浏览、购物车、用户下单、微信支付、历史订单、地址管理、用户催单。

#### 1.2 技术选型

+ **编程语言**：`Java(corretto-17)`

+ **用户层**：`node.js`、`VUE.js`、`ElementUI`、微信小程序 2.25.4、`Apache ECharts`
+ **网关层**：`Nginx 1.20.2`
+ **应用层**：
  + `Spring`系列：`SpringBoot 2.7.3`、`SpringMVC`、`SpringCache`、`SpringTask`
  + 其他：`HttpCilent`、`JWT`、阿里云OSS、`Apache POI 3.16`、`WebSocket`
+ **持久层**：`MyBatis-Plus 3.5.4`、`MyBatis`、`Spring Data Redis`
+ **数据库**：`MySQL 8.0.38`、`Redis 6.2.19(Docker)`

#### 1.3 开发工具

+ **操作系统**：Windows 11 家庭中文版 25H2

+ **集成开发环境**：`Intellij IDEA 2024.2.1`、微信开发者工具 2.01.2510260

+ **数据库图形化交互工具**：
  + `MySQL`：`DataGrip 2025.3`

  + `Redis`：`Another Redis Desktop Manager 1.7.1`

+ **项目管理**：`Maven`
+ **单元测试**：`JUnit`

+ **接口设计**：`YApi`、`Apifox`
+ **接口调试**：`Swagger`、`Knife4j`、`Postman`

+ **版本控制**：`Git`、`GitHub`

+ **内网穿透工具**：`cpolar`



### 2.启动项目的前置步骤

1. 启动MySQL并执行`database/sky.sql`。
2. 运行`start-redis-by-docker.bat`脚本，然后cmd会通过Docker启动Redis。**注意**：确保你的电脑已经安装了Docker Desktop并拉取了Redis 6.2.19。
3. 双击`nginx-1.20.2/nginx.exe`，启动Nginx。**注意**：Nginx必须位于全英文路径下。
4. 修改`application-dev.yml`中的数据库相关配置，替换成你自己的数据库配置信息，包括但不限于数据库名称、用户名和密码。
5. 配置两个系统环境变量`ALIYUN_OSS_ACCESS_KEY_ID`和`ALIYUN_OSS_ACCESS_KEY_SECRET`，填入你自己的阿里云OSS的AccessKey的id和密钥，然后相应地修改`application-dev.yml`中的阿里云OSS，替换成你自己的阿里云OSS配置信息。
6. 修改`application-dev.yml`中的微信小程序，替换成你自己的微信小程序配置信息。
7. 进入Intellij IDEA，启动SkyApplication。



