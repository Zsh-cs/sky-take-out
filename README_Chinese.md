# 苍穹外卖

*语言*：中文/[英文](README.md)



### 1.项目介绍

苍穹外卖是一个用于练习的前后端分离的外卖项目。

#### 1.1 功能架构

+ **管理端**：员工管理、分类管理、菜品管理、套餐管理、订单管理、工作台、数据统计、来单提醒。
+ **用户端**：微信登录、商户浏览、购物车、用户下单、微信支付、历史订单、地址管理、用户催单。

#### 1.2 技术选型

+ **用户层**：`node.js`、`VUE.js`、`ElementUI`、微信小程序、`apache echarts`
+ **网关层**：`Nginx`
+ **应用层**：
  + `Spring`系列：`SpringBoot`、`SpringMVC`、`SpringTask`、`SpringCache`
  + 其他：`HttpCilent`、`JWT`、阿里云OSS、`POI`、`WebSocket`
+ **持久层**：`MyBatis`、`MyBatis-Plus`、`Spring Data Redis`
+ **数据库**：`MySQL`、`Redis`

#### 1.3 开发工具

+ **集成开发环境**：`Intellij IDEA 2024.2.1`、`DataGrip 2025.3`

+ **项目管理**：`Maven`
+ **单元测试**：`JUnit`

+ **接口设计**：`YApi`、`Apifox`
+ **接口调试**：`Swagger`、`Knife4j`、`Postman`

+ **版本控制**：`Git`、`GitHub`



### 2.启动项目的前置步骤

1. 启动Nginx和Tomcat，其中Tomcat已集成在SpringBoot中。
2. 修改`application-dev.yml`中的数据源，替换成你自己的数据库配置信息，包括但不限于数据库名称、用户名和密码。
3. 配置两个系统环境变量`ALIYUN_OSS_ACCESS_KEY_ID`和`ALIYUN_OSS_ACCESS_KEY_SECRET`，填入你自己的阿里云OSS的AccessKey的id和密钥，然后相应地修改`application-dev.yml`中的阿里云OSS，替换成你自己的阿里云OSS配置信息。



