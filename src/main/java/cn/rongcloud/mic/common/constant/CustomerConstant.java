package cn.rongcloud.mic.common.constant;

/**
 * Created by sunyinglong on 2020/6/5
 */
public class CustomerConstant {

    private CustomerConstant() {
    }

    public static final String SERVICENAME = "mic";
    public static final String SYSTEM_USER_ID = "DLMrJ9prNw2vcl0MJavRClK"; //系统用户ID

   public static final Integer CACHE_TIME = 2 * 60 * 60;

    //系统用户 用来发消息的
   public static final String SYSTEM_UID="_SYSTEM_";

    /**
     * 微信session_key缓存
     */
    public static final String WX_SESSION_KEY_CACHE_PREFIX = "wx_session_key:";

    public static final int WX_REQUEST_SUCCESS = 0;
}
