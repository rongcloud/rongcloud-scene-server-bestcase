package cn.rongcloud.common.im;


import cn.rongcloud.common.utils.GsonUtil;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by sunyinglong on 2020/6/25
 */
public abstract class BaseMessage {

    @ApiIgnore
    public abstract String getObjectName();

    @Override
    public String toString() {
        return GsonUtil.toJson(this);
    }
}
