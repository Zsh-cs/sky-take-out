package com.sky;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.concurrent.TimeUnit;

//@SpringBootTest
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate(){
        System.out.println(redisTemplate);
    }


    // 操作字符串类型的数据
    @Test
    public void testString(){
        ValueOperations ops = redisTemplate.opsForValue();

        // set
        ops.set("city","Swatow");

        // get
        String city = (String) ops.get("city");
        System.out.println("city: "+city);

        // setex
        ops.set("code","123456",30, TimeUnit.SECONDS);

        // setnx
        ops.setIfAbsent("lock","1");
        ops.setIfAbsent("lock","2");// 设置失败

    }


    // 操作哈希类型的数据
    @Test
    public void testHash(){
        HashOperations ops = redisTemplate.opsForHash();

        // hset
        Object key="student";
        ops.put(key,"name","zjl");
        ops.put(key,"age","99");
        ops.put(key,"sex","male");

        // hget
        String name = (String) ops.get(key, "name");
        System.out.println("name: "+name);

        // hdel
        ops.delete(key,"age");

        // hkeys
        System.out.println(ops.keys(key));

        // hvals
        System.out.println(ops.values(key));
    }
}










