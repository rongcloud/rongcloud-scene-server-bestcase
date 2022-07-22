package cn.rongcloud.mic.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedissonUtil {

    @Resource
    private RedissonClient redissonClient;

    /**
     * set value
     *
     * @param key    string
     * @param value  object
     * @param expire 过期时间s -1表示不限制
     */
    public void set(String key, Object value, int expire) {
        log.info("redis set cache key={},value={},expire={}", key, value, expire);
        try {
            RBucket<Object> redissonClientBucket = redissonClient.getBucket(key, new JsonJacksonCodec());
            redissonClientBucket.set(value);
            if (expire > 0) {
                redissonClientBucket.expire(expire, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis cache error key={},value={},expire={}", key, value, expire);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        log.info("redis get cache key={},clazz={}", key, clazz);
        try {
            Object o = redissonClient.getBucket(key, new JsonJacksonCodec()).get();
            log.info("redis get cache key={},o={}", key, o);
            if (o == null) {
                return null;
            }
            if (clazz.isInstance(o)) {
                return (T) o;
            } else if (clazz == Long.class && o instanceof Integer) {
                Integer obj = (Integer) o;
                return (T) Long.valueOf(obj.longValue());
            }
            return (T) o;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis get cache error key={},clazz={}", key, clazz);
        }
        return null;
    }

    public String get(String key) {
        return get(key, String.class);
    }

    public Boolean setnx(String key, Object value, int expire) {
        log.info("redis setnx cache key={},value={},expire={}", key, value, expire);
        Boolean ret;
        try {
            RBucket<Object> redissonClientBucket = redissonClient.getBucket(key);
            if (expire > 0) {
                ret = redissonClientBucket.trySet(value,expire,TimeUnit.SECONDS);
            } else {
                ret = redissonClientBucket.trySet(value);
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis setnx cache error key={},value={},expire={}", key, value, expire);
        }
        return null;

    }

    public boolean delete(String key) {
        log.info("redis delete cache key={}", key);
        try {
            return redissonClient.getBucket(key).delete();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("redis delete error cache key={}", key);
        }
        return false;
    }

    public double getScore(String key, Object value) {
        try {
            RScoredSortedSet<Object> sort = redissonClient.getScoredSortedSet(key);
            Integer rank = sort.rank(value);
            sort.valueRange(0,2);
            return sort.getScore(value);
        }catch (Exception e) {
            e.printStackTrace();
            log.info("redis getScore error cache key={}", key);
        }
        return 0;
    }
}
