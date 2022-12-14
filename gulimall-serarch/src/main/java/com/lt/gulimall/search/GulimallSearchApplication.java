package com.lt.gulimall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName GulimallSearchApplication
 * @Description:
 * @Author lite
 * @Date 2022/12/2
 * @Version V1.0
 **/

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients("com.lt.gulimall.search.feign")
public class GulimallSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallSearchApplication.class, args);
    }
}
