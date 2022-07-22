package cn.rongcloud.mic.room.pojos;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResSensitive {

    @ApiModelProperty("敏感词id")
    private Long id;

    @NotBlank(message = "roomId should not be blank")
    @ApiModelProperty("房间id")
    private String roomId;

    @NotBlank(message = "name should not be blank")
    @ApiModelProperty("敏感词")
    private String name;
}
