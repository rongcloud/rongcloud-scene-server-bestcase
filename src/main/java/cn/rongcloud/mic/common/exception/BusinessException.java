package cn.rongcloud.mic.common.exception;

import java.util.Map;

/**
 *
 * @author zhouxingbin
 * @since 2021/09/26
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String code;
    private final String msg;
    private Map<String, Object> exceptionData;

    public BusinessException(String code, String msg) {
        super(msg, null);
        this.msg = msg;
        this.code = code;
    }

    public BusinessException(String code, String msg, Throwable cause) {
        super(msg, cause);
        this.msg = msg;
        this.code = code;
    }

    public BusinessException(String code, String msg, Map<String, Object> exceptionData) {
        super(msg, null);
        this.msg = msg;
        this.code = code;
        this.exceptionData = exceptionData;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public Map<String, Object> getExceptionData() {
        return exceptionData;
    }

    public void setExceptionData(Map<String, Object> exceptionData) {
        this.exceptionData = exceptionData;
    }
}

