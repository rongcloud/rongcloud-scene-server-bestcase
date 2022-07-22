package cn.rongcloud.mic.community.message;

import cn.rongcloud.common.im.GroupHelper;
import cn.rongcloud.common.im.IMHelper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommunityMessageUtil {

    @Autowired
    private IMHelper imHelper;

    @Autowired
    private GroupHelper groupHelper;

    public void sendAuditCommunityMessage(String fromUserId,String toUserId,String fromUserName,String communitName,String communityUid){
        // 用户登录发送登录设备信息
        CommunityNoticeMessage noticeMessage = new CommunityNoticeMessage();
        noticeMessage.setMessage(String.format("%s 申请加入 %s 社区",fromUserName,communitName));
        noticeMessage.setCommunityUid(communityUid);
        noticeMessage.setFromUserId(fromUserId);
        noticeMessage.setType(0);
        String uid = "_SYSTEM_";
        try {
            imHelper.publishSysMessage(uid, Lists.newArrayList(toUserId),noticeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendJoinCommunityMessage(String toUserId,String communityUid,String communitName){
        // 用户登录发送登录设备信息
        CommunityNoticeMessage noticeMessage = new CommunityNoticeMessage();
        noticeMessage.setMessage(String.format("您已加入 %s 社区",communitName));
        noticeMessage.setCommunityUid(communityUid);
        noticeMessage.setType(1);
        String uid = "_SYSTEM_";
        try {
            imHelper.publishSysMessage(uid, Lists.newArrayList(toUserId),noticeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendQuitCommunityMessage(String toUserId,String communityUid,String communitName){
        // 用户登录发送登录设备信息
        CommunityNoticeMessage noticeMessage = new CommunityNoticeMessage();
        noticeMessage.setMessage(String.format("您已退出 %s 社区",communitName));
        noticeMessage.setCommunityUid(communityUid);
        noticeMessage.setType(2);
        String uid = "_SYSTEM_";
        try {
            imHelper.publishSysMessage(uid, Lists.newArrayList(toUserId),noticeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendQuitedCommunityMessage(String toUserId,String communityUid,String communitName){
        // 用户登录发送登录设备信息
        CommunityNoticeMessage noticeMessage = new CommunityNoticeMessage();
        noticeMessage.setMessage(String.format("您已被退出 %s 社区",communitName));
        noticeMessage.setCommunityUid(communityUid);
        noticeMessage.setType(3);
        String uid = "_SYSTEM_";
        try {
            imHelper.publishSysMessage(uid, Lists.newArrayList(toUserId),noticeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendShutupCommunityMessage(String toUserId,String communityUid,String communitName){
        // 用户登录发送登录设备信息
        CommunityNoticeMessage noticeMessage = new CommunityNoticeMessage();
        noticeMessage.setMessage(String.format("您已被 %s 禁言",communitName));
        noticeMessage.setCommunityUid(communityUid);
        noticeMessage.setType(4);
        String uid = "_SYSTEM_";
        try {
            imHelper.publishSysMessage(uid, Lists.newArrayList(toUserId),noticeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNoShutupCommunityMessage(String toUserId,String communityUid,String communitName){
        // 用户登录发送登录设备信息
        CommunityNoticeMessage noticeMessage = new CommunityNoticeMessage();
        noticeMessage.setMessage(String.format("您已被%s解除禁言",communitName));
        noticeMessage.setCommunityUid(communityUid);
        noticeMessage.setType(5);
        String uid = "_SYSTEM_";
        try {
            imHelper.publishSysMessage(uid, Lists.newArrayList(toUserId),noticeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCommunityNoticeMessage(String fromUserId, List<String> toUserIds, String communityUid, String message, Integer type){
        CommunityNoticeMessage noticeMessage = new CommunityNoticeMessage();
        noticeMessage.setMessage(message);
        noticeMessage.setFromUserId(fromUserId);
        noticeMessage.setCommunityUid(communityUid);
        noticeMessage.setType(type);
        String uid = "_SYSTEM_";
        try {
            imHelper.publishSysMessage(uid, toUserIds,noticeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUserUpdateMessage(String userId, List<String> communityUids, String portrait, String nickName, Integer type){
        UserUpdateMessage updateMessage = new UserUpdateMessage();
        updateMessage.setUserId(userId);
        updateMessage.setPortrait(portrait);
        updateMessage.setNickName(nickName);
        updateMessage.setType(type);
        String uid = "_SYSTEM_";
        try {
            groupHelper.sendCommunityMessage(uid, communityUids,updateMessage,"");
            //imHelper.publishSysMessage(uid, toUserIds,updateMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendChannelMarkMessage(String fromUserId, String communityUid,String message,String channnelUid,String messageUid,Integer type){
        ChannelNoticeMessage noticeMessage = new ChannelNoticeMessage();
        noticeMessage.setMessage(message);
        noticeMessage.setCommunityUid(communityUid);
        noticeMessage.setChannelUid(channnelUid);
        noticeMessage.setMessageUid(messageUid);
        noticeMessage.setFromUserId(fromUserId);
        noticeMessage.setType(type);
        String uid = "_SYSTEM_";
        try {
            groupHelper.sendCommunityMessage(uid, Lists.newArrayList(communityUid),noticeMessage,channnelUid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendChannelNoticeMessage(String fromUserId, String toUserId,String communityUid,String channelUid,String message,Integer type){
        ChannelNoticeMessage noticeMessage = new ChannelNoticeMessage();
        noticeMessage.setMessage(message);
        noticeMessage.setCommunityUid(communityUid);
        noticeMessage.setFromUserId(fromUserId);
        noticeMessage.setType(type);
        noticeMessage.setToUserId(toUserId);
        noticeMessage.setChannelUid(channelUid);
        String uid = "_SYSTEM_";
        try {
            groupHelper.sendCommunityMessage(uid, Lists.newArrayList(communityUid),noticeMessage,channelUid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void sendCommunityChangeMessage(String fromUserId, String communityUid,List<String> channelUids){
        CommunityChanngeMessage channgeMessage = new CommunityChanngeMessage();
        channgeMessage.setCommunityUid(communityUid);
        channgeMessage.setFromUserId(fromUserId);
        channgeMessage.setChannelUids(channelUids);
        channgeMessage.setMessage("频道被删除");
        String uid = "_SYSTEM_";
        groupHelper.sendCommunityMessage(uid,Lists.newArrayList(communityUid),channgeMessage,"");
    }

    public void sendCommunityDeleteMessage(String fromUserId, String communityUid){
        CommunityDeleteMessage deleteMessage = new CommunityDeleteMessage();
        deleteMessage.setCommunityUid(communityUid);
        deleteMessage.setFromUserId(fromUserId);
        String uid = "_SYS_";// 端上需要
        groupHelper.sendCommunityMessage(uid,Lists.newArrayList(communityUid),deleteMessage,"");
    }

}
