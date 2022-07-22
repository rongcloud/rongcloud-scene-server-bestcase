package cn.rongcloud.mic.community.controller;

import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.community.dto.TChannelMessageDTO;
import cn.rongcloud.mic.community.dto.TChannelMessageSaveReq;
import cn.rongcloud.mic.community.service.TChannelMessageService;
import cn.rongcloud.mic.community.vo.TChannelMessageVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 频道消息表
 *
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:04
 */
@RestController
@RequestMapping("/mic/channel/message")
@Slf4j
@Api(tags="频道消息相关")
public class TChannelMessageController {
    @Autowired
    private TChannelMessageService tChannelMessageService;

    @ApiOperation("分页查询频道标记消息")
    @PostMapping("/page")
    public RestResult<IPage<TChannelMessageVO>> page(@RequestBody TChannelMessageDTO params){
        return tChannelMessageService.queryPage(params);
    }

    @ApiOperation("添加频道标记消息")
    @PostMapping("/save")
    @RedisKeyRepeatSubmit
    public RestResult save(@RequestBody TChannelMessageSaveReq params, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        return tChannelMessageService.save(params,jwtUser);
    }

    @ApiOperation("删除频道标记消息")
    @PostMapping("/delete/{messageUid}")
    @RedisKeyRepeatSubmit
    public RestResult delete(@PathVariable("messageUid")String messageUid, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        return tChannelMessageService.delete(messageUid,jwtUser);
    }


    @ApiOperation("标记消息详情")
    @PostMapping("/detail/{messageUid}")
    public RestResult<TChannelMessageVO> detail(@PathVariable("messageUid") String messageUid, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        return tChannelMessageService.detail(messageUid,jwtUser);
    }

}
