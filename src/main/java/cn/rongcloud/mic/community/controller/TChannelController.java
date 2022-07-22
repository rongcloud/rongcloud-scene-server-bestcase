package cn.rongcloud.mic.community.controller;

import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.community.dto.TChannelSaveReq;
import cn.rongcloud.mic.community.dto.TChannelUpdateReq;
import cn.rongcloud.mic.community.service.TChannelService;
import cn.rongcloud.mic.community.vo.TChannelVO;
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
 * 频道表
 *
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@RestController
@RequestMapping("/mic/channel")
@Slf4j
@Api(tags="频道相关")
public class TChannelController {
    @Autowired
    private TChannelService tChannelService;

    @ApiOperation("创建频道")
    @PostMapping("/save")
    @RedisKeyRepeatSubmit
    public RestResult<String> save(@RequestBody TChannelSaveReq params, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        return tChannelService.save(params,jwtUser);
    }

    @ApiOperation("删除频道")
    @PostMapping("/delete/{channelUid}")
    @RedisKeyRepeatSubmit
    public RestResult delete(@PathVariable("channelUid") String channelUid, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        return tChannelService.delete(channelUid,jwtUser);
    }

    @ApiOperation("频道详情")
    @PostMapping("/detail/{channelUid}")
    public RestResult<TChannelVO> detail(@PathVariable("channelUid") String channelUid, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        return tChannelService.detail(channelUid,jwtUser);
    }

    @ApiOperation("修改频道")
    @PostMapping("/update")
    @RedisKeyRepeatSubmit
    public RestResult update(@RequestBody TChannelUpdateReq params, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        return tChannelService.update(params,jwtUser);
    }

}
