package cn.rongcloud.mic.community.constant;

import cn.rongcloud.mic.common.constant.CustomerConstant;

public class CommunityConstant {

    public static String COMMUNITY_INFO = CustomerConstant.SERVICENAME + "|community|info|%s";

    public static String GROUP_INFO = CustomerConstant.SERVICENAME + "|group|info|%s";

    public static String CHANNEL_INFO = CustomerConstant.SERVICENAME + "|channel|info|%s";

    public static String COMMUNITY_USER_INFO = CustomerConstant.SERVICENAME + "|community_user|info|%s|%s";

    public static Integer INFO_TIME=60*60*24*1; // 1天

    /**
     *
     */
    public static String COMMUNITY_USER_LIST =  CustomerConstant.SERVICENAME + "|community|user|%s|%s";

    public static String SAVE_LOCK = CustomerConstant.SERVICENAME + "|community|lock|%s|%s";

    public static Integer SAVE_LOCK_TIME = 3; // 3秒
}
