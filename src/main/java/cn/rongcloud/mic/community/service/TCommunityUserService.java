package cn.rongcloud.mic.community.service;


import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.page.PageParam;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.community.dto.*;
import cn.rongcloud.mic.community.entity.TCommunityUser;
import cn.rongcloud.mic.community.vo.TCommunityVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Set;


/**
 * 用户社区表
 *
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
public interface TCommunityUserService {

    RestResult update(TCommunityUserUpdateReq params, JwtUser jwtUser);

    RestResult<IPage<TCommunityUserResp>> queryPage(TCommunityUserReq params, JwtUser jwtUser);

    RestResult<IPage<TCommunityVO>> pageCommunity(PageParam pageParam, JwtUser jwtUser);

    RestResult<Integer> join(String communityUid, JwtUser jwtUser);

    void deleteByCommunityUid(String communityUid);

    RestResult updateChannel(TChannelUserUpdateReq params, JwtUser jwtUser);

    List<TCommunityUser> queryByUserIdsAndCommunityUid(Set<String> userIds, String communityUid);

    TCommunityUser queryByUserIdAndCommunityUid(String userId, String communityUid);

    RestResult statusSync(Long timestamp, String nonce, String signature, List<UserStatusSyncDTO> dtos);

    List<CommunityUserCount> queryUserCountByCommunityUids(List<String> communityUids);

    List<TCommunityUser> queryByUserId(String userId);

    RestResult<TCommunityUserInfoResp> userInfo(TCommunityUserInfoReq params, JwtUser jwtUser);

    int updateJoinByCommunityUid(String uid);

    List<TCommunityUser> queryByCommunityUid(String uid);
}

