package cn.rongcloud.mic.appversion.controller;

import cn.rongcloud.mic.appversion.pojos.ReqAppVersionCreate;
import cn.rongcloud.mic.appversion.pojos.ReqAppVersionUpdate;
import cn.rongcloud.mic.appversion.pojos.RespAppVersion;
import cn.rongcloud.mic.appversion.service.AppVersionService;
import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.rest.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sunyinglong on 2020/6/25
 */
@RestController
@RequestMapping("/appversion")
@Slf4j
@Api(tags = "app 版本管理")
public class AppVersionController {

    @Autowired
    private AppVersionService appVersionService;

    @GetMapping(value = "/latest")
    @ApiOperation("获取 App 最新版本")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform", value = "Android或iOS",required = true)
    })
    public RestResult<RespAppVersion> appVersionLatest(@RequestParam(value = "platform", required = true) String platform){
        log.info("get latest app version, platform:{}", platform);
        return appVersionService.getLatestAppVersion(platform);
    }

    @ApiOperation("发布版本")
    @PostMapping(value = "/publish")
    @RedisKeyRepeatSubmit
    public RestResult createAppVersion(@RequestBody ReqAppVersionCreate data){
        log.info("publish app version, data:{}", GsonUtil.toJson(data));
        return appVersionService.publishAppVersion(data);
    }

    @ApiOperation("更新版本信息")
    @PutMapping(value = "/update")
    @RedisKeyRepeatSubmit
    public RestResult updateAppVersion(
        @RequestBody ReqAppVersionUpdate data,
        @RequestParam(value = "platform", required = true) String platform,
        @RequestParam(value = "version", required = true) String version){
        log.info("update app version, platform:{}, version:{}, data:{}", platform, version, GsonUtil.toJson(data));
        return appVersionService.updateAppVersion(platform, version, data);
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation("删除版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform", value = "Android或iOS",required = true),
            @ApiImplicitParam(name = "version", value = "版本")
    })
    public RestResult deleteAppVersion(@RequestParam(value = "platform", required = true) String platform,
        @RequestParam(value = "version", required = true) String version){
        log.info("delete app version, platform:{}, version:{}", platform, version);
        return appVersionService.deleteAppVersion(platform, version);
    }
}
