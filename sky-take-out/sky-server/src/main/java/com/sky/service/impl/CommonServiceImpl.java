package com.sky.service.impl;

import com.sky.mapper.CommonMapper;
import com.sky.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private CommonMapper commonMapper;

    // 文件上传
    @Override
    public void upload(File file) {

    }
}
