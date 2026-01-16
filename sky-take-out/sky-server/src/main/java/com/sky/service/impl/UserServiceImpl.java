package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    // 微信接口服务地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    // 微信用户登录
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {

        // 1.调用微信接口服务，获取当前微信用户的openid
        //Caution: 微信的openid与appid是强绑定的，一个appid对应一个openid，
        //  每次前端传来变化的code而非直接传来openid只是为了增强安全性，避免他人截货并冒用openid发送请求
        Map<String, String> map=new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");

        // 2.判断openid是否为空，如果为空，说明登录失败，抛出业务异常
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 3.根据openid去user表中查询是否存在对应用户，从而判断当前用户是否为苍穹外卖的新用户
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(User::getOpenid,openid);
        User user = userMapper.selectOne(lqw);
        if(user==null){
            // 4.如果是新用户，自动完成注册
            //Caution: 暂时获取不到用户的其他信息，后续可以由用户去个人中心自行完善
            //Caution: 必须先new一个User出来，否则user仍为null，调用setter会报错！
            user=new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            userMapper.save(user);
        }

        return user;
    }


    // 根据id查询用户
    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }


    // 根据日期统计当天新增用户数
    @Override
    public Integer countNewUsersByDate(LocalDate date) {
        LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);

        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.between(User::getCreateTime,startOfDay,endOfDay);
        return Math.toIntExact(userMapper.selectCount(lqw));
    }


    // 根据日期统计当天总用户数
    @Override
    public Integer countTotalUsersByDate(LocalDate date) {
        LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);

        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.le(User::getCreateTime,endOfDay);
        return Math.toIntExact(userMapper.selectCount(lqw));
    }
}
