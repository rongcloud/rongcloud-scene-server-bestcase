package cn.rongcloud.common.sms.pojos;

/**
 * Created by sunyinglong on 2020/6/25
 */
public class SMSCodeVerifyResult {

    // 返回码，200 为正常。
    Integer code;
    // 错误信息。
    String errorMessage;

    boolean success;

    public SMSCodeVerifyResult(Integer code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 设置code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取code
     *
     * @return Integer
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 设置errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 获取errorMessage
     *
     * @return String
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}
