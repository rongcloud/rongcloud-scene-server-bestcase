package cn.rongcloud.mic.common.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PageParam implements Serializable {
    @ApiModelProperty("页数")
    private Integer pageNum = 1;

    @ApiModelProperty("分页大小")
    private Integer pageSize = 10;
}
