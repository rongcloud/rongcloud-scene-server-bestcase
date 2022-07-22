package cn.rongcloud.mic.common.aop;

import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.constant.CustomerConstant;
import cn.rongcloud.mic.common.rest.RestResult;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Aspect
@Component
@Slf4j
public class RedisKeyRepeatAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 定义切点
     */
    @Pointcut("@annotation(cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit)")
    public void preventDuplication() {
    }

    /**
     * 环绕通知 （可以控制目标方法前中后期执行操作，目标方法执行前后分别执行一些代码）
     *
     * @param joinPoint
     * @return
     */
    @Around("preventDuplication()")
    public Object before(ProceedingJoinPoint joinPoint) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //获取执行方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //获取防重复提交注解
        RedisKeyRepeatSubmit annotation = method.getAnnotation(RedisKeyRepeatSubmit.class);

        // 获取token以及方法标记，生成redisKey和redisValue
        String redisKey =CustomerConstant.SERVICENAME
                .concat("|repeat|")
                .concat(getMethodSign(method, joinPoint.getArgs()));
        String redisValue = redisKey.concat(annotation.value()).concat("submit duplication");
        log.info("RedisKeyRepeatAspect key:{},value:{}",redisKey,redisValue);
        if (!redisTemplate.hasKey(redisKey)) {
            //设置防重复操作限时标记（前置通知）
            redisTemplate.opsForValue()
                    .set(redisKey, redisValue, annotation.expireSeconds(), TimeUnit.SECONDS);
            try {
                //正常执行方法并返回
                //ProceedingJoinPoint类型参数可以决定是否执行目标方法，且环绕通知必须要有返回值，返回值即为目标方法的返回值
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                //确保方法执行异常实时释放限时标记(异常后置通知)
                redisTemplate.delete(redisKey);
                throw new RuntimeException(throwable);
            }
        } else {
            if(method.getReturnType().equals(RestResult.class)){
                return RestResult.generic(10001,"请勿重复提交");
            }
            throw new RuntimeException("the method must return cn.rongcloud.mic.common.rest.RestResult");
        }
    }

    /**
     * 生成方法标记：采用数字签名算法SHA1对方法签名字符串加签
     *
     * @param method
     * @param args
     * @return
     */
    private String getMethodSign(Method method, Object... args) {
        StringBuilder sb = new StringBuilder(method.toString());
        for (Object arg : args) {
            sb.append(toString(arg));
        }
        log.info("sb:{}",sb.toString());
        return DigestUtils.sha1DigestAsHex(sb.toString());
    }

    private String toString(Object arg) {
        if (Objects.isNull(arg)) {
            return "null";
        }
        if (arg instanceof Number) {
            return arg.toString();
        }
        return JSONObject.toJSONString(arg);
    }
}