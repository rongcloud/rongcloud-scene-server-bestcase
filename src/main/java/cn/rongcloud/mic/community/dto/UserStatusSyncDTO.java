package cn.rongcloud.mic.community.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserStatusSyncDTO implements Serializable {

    private String userid;

    private String status;

    private String os;

    private Long time;

    private String clientIp;

}
