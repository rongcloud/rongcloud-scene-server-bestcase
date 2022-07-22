package cn.rongcloud.common.whiteboard.herewhite.pojos;

import lombok.Data;

@Data
public class HereWhiteCreateInfo {

    private String name; //白板名称

    private Integer limit; //设置为 0 是不限制，推荐使用设置为 0：房间不限制，从业务上去限制。

    private String model; //v2版本参数；房间类型：persistent,historied
}
