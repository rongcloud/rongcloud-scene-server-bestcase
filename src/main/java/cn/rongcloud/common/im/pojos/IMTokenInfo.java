package cn.rongcloud.common.im.pojos;

import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Data
public class IMTokenInfo {
    // 返回码，200 为正常.如果您正在使用开发环境的 AppKey，您的应用只能注册 100 名用户，达到上限后，将返回错误码 2007.如果您需要更多的测试账户数量，您需要在应用配置中申请“增加测试人数”。
    Integer code;
    // 用户 Token，可以保存应用内，长度在 256 字节以内.用户 Token，可以保存应用内，长度在 256 字节以内。
    String token;
    // 用户 Id，与输入的用户 Id 相同.用户 Id，与输入的用户 Id 相同。
    String userId;
    // 错误信息。
    String errorMessage;

    public boolean isSuccess() {
        return code == 200;
    }
}
