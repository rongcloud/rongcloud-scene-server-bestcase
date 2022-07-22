package cn.rongcloud.mic.common.utils;

import cn.rongcloud.mic.common.exception.BusinessException;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 *
 * @author zhouxingbin
 * @since 2021/09/26
 */
public class WAssert {

    // 标记为@Nullable说明可以传入null
    public static void notNull(@Nullable Object object, String message, String code) {
        if (object == null) {
            throw new BusinessException(code, message);
        }
    }

    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new BusinessException("999", message);
        }
    }

    public static boolean isNull(@Nullable Object object) {
        return object == null ? true : false;
    }

    public static boolean isEmpty(String expression) {
        if (StringUtils.isEmpty(expression)) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String expression) {
        if (!StringUtils.isEmpty(expression)) {
            return true;
        }
        return false;
    }

    public static void isTrue(boolean bool, String message, String code) {
        if (!bool) {
            throw new BusinessException(code, message);
        }
    }

    public static void isTrue(boolean bool, String message) {
        if (!bool) {
            throw new BusinessException("999", message);
        }
    }

    public static void notEmpty(String expression, String message, String code) {
        if (StringUtils.isEmpty(expression)) {
            throw new BusinessException(code, message);
        }
    }


    public static void notEmpty(String expression, String message) {
        if (StringUtils.isEmpty(expression)) {
            throw new BusinessException("999", message);
        }
    }

}
