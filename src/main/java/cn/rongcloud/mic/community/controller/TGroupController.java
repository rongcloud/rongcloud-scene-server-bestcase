package cn.rongcloud.mic.community.controller;

import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.community.dto.TGroupSaveReq;
import cn.rongcloud.mic.community.dto.TGroupUpdateReq;
import cn.rongcloud.mic.community.service.TGroupService;
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
 * 分组表
 *
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:04
 */
@RestController
@RequestMapping("/mic/group")
@Slf4j
@Api(tags="分组相关")
public class TGroupController {
    @Autowired
    private TGroupService tGroupService;


    @ApiOperation("保存")
    @PostMapping("/save")
    @RedisKeyRepeatSubmit
    public RestResult<String> save(@RequestBody TGroupSaveReq params, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        return tGroupService.save(params,jwtUser);
    }

    @ApiOperation("删除")
    @PostMapping("/delete/{groupUid}")
    @RedisKeyRepeatSubmit
    public RestResult delete(@PathVariable("groupUid") String groupUid, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        return tGroupService.delete(groupUid,jwtUser);
    }


    @ApiOperation("修改分组")
    @PostMapping("/update")
    @RedisKeyRepeatSubmit
    public RestResult update(@RequestBody TGroupUpdateReq params, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser){
        return tGroupService.update(params,jwtUser);
    }

}
