package cn.rongcloud.mic.room.constant;

import cn.rongcloud.mic.common.constant.CustomerConstant;

/**
 * Desc: 
 *
 * @author zhouxingbin
 * @date 2021/10/11
 */
public class RoomKeysCons {

    public static String REDIS_ROOM_USER_TOTAL_KEY = CustomerConstant.SERVICENAME + "|room_user_total";
    public static String REDIS_ROOM_PK_STATUS_KEY = CustomerConstant.SERVICENAME + "|room_pk_status";
    public static String REDIS_ROOM_PK_ROOM_KEY = CustomerConstant.SERVICENAME + "|room_pk_room";
    public static String REDIS_ROOM_PK_SCORE_KEY = CustomerConstant.SERVICENAME + "|room_pk_score";

    public static String REDIS_USER_SCORE_ACCOUNT = CustomerConstant.SERVICENAME + "|user_score_account";

    public static String REDIS_GIFT_KEY = CustomerConstant.SERVICENAME + "|gift_key";

    // 房间正在播放音乐key
    public static String REDIS_ROOM_MUSIC_PLAY = CustomerConstant.SERVICENAME + "|room_music|play|%s";
    public static Integer REDIS_ROOM_MUSIC_PLAY_TIME=60*20; // 20分钟

}
