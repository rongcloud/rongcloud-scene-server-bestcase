package cn.rongcloud.mic.common.config.redis;

import cn.rongcloud.mic.room.model.TRoom;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;

/**
 * Created by sunyinglong on 2020/6/3
 */
@Configuration("commonRedisTemplateFactory")
public class RedisTemplateFactory {

    @Resource
    private JedisConnectionFactory connectionFactory;

    @Bean(name = "keyStringSerializer")
    public RedisSerializer<String> keyStringSerializer() {
        return new StringRedisSerializer();
    }

    @Bean(name = "valueObjectSerializer")
    public RedisSerializer<Object> valueObjectSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisSerializer<String> keyStringSerializer,
        RedisSerializer<Object> valueObjectSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(keyStringSerializer);
        template.setValueSerializer(valueObjectSerializer);
        return template;
    }

    @Bean(name = "roomRedisTemplate")
    public RedisTemplate<String, TRoom> roomRedisTemplate(
        RedisSerializer<String> keyStringSerializer) {
        RedisTemplate<String, TRoom> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(keyStringSerializer);
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean(destroyMethod = "destroy")
    public RedisLockRegistry redisLockRegistry(JedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "lock");
    }
}
