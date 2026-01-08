# Sky-Take-Out

*Language*: English/[Chinese](README_Chinese.md)



### 1.Project Introduction

Sky-Take-Out is a front-end and back-end separated takeout project  designed for practice purposes.

#### 1.1 Functional Architecture

- **Admin End**: Employee Management, Category Management, Dish Management, Set Meal Management, Order Management, Dashboard, Data Statistics, New Order Notification.
- **User End**: WeChat Login, Merchant Browsing, Shopping Cart, User Order Placement, WeChat Payment, Order History, Address Management, Order Reminder.

#### 1.2 Technology Stack

- **User Layer**: `node.js`, `VUE.js`, `ElementUI`, WeChat Mini Program, `apache echarts`
- **Gateway Layer**: `Nginx`
- **Application Layer**:
  - `Spring` Family: `SpringBoot`, `SpringMVC`, `SpringTask`, `SpringCache`
  - Others: `HttpCilent`, `JWT`, Alibaba Cloud OSS, `POI`, `WebSocket`
- **Persistence Layer**: `MyBatis`, `MyBatis-Plus`, `Spring Data Redis`
- **Database**: `MySQL`, `Redis`

#### 1.3 Development Tools

- **Integrated Development Environment**: `Intellij IDEA 2024.2.1`, `DataGrip 2025.3`
- **Project Management**: `Maven`
- **Unit Testing**: `JUnit`
- **Interface Design**: `YApi`, `Apifox`
- **Interface Debugging**: `Swagger`, `Knife4j`, `Postman`
- **Version Control**: `Git`, `GitHub`



### 2.Pre-launch Preparation Steps

1. Start Nginx and Tomcat, where Tomcat is already built into SpringBoot.
2. Modify the data source configuration in `application-dev.yml` and replace it with your own database details, including but not limited to the database name, username and password.
3. Configure two system environment variables: `ALIYUN_OSS_ACCESS_KEY_ID` and `ALIYUN_OSS_ACCESS_KEY_SECRET`, and fill them with the AccessKey ID and secret of your own Alibaba Cloud OSS. Then modify the corresponding Alibaba Cloud OSS configuration items in `application-dev.yml` to match your personal OSS settings.

