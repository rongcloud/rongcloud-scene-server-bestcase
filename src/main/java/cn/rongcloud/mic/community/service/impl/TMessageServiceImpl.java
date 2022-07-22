package cn.rongcloud.mic.community.service.impl;


import cn.rongcloud.common.utils.IMSignUtil;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.utils.CoverUtil;
import cn.rongcloud.mic.community.dto.TMessageDTO;
import cn.rongcloud.mic.community.dto.TMessageSyncReq;
import cn.rongcloud.mic.community.entity.TMessage;
import cn.rongcloud.mic.community.mapper.TMessageMapper;
import cn.rongcloud.mic.community.service.TMessageService;
import cn.rongcloud.mic.community.vo.TMessageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TMessageServiceImpl implements TMessageService {

    @Resource
    private TMessageMapper TMessageMapper;

    @Autowired
    IMSignUtil imSignUtil;

    @Override
    public RestResult<IPage<TMessageVO>> queryPage(TMessageDTO params) {
        Page<TMessage> pageParam = new Page<>();
        pageParam.setCurrent(params.getPageNum());
        pageParam.setSize(params.getPageSize());
        IPage pageResult = TMessageMapper.selectPage(pageParam, new QueryWrapper<TMessage>());
        List<TMessage> records = pageResult.getRecords();
        List<TMessageVO> pageVOS = CoverUtil.sourceToTarget(records, TMessageVO.class);
        pageResult.setRecords(pageVOS);
        return RestResult.success(pageResult);
    }


    @Override
    public TMessageVO queryById(Long id) {
        TMessage entity = TMessageMapper.selectById(id);
        TMessageVO result = CoverUtil.sourceToTarget(entity, TMessageVO.class);
        return result;
    }

    @Override
    public RestResult sync(TMessageSyncReq params, Long timestamp, String nonce, String signature) {
        //校验签名是否正确
        if (!imSignUtil.validSign(nonce, timestamp.toString(), signature)) {
            log.info("message sync access denied: signature error, timestamp:{}, nonce:{}, signature:{}", timestamp, nonce, signature);
            return RestResult.success();
        }
        TMessage message = new TMessage();
        BeanUtils.copyProperties(params,message);
        message.setMessageUid(params.getMsgUID());
        TMessageMapper.insert(message);
        return RestResult.success();
    }

    @Override
    public int deleteMessageByDate(Date date) {
        LambdaQueryWrapper<TMessage> wrapper = new QueryWrapper<TMessage>().lambda().lt(TMessage::getCreateTime, date);
        return TMessageMapper.delete(wrapper);
    }

}