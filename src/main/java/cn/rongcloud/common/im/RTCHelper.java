package cn.rongcloud.common.im;

import cn.rongcloud.common.im.config.IMProperties;
import cn.rongcloud.common.im.config.RTCProperties;
import cn.rongcloud.common.im.pojos.*;
import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.common.utils.HttpHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Slf4j
@Component
public class RTCHelper {
    private static final String UTF8 = "UTF-8";

    @Autowired
    HttpHelper httpHelper;

    @Autowired
    RTCProperties rtcProperties;

    @Autowired
    IMProperties imProperties;

    /**
     * 会议录制
     **/
    public RTCRecordResp startRecord(String roomId, RTCStartRecordReq info) throws Exception {
        if (roomId == null) {
            throw new IllegalArgumentException("Paramer 'roomId' is required");
        }
        if (info.getConfig().getModel() == null) {
            throw new IllegalArgumentException("Paramer 'config.mode' is required");
        }
        if (info.getConfig().getVideoFormat() == null) {
            throw new IllegalArgumentException("Paramer 'config.videoFormat' is required");
        }
        if (info.getConfig().getAudioFormat() == null) {
            throw new IllegalArgumentException("Paramer 'config.audioFormat' is required");
        }
        if (info.getConfig().getVideoResolution() == null) {
            throw new IllegalArgumentException("Paramer 'config.videoResolution' is required");
        }
        if (info.getConfig().getHostUserId() == null) {
            throw new IllegalArgumentException("Paramer 'config.hostUserId' is required");
        }
        if (info.getConfig().getHostStreamId() == null) {
            throw new IllegalArgumentException("Paramer 'config.hostStreamId' is required");
        }

        String body = GsonUtil.toJson(info);

        HttpURLConnection conn = httpHelper.createCommonPostHttpConnection(rtcProperties.getHost(), imProperties.getAppKey(), imProperties.getSecret(), "/rtc/record/start.json",
                "application/json");
        conn.setRequestProperty("Room-Id", roomId);
        httpHelper.setBodyParameter(body, conn);

        return (RTCRecordResp) GsonUtil.fromJson(httpHelper.returnResult(conn), RTCRecordResp.class);
    }

    /**
     * 结束录像
     **/
    public RTCRecordResp endRecord(String roomId, String sessionId) throws Exception {
        if (roomId == null) {
            throw new IllegalArgumentException("Paramer 'roomId' is required");
        }
        if (sessionId == null) {
            throw new IllegalArgumentException("Paramer 'sessionId' is required");
        }

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("sessionId", sessionId);
        String body = GsonUtil.toJson(bodyMap);
        HttpURLConnection conn = httpHelper.createCommonPostHttpConnection(rtcProperties.getHost(), imProperties.getAppKey(), imProperties.getSecret(), "/rtc/record/stop.json",
                "application/json");
        conn.setRequestProperty("Room-Id", roomId);
        httpHelper.setBodyParameter(body, conn);

        return (RTCRecordResp) GsonUtil.fromJson(httpHelper.returnResult(conn), RTCRecordResp.class);
    }

    /**
     * 查询房间中的用户
     * @param roomId
     * @return
     * @throws Exception
     */
    public RTCRoomMemberResp queryRoomMembers(String roomId) throws Exception {
        if (roomId == null) {
            throw new IllegalArgumentException("Paramer 'roomId' is required");
        }
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("roomId", roomId);
        String body = GsonUtil.toJson(bodyMap);
        HttpURLConnection conn = httpHelper.createCommonPostHttpConnection(imProperties.getHost(), imProperties.getAppKey(), imProperties.getSecret(), "/rtc/room/query.json",
                "application/json");
        conn.setRequestProperty("Room-Id", roomId);
        httpHelper.setBodyParameter(body, conn);
        return (RTCRoomMemberResp) GsonUtil.fromJson(httpHelper.returnResult(conn), RTCRoomMemberResp.class);
    }


    public RTCCloudPlayerResp cloudPlayerStart(RTCCloudPlayerReq req) throws Exception {
        // 这里应该有参数校验，但是接口文档没有写哪些必填
        String body = GsonUtil.toJson(req);
        HttpURLConnection conn = httpHelper.createCommonPostHttpConnection(imProperties.getHost(), imProperties.getAppKey(), imProperties.getSecret(), "/v2/rtc/cloudplayer/rtmp/start",
                "application/json");
        conn.setRequestProperty("Room-Id", req.getRoomId());
        httpHelper.setBodyParameter(body, conn);
        return (RTCCloudPlayerResp) GsonUtil.fromJson(httpHelper.returnResult(conn), RTCCloudPlayerResp.class);

    }

}
