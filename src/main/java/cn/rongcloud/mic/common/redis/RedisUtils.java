package cn.rongcloud.mic.common.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Date: 2021/6/4 10:22
 * @Author: by zxw
 */
@Component
public final class RedisUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key
     * @param time
     * @return
     */
    public void expire(String key, long time) {
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key
     * @return
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);

    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return true 存在 false不存在
     */

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */

    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    public void del(List<String> list) {
        if (list != null && list.size() > 0) {
            redisTemplate.delete(list);
        }
    }

    public void dels(Set<String> list) {
        if (list != null && list.size() > 0) {
            redisTemplate.delete(list);
        }
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key
     * @return
     */

    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 批量获取
     *
     * @param key
     * @return
     */
    public Set<String> keys(String key) {
        return key == null ? null : redisTemplate.keys(key + '*');
    }

    /**
     * 普通缓存放入
     *
     * @param key
     * @param value
     */

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 普通缓存放入并设置时间
     *
     * @param key
     * @param value
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */

    public void set(String key, Object value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
    }

    /**
     * 递增
     *
     * @param key
     * @param delta 要增加几(大于0)
     * @return
     */

    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key
     * @param delta 要减少几(小于0)
     * @return
     */

    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ================================zset=================================

    /**
     * zSet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */

    public Object zset(String key, String item, double value) {
        return redisTemplate.opsForZSet().add(key, item, value);
    }

    /**
     * zremove
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */

    public Object zremove(String key, String item) {
        return redisTemplate.opsForZSet().remove(key,item);
    }

    /**
     * score
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */

    public Double score(String key, String item) {
        return redisTemplate.opsForZSet().score(key, item);
    }

    /**
     * incrementScore
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */

    public Double incrementScore(String key, String item, double value) {
        return redisTemplate.opsForZSet().incrementScore(key, item, value);
    }

    /**
     * rangeByScore
     *
     * @param key 键 不能为null
     * @return 值
     */

    public Object rangeByScore(String key, double start, double end) {
        return redisTemplate.opsForZSet().rangeByScore(key, start, end);
    }

    /**
     * range
     *
     * @param key 键 不能为null
     * @return 值
     */

    public Object range(String key, Long var1, Long var2) {
        return redisTemplate.opsForZSet().range(key, var1, var2);
    }

    /**
     * rangeByScore
     *
     * @param key 键 不能为null
     * @return 值
     */

    public Object reverseRange(String key, Long var1, Long var2) {
        return redisTemplate.opsForZSet().reverseRange(key, var1, var2);
    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */

    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);

    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key
     * @return 对应的多个键值
     */

    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);

    }

    /**
     * HashSet
     *
     * @param key
     * @param map 对应多个键值
     */

    public void hmset(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }


    /**
     * HashSet 并设置时间
     *
     * @param key
     * @param map  对应多个键值
     * @param time 时间(秒)
     */

    public void hmset(String key, Map<String, Object> map, long time) {

        redisTemplate.opsForHash().putAll(key, map);
        if (time > 0) {
            expire(key, time);
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key
     * @param item  项
     * @param value 值
     */

    public void hset(String key, String item, Object value) {
        redisTemplate.opsForHash().put(key, item, value);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     */

    public void hset(String key, String item, Object value, long time) {
        redisTemplate.opsForHash().put(key, item, value);
        if (time > 0) {
            expire(key, time);
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */

    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     */

    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     */

    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */

    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key
     */

    public Set<Object> sGet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key
     * @param value
     * @return true 存在 false不存在
     */

    public boolean sHasKey(String key, Object value) {

        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 将数据放入set缓存
     *
     * @param key
     * @param values 值 可以是多个
     * @return 成功个数
     */

    public long sSet(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 将set数据放入缓存
     *
     * @param key
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */

    public long sSetAndTime(String key, long time, Object... values) {
        long count = redisTemplate.opsForSet().add(key, values);
        if (time > 0) {
            expire(key, time);
        }
        return count;
    }

    /**
     * 获取set缓存的长度
     *
     * @param key
     * @return
     */

    public long sGetSetSize(String key) {

        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 移除值为value的
     *
     * @param key
     * @param values 值 可以是多个
     * @return 移除的个数
     */

    public long setRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    // ===============================list=================================

    /**
     * 将list放入缓存
     *
     * @param key
     * @param value
     * @return
     * @author zhaodx
     * @date 2020-08-05 17:30
     */
    public Long leftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 将list pop 取出
     *
     * @param key
     * @return
     * @author zhaodx
     * @date 2020-08-05 17:30
     */
    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */

    public List<Object> lGet(String key, long start, long end) {

        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key
     * @return
     */

    public long lGetListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */

    public Object lGetIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 将list放入缓存
     *
     * @param key
     * @param value
     * @param time  时间(秒)
     * @return
     */

    public Long lSet(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 将list放入缓存
     *
     * @param key
     * @param value
     * @param time  时间(秒)
     * @return
     */

    public Long lSet(String key, Object value, long time) {
        Long rightPush = redisTemplate.opsForList().rightPush(key, value);
        if (time > 0) {
            expire(key, time);
        }
        return rightPush;
    }

    /**
     * 将list放入缓存
     *
     * @param key
     * @param value
     * @param time  时间(秒)
     * @return
     */

    public Long lSet(String key, List<Object> value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 将list放入缓存
     *
     * @param key
     * @param value
     * @param time  时间(秒)
     * @return
     */

    public Long lSet(String key, List<Object> value, long time) {
        Long rightPushAll = redisTemplate.opsForList().rightPushAll(key, value);
        if (time > 0) {
            expire(key, time);
        }
        return rightPushAll;
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key
     * @param index 索引
     * @param value
     * @return
     */

    public void lUpdateIndex(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */

    public long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }
}
