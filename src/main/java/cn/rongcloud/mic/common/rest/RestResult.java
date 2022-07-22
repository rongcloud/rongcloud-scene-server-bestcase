package cn.rongcloud.mic.common.rest;

import cn.rongcloud.common.utils.GsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "公共返回对象")
public class RestResult<T> implements Serializable {

    @ApiModelProperty("状态码")
    private int code;

    @JsonInclude(Include.NON_NULL)
    @ApiModelProperty("失败信息")
    private String msg;

    @JsonProperty("data")
    private T result = null;

    public static<T> RestResult<T> success() {
        RestResult<T> restResult =new RestResult<T>();
        restResult.setResult(null);
        restResult.setCode(RestResultCode.ERR_SUCCESS.getCode());
        return restResult;
    }

    public static<T> RestResult<T> success(T o) {
        RestResult<T> restResult =new RestResult<T>();
        restResult.setResult(o);
        restResult.setCode(RestResultCode.ERR_SUCCESS.getCode());
        return restResult;
    }

    public static<T> RestResult<T> generic(RestResultCode code) {
        RestResult<T> restResult =new RestResult<T>();
        restResult.setResult(null);
        restResult.setCode(code.getCode());
        restResult.setMsg(code.getMsg());
        return restResult;
    }

    public static<T> RestResult<T> generic(RestResultCode code, String msg) {
        RestResult<T> restResult =new RestResult<T>();
        restResult.setResult(null);
        restResult.setCode(code.getCode());
        restResult.setMsg(msg);
        return restResult;
    }

    public static<T> RestResult<T> generic(RestResultCode code, T object) {
        RestResult<T> restResult =new RestResult<T>();
        restResult.setResult(object);
        restResult.setCode(code.getCode());
        if (code != RestResultCode.ERR_SUCCESS) {
            restResult.setMsg(code.getMsg());
        }
        return restResult;
    }

    public static <T> RestResult<T> genericObj(RestResultCode code, T object) {
        RestResult<T> restResult =new RestResult<T>();
        restResult.setCode(code.getCode());
        restResult.setResult(object);
        if (code != RestResultCode.ERR_SUCCESS) {
            restResult.setMsg(code.getMsg());
        }
        return restResult;

    }

    public static<T> RestResult<T> generic(int code, String msg) {
        RestResult<T> restResult =new RestResult<T>();
        restResult.setCode(code);
        restResult.setMsg(msg);
        return restResult;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == RestResultCode.ERR_SUCCESS.getCode();
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this, RestResult.class);
    }
}
