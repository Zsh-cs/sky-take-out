package com.sky.exception;

/**
 * 密码修改失败
 */
public class PasswordEditFailedException extends BaseException{

    public PasswordEditFailedException(String msg){
        super(msg);
    }

}
