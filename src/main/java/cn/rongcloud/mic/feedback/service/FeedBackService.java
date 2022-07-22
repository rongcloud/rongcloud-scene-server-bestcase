package cn.rongcloud.mic.feedback.service;

import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.feedback.model.TFeedBack;
import cn.rongcloud.mic.feedback.pojos.RequestFeedback;
import cn.rongcloud.mic.common.jwt.JwtUser;
import org.springframework.data.domain.Page;

public interface FeedBackService {

    /**
     * 添加 反馈
     *
     * @param feedback
     */
    RestResult add(RequestFeedback feedback, JwtUser jwtUser);

    RestResult<Page<TFeedBack>> list(Integer page, Integer size, String userId);
}
