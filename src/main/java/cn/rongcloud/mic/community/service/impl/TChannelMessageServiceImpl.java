package cn.rongcloud.mic.community.service.impl;


import cn.rongcloud.common.utils.IdentifierUtils;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.common.utils.CoverUtil;
import cn.rongcloud.mic.community.dto.TChannelMessageDTO;
import cn.rongcloud.mic.community.dto.TChannelMessageSaveReq;
import cn.rongcloud.mic.community.entity.TChannel;
import cn.rongcloud.mic.community.entity.TChannelMessage;
import cn.rongcloud.mic.community.enums.ChannelMessageTypeEnum;
import cn.rongcloud.mic.community.mapper.TChannelMessageMapper;
import cn.rongcloud.mic.community.message.CommunityMessageUtil;
import cn.rongcloud.mic.community.service.TChannelMessageService;
import cn.rongcloud.mic.community.service.TChannelService;
import cn.rongcloud.mic.community.service.TCommunityService;
import cn.rongcloud.mic.community.vo.TChannelMessageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TChannelMessageServiceImpl implements TChannelMessageService {

    @Resource
    private TChannelMessageMapper TChannelMessageMapper;

    @Autowired
    private CommunityMessageUtil communityMessageUtil;

    @Autowired
    private TChannelService tChannelService;

    @Autowired
    private TCommunityService tCommunityService;

    @Override
    public RestResult<IPage<TChannelMessageVO>> queryPage(TChannelMessageDTO params) {
        Page<TChannelMessage> pageParam = new Page<>();
        pageParam.setCurrent(params.getPageNum());
        pageParam.setSize(params.getPageSize());
        LambdaQueryWrapper<TChannelMessage> queryWrapper = new QueryWrapper<TChannelMessage>().lambda()
                .eq(TChannelMessage::getDelFlag,0).eq(TChannelMessage::getChannelUid, params.getChannelUid())
                .orderByDesc(TChannelMessage::getId);
        IPage pageResult = TChannelMessageMapper.selectPage(pageParam,queryWrapper);
        List<TChannelMessage> records = pageResult.getRecords();
        List<TChannelMessageVO> pageVOS = CoverUtil.sourceToTarget(records, TChannelMessageVO.class);
        pageResult.setRecords(pageVOS);
        return RestResult.success(pageResult);
    }

    @Override
    public RestResult save(TChannelMessageSaveReq params,JwtUser jwtUser) {
        TChannel channel = tChannelService.queryByUid(params.getChannelUid());
        if(channel==null){
            return RestResult.generic(RestResultCode.ERR_CHANNEL_NOT_EXIT);
        }
        TChannelMessage tChannelMessage = queryByMessageUidAndChannelUid(params.getMessageUid(), params.getChannelUid());
        if(tChannelMessage!=null){
            return RestResult.success();
        }
        TChannelMessage channelMessage = TChannelMessage.builder().channelUid(params.getChannelUid())
                .uid(IdentifierUtils.uuid())
                .messageUid(params.getMessageUid()).creator(jwtUser.getUserId()).build();
        TChannelMessageMapper.insert(channelMessage);
        String message = String.format("%s标注了一条消息",jwtUser.getUserName());
        communityMessageUtil.sendChannelMarkMessage(jwtUser.getUserId(),channel.getCommunityUid(),message,params.getChannelUid(),params.getMessageUid(),ChannelMessageTypeEnum.MARK.getValue());
        return RestResult.success();
    }

    @Override
    public RestResult delete(String messageUid, JwtUser jwtUser) {
        TChannelMessage channelMessage = TChannelMessageMapper.selectOne(new QueryWrapper<TChannelMessage>().lambda().eq(TChannelMessage::getMessageUid, messageUid).eq(TChannelMessage::getDelFlag, 0));
        if(channelMessage==null){
            return RestResult.generic(RestResultCode.ERR_NOT_FIND);
        }
        TChannel channel = tChannelService.queryByUid(channelMessage.getChannelUid());
        if(!jwtUser.getUserId().equals(channelMessage.getCreator())&&!jwtUser.getUserId().equals(channel.getCreator())){
            return RestResult.generic(RestResultCode.ERR_ACCESS_DENIED);
        }

        channelMessage.setDelFlag(1);
        TChannelMessageMapper.updateById(channelMessage);
        String message = String.format("%s删除了一条标注消息",jwtUser.getUserName());
        communityMessageUtil.sendChannelMarkMessage(jwtUser.getUserId(),channel.getCommunityUid(),message,channel.getUid(),messageUid,ChannelMessageTypeEnum.NO_MARK.getValue());
        return RestResult.success();
    }

    @Override
    public RestResult<TChannelMessageVO> detail(String messageUid, JwtUser jwtUser) {
        TChannelMessageVO vo = new TChannelMessageVO();
        TChannelMessage channelMessage = TChannelMessageMapper.selectOne(new QueryWrapper<TChannelMessage>().lambda().eq(TChannelMessage::getMessageUid, messageUid).eq(TChannelMessage::getDelFlag,0));
        BeanUtils.copyProperties(channelMessage,vo);
        return RestResult.success(vo);
    }

    @Override
    public TChannelMessage queryByMessageUidAndChannelUid(String messageUid, String channelUid) {
        if(StringUtils.isBlank(messageUid)|| StringUtils.isBlank(channelUid)){
            return null;
        }
        LambdaQueryWrapper<TChannelMessage> queryWrapper = new QueryWrapper<TChannelMessage>().lambda()
                .eq(TChannelMessage::getDelFlag,0).eq(TChannelMessage::getChannelUid, channelUid)
                .eq(TChannelMessage::getMessageUid,messageUid);
        return TChannelMessageMapper.selectOne(queryWrapper);
    }

}