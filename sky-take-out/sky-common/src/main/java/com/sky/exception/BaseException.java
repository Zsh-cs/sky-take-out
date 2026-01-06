package com.sky.exception;

/**
 * 基本异常，是exception包下所有异常的父类
 */
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}
