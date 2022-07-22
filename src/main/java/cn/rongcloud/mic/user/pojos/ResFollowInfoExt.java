package cn.rongcloud.mic.user.pojos;

import lombok.Data;

@Data
public class ResFollowInfoExt {

    private String userId;

    private String userName;

    private String portrait;

    private Integer status;

    private boolean isAdmin;
}
