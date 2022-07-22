package cn.rongcloud.mic.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Component;

/**
 * Desc: 
 *
 * @author zhouxingbin
 * @date 2021/10/15
 */
@Component
public class PoolUtils {

    private static ExecutorService pool = Executors.newFixedThreadPool(15);

    public <T> void submit(ProcessHandler<T> processHandler) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                processHandler.call();
            }
        });
    }

    public interface ProcessHandler<T> {

        public T call();

    }

}
