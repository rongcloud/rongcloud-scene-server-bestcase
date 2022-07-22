package cn.rongcloud.mic.community.mapper;


import cn.rongcloud.mic.community.dto.CommunityUserCount;
import cn.rongcloud.mic.community.entity.TCommunityUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户社区表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@Mapper
public interface TCommunityUserMapper extends BaseMapper<TCommunityUser> {

    List<CommunityUserCount> queryUserCountByCommunityUids(@Param("communityUids") List<String> communityUids);

    int updateOnlineStatusByUserId(@Param("userId") String userId, @Param("onlineStatus") Integer onlineStatus);
}
