package cn.rongcloud.mic.common.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlideWindowUtil {

    private volatile static Map<String, LinkedList<Long>> MAP = new ConcurrentHashMap<String, LinkedList<Long>>();

    private SlideWindowUtil (){}


    public static synchronized boolean isGo(String key, int count, long timeWindow){
        Long now = System.currentTimeMillis();

        List<Long> list = MAP.computeIfAbsent(key, k -> new LinkedList<>());

        if(list.size()<count){
            list.add(0,now);
            return true;
        }

        Long firstTime = list.get(count - 1);
        if(now-firstTime<timeWindow){
            return false;
        }else{
            list.remove(count-1);
            list.add(0,now);
            return true;
        }

    }

}
