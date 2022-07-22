package cn.rongcloud.mic.user.constant;

import cn.rongcloud.mic.common.constant.CustomerConstant;

public class UserKeysCons {

    public static String REDIS_USER_TOKEN = CustomerConstant.SERVICENAME + "|user_token|%s";

    public static String REDIS_INTERNATIONAL_SMS_CODE=CustomerConstant.SERVICENAME+"|internation_sms_verifycode|%s";


    // 60 天，因为app 端可能没有做登录过期处理
    public static Integer USER_TOKEN_TIME = 60*24*60*60;

}
