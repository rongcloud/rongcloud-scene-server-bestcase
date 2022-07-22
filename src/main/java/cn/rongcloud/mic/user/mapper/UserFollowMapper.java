package cn.rongcloud.mic.user.mapper;

import cn.rongcloud.mic.user.model.TUserFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface UserFollowMapper extends BaseMapper<TUserFollow> {

    void deleteByUserId(@Param("userId") String userId);

}
