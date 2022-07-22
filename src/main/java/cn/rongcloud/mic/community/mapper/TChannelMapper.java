package cn.rongcloud.mic.community.mapper;


import cn.rongcloud.mic.community.entity.TChannel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 频道表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-16 15:50:03
 */
@Mapper
public interface TChannelMapper extends BaseMapper<TChannel> {

    void batchUpdate(@Param("list") List<TChannel> list);

    Integer maxSortByGroupUid(@Param("groupUid") String groupUid);

    Integer maxSortByCommuntiyUid(@Param("communityUid") String communityUid);
}
