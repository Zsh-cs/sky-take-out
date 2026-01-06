package com.sky.exception;

/**
 * 套餐启用失败
 */
public class SetmealEnableFailedException extends BaseException {

    public SetmealEnableFailedException(){}

    public SetmealEnableFailedException(String msg){
        super(msg);
    }
}
