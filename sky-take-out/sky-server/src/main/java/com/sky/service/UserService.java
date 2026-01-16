package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

import java.time.LocalDate;

public interface UserService {

    // 微信用户登录
    User wxLogin(UserLoginDTO userLoginDTO);

    // 根据id查询用户
    User getById(Long id);

    // 根据日期统计当天新增用户数
    Integer countNewUsersByDate(LocalDate date);

    // 根据日期统计当天总用户数
    Integer countTotalUsersByDate(LocalDate date);
}
