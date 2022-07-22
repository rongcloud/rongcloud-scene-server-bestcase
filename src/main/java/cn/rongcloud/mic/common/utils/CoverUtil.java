package cn.rongcloud.mic.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CoverUtil {

    public static <T> T sourceToTarget(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }
        try {
            T targetObject = target.newInstance();
            BeanUtils.copyProperties(source, targetObject);
            return targetObject;
        } catch (Exception var4) {
            log.error("convert error ", var4);
        }
        return null;
    }

    public static <T> List<T> sourceToTarget(Collection<?> sourceList, Class<T> target) {
        if (sourceList == null) {
            return null;
        }
        return sourceList.stream().map(t -> CoverUtil.sourceToTarget(t, target))
                .collect(Collectors.toList());
    }

}
