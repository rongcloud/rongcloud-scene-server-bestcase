package cn.rongcloud.common.whiteboard.rongcloud;

import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.common.utils.HttpHelper;
import cn.rongcloud.common.whiteboard.rongcloud.config.WhiteBoardProperties;
import cn.rongcloud.common.whiteboard.rongcloud.pojos.WhiteBoardApiResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URLEncoder;

/**
 * Created by weiqinxiao on 2019/3/7.
 */
@Component
public class WhiteBoardHelper {
    private static final String UTF8 = "UTF-8";

    @Autowired
    HttpHelper httpHelper;

    @Autowired
    WhiteBoardProperties whiteBoardProperties;

    public WhiteBoardApiResultInfo create(String appkey, String roomId) throws Exception {
        if (roomId == null) {
            throw new IllegalArgumentException("Paramer 'roomId' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&appId=").append(URLEncoder.encode(appkey, UTF8));
        sb.append("&roomNr=").append(URLEncoder.encode(roomId, UTF8));
        HttpURLConnection connection= httpHelper.createWhiteBoardPostHttpConnection(whiteBoardProperties.getHost(), "/ewb/room/create", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(sb.toString(), connection);

        return (WhiteBoardApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(connection), WhiteBoardApiResultInfo.class);
    }

    public WhiteBoardApiResultInfo destroy(String appkey, String roomId) throws Exception {
        if (roomId == null) {
            throw new IllegalArgumentException("Paramer 'roomId' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&appId=").append(URLEncoder.encode(appkey, UTF8));
        sb.append("&roomNr=").append(URLEncoder.encode(roomId, UTF8));
        HttpURLConnection connection= httpHelper.createWhiteBoardPostHttpConnection(whiteBoardProperties.getHost(), "/ewb/room/destroy", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(sb.toString(), connection);

        return (WhiteBoardApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(connection), WhiteBoardApiResultInfo.class);
    }
}
