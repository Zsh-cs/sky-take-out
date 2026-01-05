package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliyunOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private AliyunOssUtil aliyunOssUtil;

    // 文件上传
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){// 参数名需与前端请求一致，故叫作file
        log.info("文件上传：{}",file);

        try {
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            // 截取原始文件名的扩展名，如jpg
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 为了防止文件重名导致覆盖，需要对原始文件通过uuid进行重命名
            String objectName= UUID.randomUUID()+extension;
            // 文件路径
            String filePath = aliyunOssUtil.upload(file.getBytes(), objectName);

            return Result.success(filePath);
        } catch (IOException e) {
            log.error(MessageConstant.UPLOAD_FAILED +"，原因：{}",e);
            return Result.error(e.getMessage());
        }

    }
}
