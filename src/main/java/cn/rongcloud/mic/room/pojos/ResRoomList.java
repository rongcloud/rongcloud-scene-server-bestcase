package cn.rongcloud.mic.room.pojos;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by sunyinglong on 2020/6/3
 */
@Data
public class ResRoomList implements Serializable {

    @ApiModelProperty("总数")
    private Long totalCount;

    @ApiModelProperty("房间列表")
    private List<ResRoomInfo> rooms;

    @ApiModelProperty("图片列表")
    private List<String> images;

}
