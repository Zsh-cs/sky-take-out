package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 捕获业务异常
    @ExceptionHandler
    public Result exceptionHandler(BaseException e) {
        log.error("异常信息：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    // 捕获SQL异常
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException e) {
        // Duplicate entry 'arthur' for key 'employee.idx_username'
        String message = e.getMessage();
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" ");
            String name = split[2];
            if (message.contains("employee")) {
                return Result.error("用户名" + name + MessageConstant.ALREADY_EXISTS);
            } else if (message.contains("category")) {
                return Result.error("分类名称" + name + MessageConstant.ALREADY_EXISTS);
            } else {
                return Result.error(name + MessageConstant.ALREADY_EXISTS);
            }
        } else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

}
