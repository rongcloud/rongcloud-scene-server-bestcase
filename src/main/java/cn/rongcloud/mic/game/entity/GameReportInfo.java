package cn.rongcloud.mic.game.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author wangyaoyu
 * @create 2022/7/4 11:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_game_report_sud")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameReportInfo {

    /**
     * 请求类型：game_start 战斗开始通知  game_settle 战斗结算通知
     */
    private String report_type;

    /**
     * 队长用户id
     */
    private String uid;

    /**
     * 队长用户ss_token
     */
    private String ss_token;

    /**
     * 上报数据对象
     */
    private GameReportMessage report_msg;

}
