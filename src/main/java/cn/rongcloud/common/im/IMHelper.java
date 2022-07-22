package cn.rongcloud.common.im;

import cn.rongcloud.common.im.pojos.*;
import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.common.utils.HttpHelper;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Slf4j
@Component
public class IMHelper {
    private static final String UTF8 = "UTF-8";

    @Autowired
    HttpHelper httpHelper;

    /**
     * 获取 Token 方法
     *
     * @param userId:用户 Id，最大长度 64 字节.是用户在 App 中的唯一标识码，必须保证在同一个 App 内不重复，重复的用户 Id 将被当作是同一用户。（必传）
     * @param name:用户名称，最大长度 128 字节.用来在 Push 推送时显示用户的名称.用户名称，最大长度 128 字节.用来在 Push 推送时显示用户的名称。（必传）
     * @param portraitUri:用户头像 URI，最大长度 1024 字节.用来在 Push 推送时显示用户的头像。（必传）
     * @return TokenResult
     **/
    public IMTokenInfo getToken(String userId, String name, String portraitUri) throws Exception {
        if (userId == null) {
            throw new IllegalArgumentException("Paramer 'userId' is required");
        }

        if (name == null) {
            throw new IllegalArgumentException("Paramer 'name' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&userId=").append(URLEncoder.encode(userId, UTF8));
        sb.append("&name=").append(URLEncoder.encode(name, UTF8));
        sb.append("&portraitUri=").append(URLEncoder.encode(portraitUri, UTF8));
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/user/getToken.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMTokenInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMTokenInfo.class);
    }

    /**
     * 刷新用户信息
     *
     * @param userId:用户 Id，最大长度 64 字节.是用户在 App 中的唯一标识码，必须保证在同一个 App 内不重复，重复的用户 Id 将被当作是同一用户。（必传）
     * @param name:用户名称，最大长度 128 字节.用来在 Push 推送时显示用户的名称.用户名称，最大长度 128 字节.用来在 Push 推送时显示用户的名称。（必传）
     * @param portraitUri:用户头像 URI，最大长度 1024 字节.用来在 Push 推送时显示用户的头像。（必传）
     * @return TokenResult
     **/
    public IMApiResultInfo refreshUser(String userId, String name, String portraitUri) throws Exception {
        if (userId == null) {
            throw new IllegalArgumentException("Paramer 'userId' is required");
        }

        if (name == null) {
            throw new IllegalArgumentException("Paramer 'name' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&userId=").append(URLEncoder.encode(userId, UTF8));
        sb.append("&name=").append(URLEncoder.encode(name, UTF8));
        sb.append("&portraitUri=").append(URLEncoder.encode(portraitUri, UTF8));
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/user/refresh.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
    }


    /**
     * 创建聊天室
     * @param chatRoomId: 要创建的聊天室 Id，长度不超过 64 字节
     * @param chatRoomName: 聊天室的名称，每次可创建多个聊天室。
     * @return IMApiResultInfo
     * @throws Exception
     */
    public IMApiResultInfo createChatRoom(String chatRoomId, String chatRoomName)
        throws Exception {
        if (chatRoomId == null) {
            throw new IllegalArgumentException("Paramer 'userId' is required");
        }

        if (chatRoomName == null) {
            throw new IllegalArgumentException("Paramer 'chatroomName' is required");
        }

        StringBuilder sb = new StringBuilder();
        chatRoomId = "[" + chatRoomId + "]";
        sb.append("&chatroom").append(URLEncoder.encode(chatRoomId, UTF8));
        sb.append("=");
        sb.append(URLEncoder.encode(chatRoomName, UTF8));
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper
            .createIMPostHttpConnection("/chatroom/create.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
    }

    /**
     * 发送聊天室消息
     * @param fromUserId
     * @param toChatroomId
     * @param message
     * @return
     * @throws Exception
     */
    public IMApiResultInfo publishMessage(String fromUserId, String toChatroomId, BaseMessage message) throws Exception {
        String[] toChatroomIds = new String[1];
        toChatroomIds[0] = toChatroomId;
        return publishMessage(fromUserId, toChatroomIds, message);
    }

    public IMApiResultInfo publishMessage(String fromUserId, String[] toChatroomId,
        BaseMessage message)
        throws Exception {
        if (fromUserId == null) {
            throw new IllegalArgumentException("Paramer 'fromUserId' is required");
        }

        if (toChatroomId == null) {
            throw new IllegalArgumentException("Paramer 'toChatroomId' is required");
        }

        if (message == null) {
            throw new IllegalArgumentException("Paramer 'message' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));

        for (String child : toChatroomId) {
            sb.append("&toChatroomId=").append(URLEncoder.encode(child, UTF8));
        }

        String msgStr = GsonUtil.toJson(message);
        log.info("publish msg: {}", msgStr);
        sb.append("&objectName=").append(URLEncoder.encode(message.getObjectName(), UTF8));
        sb.append("&content=").append(URLEncoder.encode(msgStr, UTF8));

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper
            .createIMPostHttpConnection("/message/chatroom/publish.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
    }

    /**
     * 发送聊天室广播消息（给所有聊天室发送）单个应用每秒最多调用 1 次。
     * @param fromUserId
     * @param objectName
     * @param content
     * @return
     * @throws Exception
     */
    public IMApiResultInfo publishBroadcastMessage(String fromUserId, String objectName, String content)
        throws Exception {
        if (fromUserId == null) {
            throw new IllegalArgumentException("Paramer 'fromUserId' is required");
        }
        if (objectName == null) {
            throw new IllegalArgumentException("Paramer 'objectName' is required");
        }
        if (content == null) {
            throw new IllegalArgumentException("Paramer 'content' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));

        sb.append("&objectName=").append(URLEncoder.encode(objectName, UTF8));
        sb.append("&content=").append(URLEncoder.encode(content, UTF8));

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper
            .createIMPostHttpConnection("/message/chatroom/broadcast.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
    }

    /**
     * 聊天室获取属性
     * @return IMApiResultInfo
     */
    public ImChatroomEntryResultInfo entrySetQuery(String chatRoomId)
            throws Exception {

        if (StringUtils.isBlank(chatRoomId)) {
            throw new IllegalArgumentException("Paramer 'chatRoomId' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(chatRoomId, UTF8));

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/entry/query.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);
        ImChatroomEntryResultInfo result = (ImChatroomEntryResultInfo) GsonUtil
                .fromJson(httpHelper.returnResult(conn), ImChatroomEntryResultInfo.class);
        return result;
    }

    /**
     * 聊天室设置属性
     * @param retryTimes: 失败之后，重试次数
     * @return IMApiResultInfo
     */
    public IMApiResultInfo entrySet(ChrmEntrySetInfo entrySetInfo, int retryTimes)
        throws Exception {

        if (entrySetInfo.getChrmId() == null) {
            throw new IllegalArgumentException("Paramer 'chatRoomId' is required");
        }

        if (entrySetInfo.getUserId() == null) {
            throw new IllegalArgumentException("Paramer 'userId' is required");
        }

        if (entrySetInfo.getKey() == null) {
            throw new IllegalArgumentException("Paramer 'key' is required");
        }

        if (entrySetInfo.getValue() == null) {
            throw new IllegalArgumentException("Paramer 'value' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(entrySetInfo.getChrmId(), UTF8));
        sb.append("&userId=").append(URLEncoder.encode(entrySetInfo.getUserId(), UTF8));
        sb.append("&key=").append(URLEncoder.encode(entrySetInfo.getKey(), UTF8));
        sb.append("&value=").append(URLEncoder.encode(entrySetInfo.getValue(), UTF8));
        if (entrySetInfo.getAutoDelete() != null) {
            sb.append("&autoDelete=").append(URLEncoder.encode(entrySetInfo.getAutoDelete().toString(), UTF8));
        }
        if (entrySetInfo.getObjectName() != null) {
            sb.append("&objectName=").append(URLEncoder.encode(entrySetInfo.getObjectName(), UTF8));
        }
        if (entrySetInfo.getContent() != null) {
            sb.append("&content=").append(URLEncoder.encode(entrySetInfo.getContent(), UTF8));
        }
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/entry/set.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);
        int resCode = conn.getResponseCode();
        IMApiResultInfo result = (IMApiResultInfo) GsonUtil
            .fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
        if(result.getCode()==200){
            return result;
        }
        if (retryTimes>0) {
            log.info("set chartRoom entry failed, code,{}, entrySetInfo:{}, retryTimes:{}", resCode, GsonUtil.toJson(entrySetInfo), retryTimes);
            //延迟 100ms 重试
            Thread.sleep(100);
            entrySet(entrySetInfo, --retryTimes);
        }
        return result;
    }


    /**
     * 获取聊天室成员信息
     * @param chatroomId 要查询的聊天室 ID
     * @param count 要获取的聊天室成员信息数，最多返回 500 个成员信息
     * @param order 加入聊天室的先后顺序， 1 为加入时间正序， 2 为加入时间倒序
     * @return
     */
    public IMChatRoomUserResult queryChatroomUser(String chatroomId, Integer count, Integer order)
            throws Exception {
        if (chatroomId == null) {
            throw new IllegalArgumentException("Paramer 'chatroomId' is required");
        }
        if (count == null) {
            count = 50;
        }
        if (order == null) {
            order = 1;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(chatroomId, UTF8));
        sb.append("&count=").append(URLEncoder.encode(count + "", UTF8));
        sb.append("&order=").append(URLEncoder.encode(order + "", UTF8));
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }
        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/user/query.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMChatRoomUserResult) GsonUtil
                .fromJson(httpHelper.returnResult(conn), IMChatRoomUserResult.class);
    }

    /**
     * 查询在线状态
     * @return
     */
    public IMUserStatusResultInfo queryUserStatus(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Paramer 'userId' is required");
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("&userId=").append(URLEncoder.encode(userId, UTF8));
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }
            HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/user/checkOnline.json", "application/x-www-form-urlencoded");
            httpHelper.setBodyParameter(body, conn);
            return (IMUserStatusResultInfo) GsonUtil
                    .fromJson(httpHelper.returnResult(conn), IMUserStatusResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 聊天室添加禁言成员
     * @param chatroomId 聊天室 Id
     * @param userIds 用户 Id，可同时禁言多个用户
     * @param minute 禁言时长，以分钟为单位，最大值为 43200 分钟
     * @return
     */
    public IMApiResultInfo addGagChatroomUser(String chatroomId, List<String> userIds, Integer minute)
        throws Exception {
        if (chatroomId == null) {
            throw new IllegalArgumentException("Paramer 'chatRoomId' is required");
        }

        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException("Paramer 'userId' is required");
        }
        if (userIds.size() > 20) {
            throw new IllegalArgumentException("Paramer 'userIds' is required");
        }

        if (minute == null) {
            minute = 43200;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(chatroomId, UTF8));
        for (String child : userIds) {
            sb.append("&userId=").append(URLEncoder.encode(child, UTF8));
        }
        sb.append("&minute=").append(minute);

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/user/gag/add.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
    }

    /**
     * 聊天室移除禁言成员
     * @param chatroomId
     * @param userIds
     * @return
     * @throws Exception
     */
    public IMApiResultInfo removeGagChatroomUser(String chatroomId, List<String> userIds)
        throws Exception {
        if (chatroomId == null) {
            throw new IllegalArgumentException("Paramer 'chatRoomId' is required");
        }

        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException("Paramer 'userId' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(chatroomId, UTF8));
        for (String child : userIds) {
            sb.append("&userId=").append(URLEncoder.encode(child, UTF8));
        }

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/user/gag/rollback.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
    }

    /**
     * 查询聊天室禁言用户
     * @param chatroomId 聊天室ID
     * @return
     */
    public IMGagChatRoomUserResult queryGagChatroomUser(String chatroomId)
        throws Exception {
        if (chatroomId == null) {
            throw new IllegalArgumentException("Paramer 'chatRoomId' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(chatroomId, UTF8));

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/user/gag/list.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMGagChatRoomUserResult) GsonUtil
            .fromJson(httpHelper.returnResult(conn), IMGagChatRoomUserResult.class);
    }

    /**
     * 聊天室添加封禁成员
     * @param chatroomId 聊天室 Id
     * @param userIds 用户 Id，可同时封禁多个用户，最多不超过 20 个。（必传）
     * @param minute 封禁时长，以分钟为单位，最大值为 43200 分钟
     * @return
     */
    public IMApiResultInfo addBlockChatroomUser(String chatroomId, List<String> userIds, Integer minute)
        throws Exception {
        if (chatroomId == null) {
            throw new IllegalArgumentException("Paramer 'chatRoomId' is required");
        }

        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException("Paramer 'userId' is required");
        }
        if (userIds.size() > 20) {
            throw new IllegalArgumentException("Paramer 'userIds' is required");
        }

        if (minute == null) {
            minute = 43200;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(chatroomId, UTF8));
        for (String child : userIds) {
            sb.append("&userId=").append(URLEncoder.encode(child, UTF8));
        }
        sb.append("&minute=").append(minute);

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/user/block/add.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
    }

    /**
     * 查询用户是否在聊天室
     * @param chatroomId 要查询的聊天室 ID（必传）
     * @param userId 要查询的用户 ID（必传）
     * @return
     */
    public IMIsInChrmResult isInChartRoom(String chatroomId, String userId)
        throws Exception {
        if (chatroomId == null) {
            throw new IllegalArgumentException("Paramer 'chatRoomId' is required");
        }

        if (userId == null) {
            throw new IllegalArgumentException("Paramer 'userId' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&chatroomId=").append(URLEncoder.encode(chatroomId, UTF8));
        sb.append("&userId=").append(URLEncoder.encode(userId, UTF8));

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/user/exist.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMIsInChrmResult) GsonUtil.fromJson(httpHelper.returnResult(conn), IMIsInChrmResult.class);

    }

    /**
     * 销毁聊天室
     * @param chatroomIds 聊天室 ID 列表（必传）
     * @return
     */
    public IMApiResultInfo deleteChrm(List<String> chatroomIds)
            throws Exception {
        if (chatroomIds == null) {
            throw new IllegalArgumentException("Paramer 'chatroomIds' is required");
        }



        StringBuilder sb = new StringBuilder();
        for (String child : chatroomIds) {
            sb.append("&chatroomId=").append(URLEncoder.encode(child, UTF8));
        }
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/destroy.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);

    }

    public IMApiResultInfo publishSysMessage(String fromUserId, List<String> targetUserIds, BaseMessage message)
        throws Exception {
        return publishSystemMessage(fromUserId, targetUserIds, message, null, null, 1, 1);
    }

    /**
     * 发送系统通知消息
     * @param fromUserId 发送人用户 Id。
     * @param targetUserIds 接收用户Id，提供多个本参数可以实现向多用户发送系统消息，上限为 100 人。
     * @param pushContent 定义显示的 Push 内容，如果 objectName 为融云内置消息类型时，则发送后用户一定会收到 Push 信息。 如果为自定义消息，则 pushContent 为自定义消息显示的 Push 内容，如果不传则用户不会收到 Push 通知。
     * @param pushData 针对 iOS 平台为 Push 通知时附加到 payload 中，客户端获取远程推送内容时为 appData，同时融云默认携带了消息基本信息，客户端可通过 'rc' 属性获取，查看详细，Android 客户端收到推送消息时对应字段名为 pushData。
     * @param isPersisted 针对融云服务端历史消息中是否存储此条消息，客户端则根据消息注册的 ISPERSISTED 标识判断是否存储，如果旧版客户端上未注册该消息时，收到该消息后默认为存储，但无法解析显示。0 表示为不存储、 1 表示为存储，默认为 1 存储消息，此属性不影响离线消息功能，用户未在线时都会转为离线消息存储。
     * @param contentAvailable 针对 iOS 平台，对 SDK 处于后台暂停状态时为静默推送，是 iOS7 之后推出的一种推送方式。 允许应用在收到通知后在后台运行一段代码，且能够马上执行，查看详细。1 表示为开启，0 表示为关闭，默认为 0
     * #返回结果
     * @return
     * @throws Exception
     */
    public IMApiResultInfo publishSystemMessage(String fromUserId, List<String> targetUserIds, BaseMessage message, String pushContent, String pushData,
        Integer isPersisted, Integer contentAvailable) throws Exception {

        if (fromUserId == null) {
            throw new IllegalArgumentException("Paramer 'fromUserId' is required");
        }

        if (targetUserIds == null || targetUserIds.isEmpty() || targetUserIds.size() > 100) {
            throw new IllegalArgumentException("Paramer 'toUserIds' is required");
        }

        if (message == null) {
            throw new IllegalArgumentException("Paramer 'message' is required");
        }


        StringBuilder sb = new StringBuilder();
        sb.append("&fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));

        for (String child : targetUserIds) {
            if (null != child) {
                sb.append("&toUserId=").append(URLEncoder.encode(child, UTF8));
            }
        }

        sb.append("&objectName=").append(URLEncoder.encode(message.getObjectName(), UTF8));
        String content = GsonUtil.toJson(message);
        sb.append("&content=").append(URLEncoder.encode(content, UTF8));

        if (pushContent != null) {
            sb.append("&pushContent=").append(URLEncoder.encode(pushContent, UTF8));
        }

        if (pushData != null) {
            sb.append("&pushData=").append(URLEncoder.encode(pushData, UTF8));
        }

        if (isPersisted != null) {
            sb.append("&isPersisted=").append(URLEncoder.encode(isPersisted.toString(), UTF8));
        }

        if (contentAvailable != null) {
            sb.append("&contentAvailable=").append(URLEncoder.encode(contentAvailable.toString(), UTF8));
        }
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/message/system/publish.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
    }
    /**
     * 状态消息发送
     * @param fromUserId 发送人用户 Id。
     * @param targetUserIds 接收用户Id，提供多个本参数可以实现向多用户发送系统消息，上限为 100 人。
     * @param message 定义显示的 Push 内容，如果 objectName 为融云内置消息类型时，则发送后用户一定会收到 Push 信息。 如果为自定义消息，则 pushContent 为自定义消息显示的 Push 内容，如果不传则用户不会收到 Push 通知。
     * @param verifyBlacklist 是否过滤发送人黑名单列表，0 表示为不过滤、 1 表示为过滤，默认为 0 不过滤。
     * @param isIncludeSender 发送用户自己是否接收消息，0 表示为不接收，1 表示为接收，默认为 0 不接收。
     * #返回结果
     * @return
     * @throws Exception
     */
    public IMApiResultInfo publishPrivateStatusMessage(String fromUserId, List<String> targetUserIds, BaseMessage message,
                                                Integer verifyBlacklist, Integer isIncludeSender) throws Exception {

        if (fromUserId == null) {
            throw new IllegalArgumentException("Paramer 'fromUserId' is required");
        }

        if (targetUserIds == null || targetUserIds.isEmpty() || targetUserIds.size() > 100) {
            throw new IllegalArgumentException("Paramer 'toUserIds' is required");
        }

        if (message == null) {
            throw new IllegalArgumentException("Paramer 'message' is required");
        }


        StringBuilder sb = new StringBuilder();
        sb.append("&fromUserId=").append(URLEncoder.encode(fromUserId, UTF8));

        for (String child : targetUserIds) {
            if (null != child) {
                sb.append("&toUserId=").append(URLEncoder.encode(child, UTF8));
            }
        }

        sb.append("&objectName=").append(URLEncoder.encode(message.getObjectName(), UTF8));
        String content = GsonUtil.toJson(message);
        sb.append("&content=").append(URLEncoder.encode(content, UTF8));

        if (verifyBlacklist != null) {
            sb.append("&verifyBlacklist=").append(URLEncoder.encode(verifyBlacklist.toString(), UTF8));
        }

        if (isIncludeSender != null) {
            sb.append("&isIncludeSender=").append(URLEncoder.encode(isIncludeSender.toString(), UTF8));
        }
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/statusmessage/private/publish.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
    }
    /**
     * 添加聊天室消息类型白名单，支持添加多个，最多不超过 20 个消息类型。
     * @param objectNames
     * @return
     * @throws Exception
     */
    public IMApiResultInfo chrmWhitelistAdd(List<String> objectNames) throws Exception {
        if (objectNames == null || objectNames.isEmpty() || objectNames.size() > 20) {
            throw new IllegalArgumentException("Paramer 'objectNames' is invalid");
        }

        StringBuilder sb = new StringBuilder();
        for (String child : objectNames) {
            if (null != child) {
                sb.append("&objectnames=").append(URLEncoder.encode(child, UTF8));
            }
        }

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/whitelist/add.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
    }


    /**
     * 移除聊天室消息类型白名单，支持添加多个，最多不超过 20 个消息类型。
     * @param objectNames
     * @return
     * @throws Exception
     */
    public IMApiResultInfo chrmWhitelistDelete(List<String> objectNames) throws Exception {

        if (objectNames == null || objectNames.isEmpty() || objectNames.size() > 20) {
            throw new IllegalArgumentException("Paramer 'objectNames' is invalid");
        }

        StringBuilder sb = new StringBuilder();
        for (String child : objectNames) {
            if (null != child) {
                sb.append("&objectnames=").append(URLEncoder.encode(child, UTF8));
            }
        }

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/whitelist/delete.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (IMApiResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMApiResultInfo.class);
    }

    public IMUserExistResultInfo userExist(String roomId,String userId){
        try {
            if (roomId == null) {
                throw new IllegalArgumentException("Paramer 'roomId' is required");
            }

            if (userId == null) {
                throw new IllegalArgumentException("Paramer 'userId' is required");
            }


            StringBuilder sb = new StringBuilder();
            sb.append("&chatroomId=").append(URLEncoder.encode(roomId, UTF8));
            sb.append("&userId=").append(URLEncoder.encode(userId, UTF8));

            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }

            HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/user/exist.json", "application/x-www-form-urlencoded");
            httpHelper.setBodyParameter(body, conn);
            return (IMUserExistResultInfo) GsonUtil.fromJson(httpHelper.returnResult(conn), IMUserExistResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询聊天室消息类型白名单。
     * @return
     * @throws Exception
     */
    public IMChrmWhiteListResult chrmWhitelistQuery() throws Exception {

        HttpURLConnection conn = httpHelper.createIMPostHttpConnection("/chatroom/whitelist/query.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter("", conn);

        return (IMChrmWhiteListResult) GsonUtil
            .fromJson(httpHelper.returnResult(conn), IMChrmWhiteListResult.class);
    }

    /**
     * 获取 Media Token 方法
     *
     * @param bucketName 分桶，默认为message
     * @return TokenResult
     **/
    public RCXMediaTokenResult getToken(String bucketName) throws Exception {
        HttpURLConnection conn = httpHelper.createRCXMediaGetHttpConnection(
                "/getToken?namespace=" + (bucketName != null ? bucketName : "message"),
                "application/x-www-form-urlencoded");
        return (RCXMediaTokenResult) GsonUtil.fromJson(httpHelper.returnResult(conn), RCXMediaTokenResult.class);
    }


}
