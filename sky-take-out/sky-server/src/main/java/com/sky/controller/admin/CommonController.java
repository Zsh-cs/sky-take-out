package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private CommonService commonService;

    // 文件上传
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){// 参数名需与前端请求一致，故叫作file
        log.info("文件上传：{}",file);
        return Result.success();
    }
}
