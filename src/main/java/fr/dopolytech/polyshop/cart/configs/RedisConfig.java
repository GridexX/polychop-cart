package fr.dopolytech.polyshop.cart.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@EnableRedisRepositories
public class RedisConfig {

    // @Value("${redis.host}")
    // private String redisHost;

    // @Value("${redis.port}")
    // private Integer redisPort;

    // @Bean
    // public RedisConnectionFactory redisConnectionFactory() {
    //     return new LettuceConnectionFactory("redis", 6379);
    // }

    // @Bean
    // public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    //     RedisTemplate<?, ?> template = new RedisTemplate<>();
    //     template.setConnectionFactory(redisConnectionFactory);
    //     return template;
    // }

    @Bean
    ReactiveRedisOperations<String, Long> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Long> serializer = new Jackson2JsonRedisSerializer<>(Long.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Long> builder = RedisSerializationContext
            .newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Long> context = builder.value(serializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

    // @Bean
    // public RedisTemplate<String, Object> redisTemplate() {
    //     RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    //     redisTemplate.setConnectionFactory(redisConnectionFactory());
    //     redisTemplate.setKeySerializer(new StringRedisSerializer());
    //     redisTemplate.setValueSerializer(new StringRedisSerializer());
    //     redisTemplate.setEnableTransactionSupport(true);
    //     redisTemplate.afterPropertiesSet();
    //     return redisTemplate;
    // }

}

