package cn.rongcloud.mic.community.controller;

import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.common.page.PageParam;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.community.dto.*;
import cn.rongcloud.mic.community.service.TCommunityUserService;
import cn.rongcloud.mic.community.vo.TCommunityVO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


/**
 * 用户社区表
 *
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@RestController
@RequestMapping("/mic/community/user")
@Slf4j
@Api(tags="社区用户相关")
public class TCommunityUserController {
    @Autowired
    private TCommunityUserService tCommunityUserService;


    @ApiOperation("关于社区用户修改")
    @PostMapping("/update")
    @RedisKeyRepeatSubmit
    public RestResult<Integer> update(@RequestBody TCommunityUserUpdateReq params, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        log.info("TCommunityUserController update params:{},jwtUser:{}",params,jwtUser);
        return tCommunityUserService.update(params,jwtUser);
    }

    @ApiOperation("用户修改频道设置")
    @PostMapping("/update/channel")
    @RedisKeyRepeatSubmit
    public RestResult<Integer> updateChannel(@RequestBody TChannelUserUpdateReq params, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        log.info("TCommunityUserController updateChannel params:{},jwtUser:{}",params,jwtUser);
        return tCommunityUserService.updateChannel(params,jwtUser);
    }

    @ApiOperation("分页查询用户列表(在线、离线、禁言、封禁)")
    @PostMapping("/page")
    public RestResult<IPage<TCommunityUserResp>> page(@RequestBody TCommunityUserReq params,@RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        return  tCommunityUserService.queryPage(params,jwtUser);
    }

    @ApiOperation("分页查询用户已加入社区列表")
    @PostMapping("/pageCommunity")
    public RestResult<IPage<TCommunityVO>> pageCommunity(@RequestBody PageParam pageParam, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        return  tCommunityUserService.pageCommunity(pageParam,jwtUser);
    }

    @ApiOperation("用户加入社区,返回值1代表审核中，3代表已加入")
    @PostMapping("/join/{communityUid}")
    @RedisKeyRepeatSubmit
    public RestResult<Integer> join(@PathVariable("communityUid") String communityUid, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        log.info("TCommunityUserController join params:{},jwtUser:{}",communityUid,jwtUser);
        return tCommunityUserService.join(communityUid,jwtUser);
    }

    @ApiOperation("查询社区用户信息")
    @PostMapping("/info")
    public RestResult<TCommunityUserInfoResp> userInfo(@RequestBody TCommunityUserInfoReq params, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        log.info("TCommunityUserController userInfo params:{},jwtUser:{}",params,jwtUser);
        return tCommunityUserService.userInfo(params,jwtUser);
    }

    @PostMapping(value = "/statusSync")
    @ApiOperation("状态同步")
    public RestResult statusSync(@RequestBody List<UserStatusSyncDTO> dtos,
                                 @RequestParam(value = "timestamp") Long timestamp,
                                 @RequestParam(value = "nonce") String nonce,
                                 @RequestParam(value = "signature") String signature) {
        log.info("user status sync, params:{}", GsonUtil.toJson(dtos));
        return tCommunityUserService.statusSync(timestamp,nonce,signature,dtos);
    }

}
