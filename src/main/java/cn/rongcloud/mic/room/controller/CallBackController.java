package cn.rongcloud.mic.room.controller;

import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.room.pojos.callback.AuditCallBackDto;
import cn.rongcloud.mic.room.service.CallBackServcie;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mic/callback")
@Slf4j
@Api(tags = "回调相关")
public class CallBackController {

    @Autowired
    private CallBackServcie callBackServcie;

    @PostMapping(value = "/audit")
    @ApiOperation("审核回调")
    public RestResult auditSync(@RequestBody AuditCallBackDto auditCallBackDto) {
        return callBackServcie.auditSync(auditCallBackDto);
    }


}
