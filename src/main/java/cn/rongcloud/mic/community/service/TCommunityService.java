package cn.rongcloud.mic.community.service;


import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.community.dto.TCommunityDTO;
import cn.rongcloud.mic.community.dto.TCommunityDetailResp;
import cn.rongcloud.mic.community.dto.TCommunitySaveReq;
import cn.rongcloud.mic.community.dto.TCommunityUpdateReq;
import cn.rongcloud.mic.community.entity.TCommunity;
import cn.rongcloud.mic.community.vo.TCommunityVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;


/**
 * 社区表
 *
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
public interface TCommunityService {
    /**
     *
     * @param params
     * @return
     */
    RestResult<IPage<TCommunityVO>> queryPage(TCommunityDTO params);

    TCommunity queryByUid(String uid);

    RestResult<String> save(TCommunitySaveReq params, JwtUser jwtUser);

    RestResult delete(String communityUid, JwtUser jwtUser);

    RestResult<TCommunityDetailResp> detail(String communityUid, JwtUser jwtUser);

    RestResult saveAll(TCommunityDetailResp reqParam, JwtUser jwtUser);

    RestResult update(TCommunityUpdateReq params, JwtUser jwtUser);

    List<TCommunity> queryByCreator(String creator);

    int updateById(TCommunity community);
}

