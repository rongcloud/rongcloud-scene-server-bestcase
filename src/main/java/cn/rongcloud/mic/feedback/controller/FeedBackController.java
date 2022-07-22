package cn.rongcloud.mic.feedback.controller;


import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.annotation.RedisKeyRepeatSubmit;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.feedback.model.TFeedBack;
import cn.rongcloud.mic.feedback.pojos.RequestFeedback;
import cn.rongcloud.mic.feedback.service.FeedBackService;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Slf4j
@RequestMapping("feedback")
@Api(tags = "评价相关")
public class FeedBackController {

    @Autowired
    private FeedBackService feedBackService;

    @ApiOperation(value = "添加用户评价")
    @PostMapping("/create")
    @RedisKeyRepeatSubmit
    public RestResult create(@RequestBody RequestFeedback data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) {
        log.info("create feedback, operator:{}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
        return feedBackService.add(data, jwtUser);
    }

    @GetMapping("/list")
    @ApiIgnore
    @ApiOperation(value = "查询用户评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id"),
            @ApiImplicitParam(name = "page", value = "第几页",defaultValue = "1"),
            @ApiImplicitParam(name = "size", value = "每页条数",defaultValue = "20")
    })
    public RestResult<Page<TFeedBack>> list(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                            @RequestParam(value = "size", defaultValue = "20") Integer size,
                                            @RequestParam(value = "userId",required = false) String userId) {
        log.info("list feedback, page:{}, size:{}", page, size);
        return feedBackService.list(page,size,userId);
    }
}
