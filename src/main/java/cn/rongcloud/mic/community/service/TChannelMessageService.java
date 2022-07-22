package cn.rongcloud.mic.community.service;


import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.community.dto.TChannelMessageDTO;
import cn.rongcloud.mic.community.dto.TChannelMessageSaveReq;
import cn.rongcloud.mic.community.entity.TChannelMessage;
import cn.rongcloud.mic.community.vo.TChannelMessageVO;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
 * 频道消息表
 *
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:04
 */
public interface TChannelMessageService {
    /**
     *
     * @param params
     * @return
     */
    RestResult<IPage<TChannelMessageVO>> queryPage(TChannelMessageDTO params);


    RestResult save(TChannelMessageSaveReq params, JwtUser jwtUser);

    RestResult delete(String messageUid, JwtUser jwtUser);

    RestResult<TChannelMessageVO> detail(String messageUid, JwtUser jwtUser);

    TChannelMessage queryByMessageUidAndChannelUid(String messageUid, String channelUid);
}

