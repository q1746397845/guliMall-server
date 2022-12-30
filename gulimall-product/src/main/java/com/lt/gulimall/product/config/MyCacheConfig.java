package com.lt.gulimall.product.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @ClassName MyCacheConfig
 * @Description:
 * @Author lite
 * @Date 2022/12/30
 * @Version V1.0
 **/
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class MyCacheConfig {

    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    RedisCacheConfiguration redisCacheConfiguration(){
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        CacheProperties.Redis redis = cacheProperties.getRedis();
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
        if(redis.getTimeToLive() != null){
            config = config.entryTtl(redis.getTimeToLive());
        }
        if(redis.getKeyPrefix() != null){
            config = config.prefixCacheNameWith(redis.getKeyPrefix());
        }
        if(!redis.isCacheNullValues()){
            config = config.disableCachingNullValues();
        }
        if(!redis.isUseKeyPrefix()){
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
