package com.lt.gulimill.gulimallproduct;



import com.lt.gulimall.product.GulimallProductApplication;
import com.lt.gulimall.product.entity.AttrEntity;
import com.lt.gulimall.product.entity.AttrGroupEntity;
import com.lt.gulimall.product.service.AttrGroupService;
import com.lt.gulimall.product.service.AttrService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootTest(classes = GulimallProductApplication.class)
@RunWith(SpringRunner.class)
class GulimallProductApplicationTests {

    @Autowired
    private AttrService attrService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;

    @Test
    public void redisson(){

    }



    @Test
    void contextLoads() throws IOException {
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set("hello","word");
        System.out.println(stringStringValueOperations.get("hello"));
    }

}
