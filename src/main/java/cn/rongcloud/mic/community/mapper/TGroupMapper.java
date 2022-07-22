package cn.rongcloud.mic.community.mapper;


import cn.rongcloud.mic.community.entity.TGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分组表
 * 
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:04
 */
@Mapper
public interface TGroupMapper extends BaseMapper<TGroup> {

    void batchUpdate(@Param("list") List<TGroup> updateGroups);

    int maxSort(@Param("communityUid") String communityUid);
}
