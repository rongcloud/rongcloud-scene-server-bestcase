package cn.rongcloud.mic.community.controller;

import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.community.dto.TMessageDTO;
import cn.rongcloud.mic.community.dto.TMessageSyncReq;
import cn.rongcloud.mic.community.service.TMessageService;
import cn.rongcloud.mic.community.vo.TMessageVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * 全量消息表
 *
 * @author zxs
 * @email 
 * @date 2022-03-07 09:50:03
 */
@RestController
@RequestMapping("/mic/message")
@Slf4j
@Api(tags="全量消息相关")
public class TMessageController {
    @Autowired
    private TMessageService tMessageService;

    @ApiOperation("分页查询")
    @PostMapping("/page")
    public RestResult<IPage<TMessageVO>> list(@RequestBody TMessageDTO params){
        return  tMessageService.queryPage(params);
    }


    @ApiOperation("根据id查")
    @GetMapping("/getbyid")
    public RestResult<TMessageVO> queryById(@ApiParam("参数注释") @RequestParam("id") Long id){
            TMessageVO result= tMessageService.queryById(id);
        return  RestResult.success(result);
    }

    @PostMapping(value = "/sync")
    @ApiOperation("消息同步")
    public RestResult sync(TMessageSyncReq params,
                           @RequestParam(value = "timestamp") Long timestamp,
                           @RequestParam(value = "nonce") String nonce,
                           @RequestParam(value = "signature") String signature) {
        log.info("message sync, params:{}，timestamp：{}，noce:{},signature:{}", GsonUtil.toJson(params),timestamp,nonce,signature);
        return tMessageService.sync(params,timestamp,nonce,signature);
    }



}
