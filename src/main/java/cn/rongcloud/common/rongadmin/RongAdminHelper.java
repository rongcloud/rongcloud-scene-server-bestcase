package cn.rongcloud.common.rongadmin;

import cn.rongcloud.common.rongadmin.config.RongAdminProperties;
import cn.rongcloud.common.utils.HttpHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Slf4j
@Component
public class RongAdminHelper {
    private static final String UTF8 = "UTF-8";

    @Autowired
    HttpHelper httpHelper;

    @Autowired
    RongAdminProperties rongAdminProperties;

    /**
     * 用户数据上传
     * type: 1=>SealTalk,2=>SealRTC,3=>SealMeeting,4=>SealMic,5=>SealClass,6=>SealLive
     **/
    public void sendData(String mobile, String region, Integer type) throws Exception {
        if (mobile == null) {
            throw new IllegalArgumentException("Paramer 'mobile' is required");
        }
        if (region == null) {
            throw new IllegalArgumentException("Paramer 'region' is required");
        }
        if (type == null) {
            throw new IllegalArgumentException("Paramer 'type' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&mobile=").append(URLEncoder.encode(mobile, UTF8));
        sb.append("&region=").append(URLEncoder.encode(region, UTF8));
        sb.append("&demo_type=").append(type.toString());
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createRongAdminPostHttpConnection(rongAdminProperties.getHost(), "/demoApi/sendData", "application/x-www-form-urlencoded");

        httpHelper.setBodyParameter(body, conn);
        httpHelper.returnResult(conn);
    }

}
