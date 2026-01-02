# 苍穹外卖项目接口文档


**简介**:苍穹外卖项目接口文档


**HOST**:localhost:8080


**联系人**:


**Version**:1.0


**接口路径**:/v2/api-docs


[TOC]






# 员工相关接口


## 员工登录


**接口地址**:`/admin/employee/login`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "password": "",
  "username": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|employeeLoginDTO|员工登录时传递的数据模型|body|true|EmployeeLoginDTO|EmployeeLoginDTO|
|&emsp;&emsp;password|密码||false|string||
|&emsp;&emsp;username|用户名||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result«EmployeeLoginVO»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|data||EmployeeLoginVO|EmployeeLoginVO|
|&emsp;&emsp;id|主键值|integer(int64)||
|&emsp;&emsp;name|姓名|string||
|&emsp;&emsp;token|jwt令牌|string||
|&emsp;&emsp;userName|用户名|string||
|msg||string||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"id": 0,
		"name": "",
		"token": "",
		"userName": ""
	},
	"msg": ""
}
```


## 员工退出登录


**接口地址**:`/admin/employee/logout`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|Result«string»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code||integer(int32)|integer(int32)|
|data||string||
|msg||string||


**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": ""
}
```