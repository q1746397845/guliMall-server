package com.ly.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName GulimallElasticSearchConfig
 * @Description:
 * @Author lite
 * @Date 2022/12/2
 * @Version V1.0
 **/
@Configuration
public class GulimallElasticSearchConfig {

    @Bean
    public RestHighLevelClient esRestClient(){
        RestHighLevelClient client = new RestHighLevelClient
                (RestClient.builder ( new HttpHost( "192.168.79.130" , 9200 , "http" )));
        return client;
    }
}
