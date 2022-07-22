package cn.rongcloud.mic.shumei.controller;

import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.shumei.service.ShumeiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/mic/audit")
@Slf4j
@Api(tags = "审核 相关")
public class AuditController {

    @Autowired
    ShumeiService shumeiService;

    @ApiOperation("文本审核")
    @PostMapping(value = "/text/{text}")
    public RestResult textAudit(@PathVariable("text") String text, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("text audit, operator:{}, text:{}", jwtUser.getUserId(), text);
        RestResult restResult = shumeiService.textAudit(text);
        if(restResult.getCode()!=RestResultCode.ERR_SUCCESS.getCode()){
            return restResult;
        }
        return RestResult.success();
    }

}
