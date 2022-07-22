package cn.rongcloud.common.whiteboard.herewhite;


import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.common.utils.HttpHelper;
import cn.rongcloud.common.whiteboard.herewhite.config.HereWhiteProperties;
import cn.rongcloud.common.whiteboard.herewhite.pojos.HereWhiteApiResultInfo;
import cn.rongcloud.common.whiteboard.herewhite.pojos.HereWhiteBanInfo;
import cn.rongcloud.common.whiteboard.herewhite.pojos.HereWhiteCreateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;

@Component
public class HereWhiteHelper {

    @Autowired
    HttpHelper httpHelper;

    @Autowired
    HereWhiteProperties wbProperties;

    public HereWhiteApiResultInfo create(String name) throws Exception {
        if (name == null) {
            throw new IllegalArgumentException("Paramer 'name' is required");
        }

        HereWhiteCreateInfo wbCreateInfo = new HereWhiteCreateInfo();
        wbCreateInfo.setLimit(0);
        wbCreateInfo.setModel("persistent");
        wbCreateInfo.setName(name);

        String body = GsonUtil.toJson(wbCreateInfo);

        HttpURLConnection connection= httpHelper.createHereWhitePostHttpConnection(wbProperties.getApi(), "/room", "application/json", wbProperties.getToken());
        httpHelper.setBodyParameter(body, connection);
        return (HereWhiteApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(connection), HereWhiteApiResultInfo.class);
    }

    public HereWhiteApiResultInfo banRoom(Boolean ban, String uuid) throws Exception {
        if (ban == null) {
            throw new IllegalArgumentException("Paramer 'ban' is required");
        }
        if (uuid == null) {
            throw new IllegalArgumentException("Paramer 'uuid' is required");
        }

        HereWhiteBanInfo wbBanInfo = new HereWhiteBanInfo();
        wbBanInfo.setBan(ban);
        wbBanInfo.setUuid(uuid);
        String body = GsonUtil.toJson(wbBanInfo);

        HttpURLConnection connection= httpHelper.createHereWhitePostHttpConnection(wbProperties.getApi(), "/banRoom", "application/json", wbProperties.getToken());
        httpHelper.setBodyParameter(body, connection);

        return (HereWhiteApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(connection), HereWhiteApiResultInfo.class);
    }
}
