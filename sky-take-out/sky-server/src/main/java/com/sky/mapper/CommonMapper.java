package com.sky.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.io.File;

@Mapper
public interface CommonMapper {

    @Insert("")
    void upload(File file);
}
