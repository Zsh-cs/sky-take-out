package com.sky;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.concurrent.TimeUnit;

@SpringBootTest
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

        ops.set("city","Swatow");
        String city = (String) ops.get("city");
        System.out.println("city: "+city);

        ops.set("code","123456",30, TimeUnit.SECONDS);
        ops.setIfAbsent("lock","1");
        ops.setIfAbsent("lock","2");// 设置失败

    }
}
