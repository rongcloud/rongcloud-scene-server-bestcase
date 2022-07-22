package cn.rongcloud.common.whiteboard.rongcloud.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/04/20.
 */
@Data
public class WhiteboardInfo {

    @JsonProperty("whiteboardId")
    private String id;

    @JsonProperty("whiteboardRoomToken")
    private String roomToken;

    @JsonProperty("whiteboardType")
    private Integer type;
}
