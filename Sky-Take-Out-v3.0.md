# Sky-Take-Out-v3.0 Release Notes

## *Info*

+ Author: Zsh-cs
+ Key Development Cycle: 2026.1.2-2026.1.17



---



## *Introduction*

### 一、管理端

#### 1.员工管理

- 员工登录
- 员工退出登录
- 新增员工
- 修改密码
- 修改员工信息
- 启用、禁用员工账号
- 员工分页查询
- 根据id查询员工

#### 2.分类管理

- 新增分类
- 根据id逻辑删除分类
- 修改分类信息
- 启用、停用分类
- 分类分页查询
- 根据id查询分类
- 根据类型查询已启用的分类

#### 3.菜品管理

- 新增菜品和对应的菜品口味
- 根据id列表批量逻辑删除菜品
- 修改分类信息
- 起售、停售菜品
- 菜品分页查询
- 根据id查询菜品
- 根据分类id查询菜品

#### 4.套餐管理

- 新增套餐和对应的套餐菜品
- 根据id列表批量逻辑删除套餐
- 修改套餐信息
- 起售、停售套餐
- 套餐分页查询
- 根据id查询套餐

#### 5.店铺营业状态

+ 通过`Redis`设置店铺营业状态
+ 通过`Redis`获取店铺营业状态

#### 6.订单管理

+ 接单
+ 拒单
+ 商家取消订单
+ 派送订单
+ 完成订单
+ 各个状态的订单数量统计
+ 订单分页查询
+ 查询订单详情
+ 使用`SpringTask`实现每分钟自动取消超时未付款的订单
+ 使用`SpringTask`实现每天凌晨一点自动完成一直在派送中的订单

#### 7.数据统计

+ 使用`Apache ECharts`实现：

  + 营业额统计

  + 用户统计：新增用户数和总用户数

  + 订单统计

  + 获取销量TOP10的商品

+ 使用`Apache POI`实现将近30天的运营数据导出为Excel报表

#### 8.工作台

+ 查询今日运营数据
+ 菜品总览
+ 套餐总览
+ 查询订单管理数据



### 二、用户端

#### 1.用户接口

+ 微信用户登录
+ 微信用户退出登录

#### 2.店铺营业状态

+ 通过`Redis`获取店铺营业状态

#### 3.分类接口

+ （根据类型）查询已启用的分类

#### 4.菜品接口

+ 根据分类id查询已启用的菜品及其关联的口味
+ 使用`Redis`缓存菜品

#### 5.套餐接口

+ 根据分类id查询已启用的套餐
+ 根据套餐id查询套餐包含的菜品
+ 使用`Redis`+`SpringCache`缓存套餐

#### 6.购物车接口

+ 添加当前用户的购物车
+ 展示当前用户的购物车
+ 清空当前用户的购物车
+ 删除当前用户购物车的一个商品

#### 7.地址簿接口

+ 新增地址
+ 根据id删除地址
+ 根据id修改地址
+ 设置默认地址
+ 根据id查询地址
+ 查询当前用户的所有地址
+ 获取默认地址

#### 8.订单接口

+ 用户下单
+ 订单支付
+ 当前用户的历史订单分页查询
+ 查询历史订单详情
+ 用户取消订单
+ 再来一单：将原订单中的商品重新加入到购物车中
+ 使用`WebSocket`实现用户来单和催单提醒



---



## *What I Learn*

#### 1.注意空指针异常，不能给null对象赋值

在进行微信用户登录这一功能的开发时，我被一个bug卡了好久：使用Postman向微信接口服务发送GET请求获取openid是成功的，但在Java程序中却失败，报错如下：

```json
{
    "errcode":40002,
    "errmsg":"invalid grant_type, rid: 69613ee1-1363e562-5faeede8"
}
```

经过仔细排查和Gemini的协助，我发现`UserServiceImpl`中新用户自动完成注册部分的代码存在如下bug：

在`if(user==null)`内部，我忘记先new一个User出来，导致user仍为数据库查出的结果null，这时候调用`setOpenid`必然失败，因为不能给null对象赋值！



#### 2.涉及多表操作和单表遍历增删改操作必须开启事务



#### 3.要保证缓存与数据库的一致性

如果对某个对象使用了缓存，在提高用户端访问速度的同时，也要注意对该对象进行**增删改**操作后，必须同步清理缓存以保证缓存与数据库的一致性。



#### 4.同模块Service → Mapper，跨模块Service → Service

举例如下：

在一个正经的微服务架构或单体架构里，`OrderService`应该调用`AddressService`，而不是`AddressMapper`。

Service之间通过接口交互，绝不直接碰对方的Mapper，否则会导致系统强耦合。

Address的逻辑可能是： 

+ 地址是否合法
+ 是否被禁用 
+ 是否属于该用户 
+ 是否支持配送 

如果Order直接查Mapper，这些逻辑要么重复写，要么被绕过，无形之中已经把Address的业务逻辑拷贝到Order里了。

**总结**： 

+ 同模块：Service → Mapper 
+ 跨模块：Service → Service



#### 5.前端约定好的属性名，不要随意修改，否则会导致前端无法解析

在开发好订单分页查询功能后，我发现前端订单分页查询后订单菜品不展示。

前端约定好返回结果示例如下：

```json
{ 
    "code": 0, 
    "msg": null, 
    "data": { 
        "total": 0, 
        "records": [ 
            { 
                "id": 0, 
                "number": "string", 
                "status": 0, 
                "userId": 0, 
                "addressBookId": 0, 
                "orderTime": "string", 
                "checkoutTime": null, 
                "payMethod": 0, 
                "payStatus": 0, 
                "amount": 0, 
                "remark": "string", 
                "userName": "string", 
                "phone": "string", 
                "address": "string", 
                "consignee": "string", 
                "cancelReason": "string", 
                "rejectionReason": "string", 
                "cancelTime": "string", 
                "estimatedDeliveryTime": "string", 
                "deliveryStatus": 0, "deliveryTime": 
                "string", "packAmount": 0, 
                "tablewareNumber": 0, 
                "tablewareStatus": 0, 
                "orderDishes": "string" 
            } 
        ] 
    } 
}
```

可以发现，前端已经规定好data-records的订单菜品属性名为orderDishes。

而我由于追求命名准确性，将OrderVO的属性`private String orderDishes`
修改为`private String orderFoodsInfo`，反而弄巧成拙，导致前端无法从`OrderVO`中解析出orderDishes属性的值，从而导致前端订单菜品没有展示出来。



#### 6.表字段别名必须和待封装对象的属性名保持一致，MyBatis才能够正确封装

在开发销量TOP10商品展示这一功能时，前端有商品名字但无销量。

我初步怀疑是mapper层存在bug，我的`OrderDetailMapper.xml`代码如下：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.sky.mapper.OrderDetailMapper">

    <!--获取销量TOP10的商品-->
    <select id="getTop10Sales" resultType="com.sky.dto.GoodsSalesDTO">
        # Caution: 此处sum(od.number)的别名必须是number，否则无法封装到GoodsSalesDTO中
        select od.name, sum(od.number) as number
        from order_detail od, orders o
        where od.order_id=o.id and o.status=5 and o.order_time between #{begin} and #{end}
        group by od.name
        order by number desc
        limit 10
    </select>

</mapper>
```

我怀疑可能是销量没有封装到GoodsSalesDTO中才导致前端不显示商品销量。

我的`GoodsSalesDTO`代码如下：

```java
package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsSalesDTO implements Serializable {

    // 商品名称
    private String name;
    // 销量
    private Integer number;
}
```

终于找到了bug！od.number字段别名sale和DTO属性名number不一致，导致MyBatis无法正确将销量封装到DTO中。

修改`OrderDetailMapper.xml`代码如下：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.sky.mapper.OrderDetailMapper">

    <!--获取销量TOP10的商品-->
    <select id="getTop10Sales" resultType="com.sky.dto.GoodsSalesDTO">
        # Caution: 此处sum(od.number)的别名必须是number，否则无法封装到GoodsSalesDTO中
        select od.name, sum(od.number) as number
        from order_detail od, orders o
        where od.order_id=o.id and o.status=5 and o.order_time between #{begin} and #{end}
        group by od.name
        order by number desc
        limit 10
    </select>

</mapper>
```



#### 7.直接修改生产环境下已编译、压缩的静态资源文件是一种常见的临时调试手段，但必须注意浏览器缓存带来的不同步问题，清空缓存并强制刷新是解决不同步问题的标准方法

我修改了苍穹外卖工作台js代码中的一个导航链接文本，将“订单明细”更改为“订单管理”。修改文件并保存后，在浏览器中刷新页面，前端界面显示的文本依然是旧的“订单明细”，并未更新为“订单管理”。

最可能的原因是**浏览器缓存**：浏览器为了提升性能，会缓存静态资源（如js、css文件），因此它没有从服务器下载最新的文件，而是直接使用了本地缓存的旧版本。

于是我在前端网页按下F12，然后右键点击浏览器左上角的刷新按钮，执行**清空缓存并硬性重新加载**操作，强制浏览器忽略本地缓存，从服务器重新下载所有资源，成功解决了这个问题。

![image-20260117174305019](images/image-20260117174305019.png)

