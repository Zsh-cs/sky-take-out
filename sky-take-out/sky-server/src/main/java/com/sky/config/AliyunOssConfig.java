package com.sky.config;

import com.sky.properties.AliyunOssProperties;
import com.sky.utils.AliyunOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建AliyunOssUtil对象
 */
@Configuration
@Slf4j
public class AliyunOssConfig {

    public AliyunOssUtil aliyunOssUtil(AliyunOssProperties properties){
        log.info("开始创建阿里云OSS文件上传工具对象...");
        return new AliyunOssUtil(
                properties.getEndpoint(),
                properties.getAccessKeyId(),
                properties.getAccessKeySecret(),
                properties.getBucketName()
        );
    }
}
