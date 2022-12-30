package com.lt.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @ClassName MyRedissonConfig
 * @Description:
 * @Author lite
 * @Date 2022/12/29
 * @Version V1.0
 **/
@Configuration
public class MyRedissonConfig {

    @Bean(destroyMethod="shutdown")
    RedissonClient redisson() throws IOException {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.79.130:6379");
        return Redisson.create(config);
    }
}
