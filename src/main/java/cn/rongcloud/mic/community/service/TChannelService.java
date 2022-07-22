package cn.rongcloud.mic.community.service;


import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.community.dto.TChannelDTO;
import cn.rongcloud.mic.community.dto.TChannelSaveReq;
import cn.rongcloud.mic.community.dto.TChannelUpdateReq;
import cn.rongcloud.mic.community.entity.TChannel;
import cn.rongcloud.mic.community.vo.TChannelVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;


/**
 * 频道表
 *
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
public interface TChannelService {
    /**
     *
     * @param params
     * @return
     */
    RestResult<IPage<TChannelVO>> queryPage(TChannelDTO params);

    TChannel queryByUid(String uid);

    List<TChannel> queryByCommunityUid(String communityUid);

    RestResult<String> save(TChannelSaveReq params, JwtUser jwtUser);

    RestResult delete(String channelUid, JwtUser jwtUser);

    RestResult update(TChannelUpdateReq params, JwtUser jwtUser);

    void insert(TChannel channel);

    void deleteByCommunityUid(String communityUid);

    void deleteByGroupUids(List<String> groupUids);

    void batchUpdate(List<TChannel> updateChannels);

    RestResult<TChannelVO> detail(String channelUid, JwtUser jwtUser);

    TChannel queryByCommunityUidAndName(String communityUid, String name);

    List<TChannel> queryByGroupUids(List<String> groupUids);
}

