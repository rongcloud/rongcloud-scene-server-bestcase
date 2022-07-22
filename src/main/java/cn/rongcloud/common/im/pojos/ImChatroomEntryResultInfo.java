package cn.rongcloud.common.im.pojos;

import cn.rongcloud.common.im.ChrmEntrySetInfo;
import lombok.Data;

import java.util.List;

@Data
public class ImChatroomEntryResultInfo {

    Integer code;

    List<ChrmEntrySetInfo> keys;

}
