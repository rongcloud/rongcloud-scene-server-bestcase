package cn.rongcloud.common.utils;

public class RandomUtils {

    /**
     * 随机获取 N 位随机数
     * @param length
     * @return
     */
    public static String getRandomNumber(int length){
        int k = 1;
        for (int i=1; i < length; i++) {
            k = k * 10;
        }
        return String.valueOf((int) ((Math.random() * 9 + 1) * k));
    }
}
