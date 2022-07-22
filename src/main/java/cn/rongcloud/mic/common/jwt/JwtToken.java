package cn.rongcloud.mic.common.jwt;

import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Data
public class JwtToken {
    private String tokenId;
    private String userId;
    private String roomId;
    private String token;
    private long issuedTime;//UTC时间戳(ms)
    private long expiredTime;//UTC时间戳(ms)
}
