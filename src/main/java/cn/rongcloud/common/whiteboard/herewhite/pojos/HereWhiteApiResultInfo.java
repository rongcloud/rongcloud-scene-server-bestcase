package cn.rongcloud.common.whiteboard.herewhite.pojos;

import lombok.Data;

/**
 * Created by weiqinxiao on 2019/3/7.
 */

/**
 * Created by weiqinxiao on 2019/3/7.
 */
@Data
public class HereWhiteApiResultInfo {
    private int code;
    private HereWhiteMsg msg;

    public boolean isSuccess() {
        return code == 200;
    }

    @Data
    public static class HereWhiteMsg {
        private HereWhiteRoom room;
        private String roomToken;
        private String code;

    }

    @Data
    public static class HereWhiteRoom {
        private String name;
        private String uuid;
    }
}
