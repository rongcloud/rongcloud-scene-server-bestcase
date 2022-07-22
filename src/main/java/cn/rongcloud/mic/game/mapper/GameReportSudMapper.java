package cn.rongcloud.mic.game.mapper;

import cn.rongcloud.mic.game.entity.GameReportInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author wangyaoyu
 * @create 2022/7/4 11:03
 */
public interface GameReportSudMapper extends BaseMapper<GameReportInfo> {

    void add(GameReportInfo reqParam);
}
