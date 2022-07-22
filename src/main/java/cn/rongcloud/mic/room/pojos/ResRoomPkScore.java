package cn.rongcloud.mic.room.pojos;

import cn.rongcloud.common.im.BaseMessage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ResRoomPkScore extends BaseMessage {

    @ApiModelProperty("pk 状态,0:PK中,1:惩罚中，2:PK结束")
    private int statusMsg;

    // 如果是pk 阶段，那么代表当前时间减去pk开始实际那，如果是惩罚阶段，那么是当前时间-惩罚开始时间
    @ApiModelProperty("若PK中代表PK时间,若是惩罚阶段，达标惩罚时间，毫秒")
    private Long timeDiff;

    @ApiModelProperty(value = "房间积分情况",name = "roomScores")
    private List<ResRoomScore> roomScores;

    @Override
    public String getObjectName() {
        return "RCMic:chrmPkNewMsg";
    }

}
