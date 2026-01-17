package com.sky.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 日期工具类
 */
public class DateUtil {

    // 根据begin和end获取一个存放了从begin到end范围内每天的日期的LocalDate集合
    public static List<LocalDate> getDateList(LocalDate begin, LocalDate end){
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }
}
