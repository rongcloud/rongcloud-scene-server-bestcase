package cn.rongcloud.mic.user.pojos;

import lombok.Data;

/**
 * Created by sunyinglong on 2020/5/25.
 */
@Data
public class ReqLoginWX {

    private String openId;
    private String encryptedData;
    private String iv;
}
