package com.summitsync.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${summitsync.redis.host}")
    private String redisHostname;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        var redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHostname);
        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(this.redisConnectionFactory());
        return template;
    }
}
