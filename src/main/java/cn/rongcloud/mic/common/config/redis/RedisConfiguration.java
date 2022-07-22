package cn.rongcloud.mic.common.config.redis;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by sunyinglong on 2020/6/3
 */
@Configuration
public class RedisConfiguration {

    @Value("${redis.host}")
    private String hostName;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.pass}")
    private String password;
    @Value("${redis.maxIdle}")
    private int maxIdle;
    @Value("${redis.maxTotal}")
    private int maxTotal;
    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${redis.database}")
    private Integer database;

    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setTestOnBorrow(testOnBorrow);
        return config;
    }

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(hostName);
        if (StringUtils.isNotBlank(password)) {
            configuration.setPassword(RedisPassword.of(password));
        }
        configuration.setPort(port);
        if (database != null && database <= 15) {
            configuration.setDatabase(database);
        }
        return configuration;
    }

    @Bean
    public JedisClientConfiguration clientConfiguration() {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        return builder.usePooling()
                .poolConfig(jedisPoolConfig())
                .build();
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory(redisStandaloneConfiguration(), clientConfiguration());
    }
}
