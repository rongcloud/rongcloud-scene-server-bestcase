package cn.rongcloud.common.im;

import cn.rongcloud.common.im.pojos.IMApiResultInfo;
import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.common.utils.HttpHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GroupHelper {

    private static final String UTF8 = "UTF-8";

    @Autowired
    HttpHelper httpHelper;

    /**
     * 创建群
     * @param groupId
     * @param groupName
     * @param userId
     * @return
     * @throws Exception
     */
    public IMApiResultInfo createGroup(String groupId, String groupName, String userId) {
        if(StringUtils.isBlank(groupId)||StringUtils.isBlank(groupName)||StringUtils.isBlank(userId)){
            throw new IllegalArgumentException("Paramer is invalid");
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("&userId=").append(URLEncoder.encode(userId, UTF8));
            sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
            sb.append("&groupName=").append(URLEncoder.encode(groupName, UTF8));
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }
            HttpURLConnection conn  = httpHelper
                    .createIMPostHttpConnection("/ultragroup/create.json", "application/x-www-form-urlencoded");
            httpHelper.setBodyParameter(body, conn);
            return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public IMApiResultInfo delGroup(String groupId){
        if(StringUtils.isBlank(groupId)){
            throw new IllegalArgumentException("Paramer is invalid");
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }
            HttpURLConnection conn = httpHelper
                    .createIMPostHttpConnection("/ultragroup/dis.json", "application/x-www-form-urlencoded");
            httpHelper.setBodyParameter(body, conn);
            return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加入超级群
     * @param groupId
     * @param userId
     * @return
     * @throws Exception
     */
    public IMApiResultInfo joinGroup(String groupId,String userId){
        if(StringUtils.isBlank(groupId)||StringUtils.isBlank(userId)){
            throw new IllegalArgumentException("Paramer is invalid");
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("&userId=").append(URLEncoder.encode(userId, UTF8));
            sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }
            HttpURLConnection conn  = httpHelper
                    .createIMPostHttpConnection("/ultragroup/join.json", "application/x-www-form-urlencoded");
            httpHelper.setBodyParameter(body, conn);
            return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 退出超级群
     * @param groupId
     * @param userId
     * @return
     * @throws Exception
     */
    public IMApiResultInfo exitGroup(String groupId,String userId) {
        if(StringUtils.isBlank(groupId)||StringUtils.isBlank(userId)){
            throw new IllegalArgumentException("Paramer is invalid");
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("&userId=").append(URLEncoder.encode(userId, UTF8));
            sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }
            HttpURLConnection conn = httpHelper
                    .createIMPostHttpConnection("/ultragroup/quit.json", "application/x-www-form-urlencoded");
            httpHelper.setBodyParameter(body, conn);
            return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新超级群
     * @param groupId
     * @param groupName
     * @return
     * @throws Exception
     */
    public IMApiResultInfo updateGroup(String groupId,String groupName) {
        if(StringUtils.isBlank(groupId)||StringUtils.isBlank(groupName)){
            throw new IllegalArgumentException("Paramer is invalid");
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("&groupName=").append(URLEncoder.encode(groupName, UTF8));
            sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }
            HttpURLConnection conn = httpHelper
                    .createIMPostHttpConnection("/ultragroup/refresh.json", "application/x-www-form-urlencoded");
            httpHelper.setBodyParameter(body, conn);
            return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 添加用户超级群禁言
     * @param groupId
     * @param userIds
     * @return
     * @throws Exception
     */
    public IMApiResultInfo addGroupBanned(String groupId, List<String> userIds){
        if(StringUtils.isBlank(groupId)|| CollectionUtils.isEmpty(userIds)||userIds.size()>20){
            throw new IllegalArgumentException("Paramer is invalid");
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
            sb.append("&userIds=").append(URLEncoder.encode(String.join(",",userIds), UTF8));
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }
            HttpURLConnection conn = httpHelper
                    .createIMPostHttpConnection("/ultragroup/userbanned/add.json", "application/x-www-form-urlencoded");
            httpHelper.setBodyParameter(body, conn);
            return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除超级群用户禁言
     * @param groupId
     * @param userIds
     * @return
     * @throws Exception
     */
    public IMApiResultInfo delGroupBanned(String groupId,List<String> userIds) {
        if(StringUtils.isBlank(groupId)|| CollectionUtils.isEmpty(userIds)||userIds.size()>20){
            throw new IllegalArgumentException("Paramer is invalid");
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
            sb.append("&userIds=").append(URLEncoder.encode(String.join(",",userIds), UTF8));
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }
            HttpURLConnection conn = httpHelper
                    .createIMPostHttpConnection("/ultragroup/userbanned/del.json", "application/x-www-form-urlencoded");
            httpHelper.setBodyParameter(body, conn);
            return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建群频道
     * @param groupId
     * @param busChannel
     * @return
     * @throws Exception
     */
    public IMApiResultInfo createChannel(String groupId, String busChannel) {
        if(StringUtils.isBlank(groupId)||StringUtils.isBlank(busChannel)){
            throw new IllegalArgumentException("Paramer is invalid");
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
            sb.append("&busChannel=").append(URLEncoder.encode(busChannel, UTF8));
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }
            HttpURLConnection conn = httpHelper
                    .createIMPostHttpConnection("/ultragroup/channel/create.json", "application/x-www-form-urlencoded");
            httpHelper.setBodyParameter(body, conn);
            return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除群频道
     * @param groupId
     * @param channelUid
     * @return
     * @throws Exception
     */
    public IMApiResultInfo delChannel(String groupId, String channelUid) {
        if(StringUtils.isBlank(groupId)||StringUtils.isBlank(channelUid)){
            throw new IllegalArgumentException("Paramer is invalid");
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
            sb.append("&busChannel=").append(URLEncoder.encode(channelUid, UTF8));
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }
            HttpURLConnection conn  = httpHelper
                    .createIMPostHttpConnection("/ultragroup/channel/del.json", "application/x-www-form-urlencoded");

            httpHelper.setBodyParameter(body, conn);
            return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除群频道
     * @param groupId
     * @return
     * @throws Exception
     */
    public String queryChannel(String groupId) {
        if(StringUtils.isBlank(groupId)){
            throw new IllegalArgumentException("Paramer is invalid");
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("&groupId=").append(URLEncoder.encode(groupId, UTF8));
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }
            HttpURLConnection conn  = httpHelper
                    .createIMPostHttpConnection("/ultragroup/channel/get.json", "application/x-www-form-urlencoded");

            httpHelper.setBodyParameter(body, conn);
            String returnResult = httpHelper.returnResult(conn);
            log.info("returnResult:{}",GsonUtil.toJson(returnResult));
            return returnResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送超级群消息
     * @return
     * @throws Exception
     */
    public IMApiResultInfo sendCommunityMessage(String fromUserId, List<String> toGroupIds,BaseMessage baseMessage,String channelUid) {
        if(StringUtils.isBlank(fromUserId)||CollectionUtils.isEmpty(toGroupIds)||baseMessage==null||toGroupIds.size()>3){
            throw new IllegalArgumentException("Paramer is invalid");
        }
        try {
            Map<String,Object> map = Maps.newHashMap();
            map.put("fromUserId",fromUserId);
            map.put("toGroupIds",toGroupIds);
            map.put("objectName",baseMessage.getObjectName());
            map.put("content",GsonUtil.toJson(baseMessage));
            map.put("isPersisted",1);
            map.put("contentAvailable",1);
            map.put("busChannel",channelUid);
            HttpURLConnection conn  = httpHelper
                    .createIMPostHttpConnection("/message/ultragroup/publish.json", "application/json");

            httpHelper.setBodyParameter(GsonUtil.toJson(map), conn);
            return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
