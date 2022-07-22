package cn.rongcloud.mic.game.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基础响应体
 *
 * @author Sud
 * @param <T>
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BaseResp<T> {

    /**
     * 响应码
     */
    private int ret_code;

    /**
     * 响应消息
     */
    private String ret_msg;

    /**
     * 业务响应数据
     */
    private T data;

    /**
     * sdk错误码
     */
    private Integer sdk_error_code;


    public void setRetCode(RetCodeEnum retCode) {
        this.ret_code = retCode.getIndex();
        this.ret_msg = retCode.getName();
    }

}
