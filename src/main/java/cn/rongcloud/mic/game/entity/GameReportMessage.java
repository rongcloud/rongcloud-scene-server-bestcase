package cn.rongcloud.mic.game.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangyaoyu
 * @create 2022/7/4 11:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameReportMessage {

    /**
     * 游戏id
     */
    private Long mg_id;

    /**
     * 小游戏id数值型兼容字段
     */
    private String mg_id_str;

    /**
     * 接入房房间id
     */
    private String room_id;

    /**
     * 游戏模式
     */
    private Integer game_mode;

    /**
     * 本局游戏的id
     */
    private String game_round_id;

    /**
     * 战斗开始时间
     */
    private Integer battle_start_at;

    /**
     * 战斗结束时间
     */
    private Integer battle_end_at;

    /**
     * 战斗总时间
     */
    private Integer battle_duration;

    /**
     * 游戏上报信息扩展参数（透传
     */
    private String report_game_info_extras;

    /**
     * 游戏用户
     */
    private List<PlayerResult> players;

    /**
     * 游戏结果
     */
    private List<PlayerResult> results;


}
