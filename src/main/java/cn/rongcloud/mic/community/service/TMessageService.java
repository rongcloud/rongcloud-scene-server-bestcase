package cn.rongcloud.mic.community.service;


import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.community.dto.TMessageDTO;
import cn.rongcloud.mic.community.dto.TMessageSyncReq;
import cn.rongcloud.mic.community.vo.TMessageVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Date;


/**
 * 全量消息表
 *
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
public interface TMessageService {
    /**
     *
     * @param params
     * @return
     */
    RestResult<IPage<TMessageVO>> queryPage(TMessageDTO params);

    /**
     *
     * @param id
     * @return
     */
    TMessageVO queryById(Long id);

    RestResult sync(TMessageSyncReq params, Long timestamp, String nonce, String signature);

    int deleteMessageByDate(Date date);
}

