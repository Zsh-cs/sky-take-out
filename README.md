# Sky-Take-Out

*Language*: English/[Chinese](README_Chinese.md)



> Video for Reference: 【黑马程序员Java项目实战《苍穹外卖》，最适合新手的SpringBoot+SSM的企业级Java项目实战】https://www.bilibili.com/video/BV1TP411v7v6?vd_source=b7f14ba5e783353d06a99352d23ebca9



### 1.Project Introduction

Sky-Take-Out is a front-end and back-end separated takeout project  designed for practice purposes.

#### 1.1 Functional Architecture

- **Admin End**: Employee Management, Category Management, Dish Management, Set Meal Management, Order Management, Dashboard, Data Statistics, New Order Notification.
- **User End**: WeChat Login, Merchant Browsing, Shopping Cart, User Order Placement, WeChat Payment, Order History, Address Management, Order Reminder.

#### 1.2 Technology Stack

- **User Layer**: `node.js`, `VUE.js`, `ElementUI`, WeChat Mini Program, `apache echarts`
- **Gateway Layer**: `Nginx 1.20.2`
- **Application Layer**:
  - `Spring` Family: `SpringBoot 2.7.2`, `SpringMVC`, `SpringTask`, `SpringCache`
  - Others: `HttpCilent`, `JWT`, Alibaba Cloud OSS, `POI`, `WebSocket`
- **Persistence Layer**: `MyBatis-Plus 3.5.4`, `MyBatis`, `Spring Data Redis`
- **Database**: `MySQL 8.0.38`, `Redis 6.2.19(Docker)`

#### 1.3 Development Tools

+ **Operating System**: Windows 11 Home China Edition 25H2

- **Integrated Development Environment**: `Intellij IDEA 2024.2.1`
- **Graphical Database Management Tool**: `DataGrip 2025.3`, `Another Redis Desktop Manager 1.7.1`
- **Project Management**: `Maven`
- **Unit Testing**: `JUnit`
- **Interface Design**: `YApi`, `Apifox`
- **Interface Debugging**: `Swagger`, `Knife4j`, `Postman`
- **Version Control**: `Git`, `GitHub`



### 2.Pre-launch Preparation Steps

1. Start MySQL.
2. Run the `start-redis-by-docker.bat` script, and then the cmd will start Redis by Docker. **Note**: Make sure Docker Desktop is installed on your computer and the Redis 6.2.19 image has been pulled.
3. Double-click `nginx-1.20.2/nginx.exe` to launch Nginx.
4. Open IntelliJ IDEA and start SkyApplication.
5. Modify the database configuration in `application-dev.yml` and replace it with your own database details, including but not limited to the database name, username and password.
6. Configure two system environment variables: `ALIYUN_OSS_ACCESS_KEY_ID` and `ALIYUN_OSS_ACCESS_KEY_SECRET`, and fill them with the AccessKey ID and secret of your own Alibaba Cloud OSS. Then modify the corresponding Alibaba Cloud OSS configuration items in `application-dev.yml` to match your personal OSS settings.

