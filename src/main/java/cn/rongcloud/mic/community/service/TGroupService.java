package cn.rongcloud.mic.community.service;


import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.community.dto.TGroupDTO;
import cn.rongcloud.mic.community.dto.TGroupSaveReq;
import cn.rongcloud.mic.community.dto.TGroupUpdateReq;
import cn.rongcloud.mic.community.entity.TGroup;
import cn.rongcloud.mic.community.vo.TGroupVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;


/**
 * 分组表
 *
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:04
 */
public interface TGroupService {
    /**
     *
     * @param params
     * @return
     */
    RestResult<IPage<TGroupVO>> queryPage(TGroupDTO params);

    TGroup queryByUid(String uid);

    List<TGroup> queryByCommunityUid(String communityUid);

    RestResult<String> save(TGroupSaveReq params, JwtUser jwtUser);

    RestResult delete(String groupUid, JwtUser jwtUser);

    void insert(TGroup group);

    void deleteByCommunityUid(String communityUid);

    void batchUpate(List<TGroup> updateGroups);

    TGroup queryByCommunityUidAndName(String communityUid, String name);

    void sendGroupDeleteMessage(String communityUid, List<String> groupUids, String userId);

    RestResult update(TGroupUpdateReq params, JwtUser jwtUser);
}

