package cn.rongcloud.mic.community.controller;

import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.community.dto.TCommunityDTO;
import cn.rongcloud.mic.community.dto.TCommunityDetailResp;
import cn.rongcloud.mic.community.dto.TCommunitySaveReq;
import cn.rongcloud.mic.community.dto.TCommunityUpdateReq;
import cn.rongcloud.mic.community.service.TCommunityService;
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
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 社区表
 *
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@RestController
@RequestMapping("/mic/community")
@Slf4j
@Api(tags="社区相关")
public class TCommunityController {

    @Autowired
    private TCommunityService tCommunityService;

    @ApiOperation("创建社区")
    @PostMapping("/save")
    @RedisKeyRepeatSubmit
    public RestResult<String> save(@RequestBody TCommunitySaveReq params, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        log.info("TCommunityController save params:{},jwtUser:{}",params,jwtUser);
        return tCommunityService.save(params,jwtUser);
    }

    @ApiOperation("修改社区")
    @PostMapping("/update")
    @RedisKeyRepeatSubmit
    public RestResult update(@RequestBody TCommunityUpdateReq params, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        return tCommunityService.update(params,jwtUser);
    }

    @ApiOperation("解散社区")
    @PostMapping("/delete/{communityUid}")
    @RedisKeyRepeatSubmit
    public RestResult delete(@PathVariable("communityUid") String communityUid, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        return tCommunityService.delete(communityUid,jwtUser);
    }


    @ApiOperation("社区详情")
    @PostMapping("/detail/{communityUid}")
    public RestResult<TCommunityDetailResp> detail(@PathVariable("communityUid") String communityUid, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("TCommunityController detail communityUid:{},jwtUser:{}",communityUid,jwtUser);
        return tCommunityService.detail(communityUid,jwtUser);
    }

    @ApiOperation("整体保存")
    @PostMapping("/saveAll")
    @RedisKeyRepeatSubmit
    public RestResult saveAll(@RequestBody TCommunityDetailResp reqParam, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("TCommunityController saveAll reqParam:{},jwtUser:{}", GsonUtil.toJson(reqParam),jwtUser);
        return tCommunityService.saveAll(reqParam,jwtUser);
    }


    @ApiOperation("分页查询,用在发现页")
    @PostMapping("/page")
    public RestResult<IPage<TCommunityVO>> list(@RequestBody TCommunityDTO params){
        return tCommunityService.queryPage(params);
    }

}
