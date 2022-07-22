package cn.rongcloud.mic.common.jwt;

import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Data
public class JwtUser {
    private String userId;
    private String userName;
    private String portrait;
    private Integer type;
}
