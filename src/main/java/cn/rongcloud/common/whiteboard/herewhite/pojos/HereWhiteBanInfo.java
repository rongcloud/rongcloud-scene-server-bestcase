package cn.rongcloud.common.whiteboard.herewhite.pojos;

import lombok.Data;

@Data
public class HereWhiteBanInfo {

    private boolean ban; //true为禁用；false为恢复

    private String uuid; //白板唯一标识符
}
