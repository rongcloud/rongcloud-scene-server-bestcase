package cn.rongcloud.mic.game.entity;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author wangyaoyu
 * @create 2022/7/4 11:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerResult {

    /**
     * 接入方uid
     */
    private String uid;

    /**
     * 	排名从1开始，平局排名相同
     */
    private Integer rank;

    /**
     * 0:正常，1:逃跑
     */
    private Integer is_escaped;

    /**
     * 0:普通用户，1:机器人
     */
    private Integer is_ai;

    /**
     * 玩家在游戏中角色
     */
    private Integer role;

    /**
     * 玩家当前局得到的分数
     */
    private Integer score;

    /**
     * 结果 0:表示没有信息，1:输，2:赢，3:平局
     */
    private Integer is_win;

}
