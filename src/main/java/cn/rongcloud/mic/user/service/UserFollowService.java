package cn.rongcloud.mic.user.service;

import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.user.model.TUserFollow;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserFollowService {

    RestResult follow(JwtUser jwtUser, String userId);

    RestResult followList(JwtUser jwtUser, Integer type, Integer page, Integer size);

    Page<TUserFollow> findByUidList(String uid, Integer page, Integer size, Integer type, List<String> userIds);

    void deleteByUserId(String uid);
}
