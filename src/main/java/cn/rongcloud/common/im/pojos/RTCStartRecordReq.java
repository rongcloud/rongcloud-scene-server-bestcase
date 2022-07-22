package cn.rongcloud.common.im.pojos;

import lombok.Data;

@Data
public class RTCStartRecordReq {

    private String sessionId;
    private RecordConfig config;

    @Data
    public static class RecordConfig {
        private Integer model; // 录像模式，可选项：0：音视频 single 模式；1：纯视频 single 模式；2：纯音频 single 模式；3：音视频 mix 模式；4：纯视频 mix 模式；5：纯音频 mix 模式；6：单人 mix 音视频模式
        private String videoFormat; //视频格式，可选项：mkv mp4 flv
        private String audioFormat; //音频格式，可选项：aac mp3
        private String videoResolution; //分辨率，可选项：480x320 320x480 640x480 480x640 1280x720 720x1280
        private Integer mixLayout; //mix 模式下的视频合流布局方式，可选项：1：自定义布局 2：悬浮布局 3：自适应布局，默认值为悬浮布局。
        private Integer sliceMin; //录像切片时长，单位为分钟。可选项：30, 60, 90, 120
        private Integer renderMode; //视频的填充方式，可选项：1：等比放大填充（相对视频源会有裁剪）2：等比缩放（可能会有黑边）默认值为等比缩放。
        private String hostUserId; //主画面的用户 ID。悬浮布局时使用该用户的视频渲染主背景。自适应布局时使用该用户的视频渲染左上角的画面；自定义布局时可不传。
        private String hostStreamId; //主画面的流 ID。悬浮布局时使用该视频流渲染主背景；自适应布局时使用该视频流渲染左上角的画面；自定义布局时可不传。
    }
}
