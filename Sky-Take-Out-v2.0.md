# Sky-Take-Out-v2.0 Release Notes

## *Introduction*

### 一、管理端

#### 1.员工管理

- 员工登录
- 员工退出登录
- 新增员工
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
+ 订单分页查询
+ 查询订单详情



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
+ 查询默认地址

#### 8.订单接口

+ 用户下单
+ 订单支付
+ 当前用户的历史订单分页查询
+ 查询历史订单详情
+ 用户取消订单
+ 再来一单：将原订单中的商品重新加入到购物车中



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



