package cn.rongcloud.mic.room.key;

import static cn.rongcloud.mic.room.constant.RoomKeysCons.REDIS_GIFT_KEY;

import cn.rongcloud.mic.common.constant.CustomerConstant;

/**
 * Desc: 
 *
 * @author zhouxingbin
 * @date 2021/10/11
 */
public class KeyGen {

    public static String getRedisRoomIdsKey() {
        return CustomerConstant.SERVICENAME + "|room_ids";
    }

    public static String getRedisRoomIdsKey(Integer type) {
        return CustomerConstant.SERVICENAME + "|type_room_ids|" + type;
    }

    public static String getRedisRoomInfosKey() {
        return CustomerConstant.SERVICENAME + "|rooms_info";
    }

    public static String getRedisRoomGagUserKey(String roomId) {
        return CustomerConstant.SERVICENAME + "|gag_user|" + roomId;
    }

    public static String getRedisRoomManageUserKey(String roomId) {
        return CustomerConstant.SERVICENAME + "|manage_user|" + roomId;
    }

    public static String getRedisRoomGiftKey(String roomId) {
        return CustomerConstant.SERVICENAME + "|user_gift|" + roomId;
    }

    public static String getRedisRoomPkGiftKey(String roomId) {
        return CustomerConstant.SERVICENAME + "|pk|user_gift|" + roomId;
    }

    public static String getRedisRoomPkScoreKey(String roomId) {
        return CustomerConstant.SERVICENAME + "|pk|score|" + roomId;
    }

    // ==============================================================

    public static String getGiftKey(Long giftId) {
        return REDIS_GIFT_KEY + "|" + giftId;
    }

}
