package cn.rongcloud.mic.game.controller;

import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.game.entity.GameReportInfo;
import cn.rongcloud.mic.game.pojos.*;
import cn.rongcloud.mic.game.service.GameService;
import cn.rongcloud.mic.room.pojos.ResRoomInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/mic/game")
@Slf4j
@Api(tags = "游戏 相关")
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * 短期令牌Code更换长期令牌SSToken
     * 调用方：游戏服务
     *
     * @param reqParam
     * @return
     */
    @ApiOperation("get_sstoken")
    @PostMapping("/get_sstoken")
    public Object getSSToken(@RequestBody() GetSSTokenReq reqParam) {
        return gameService.getSSToken(reqParam.getCode());
    }

    /**
     * 刷新长期令牌
     * 调用方：游戏服务
     *
     * @param reqParam
     * @return
     */
    @ApiOperation("update_sstoken")
    @PostMapping("/update_sstoken")
    public Object updateSSToken(@RequestBody() UpdateSSTokenReq reqParam) {
        return gameService.updateSSToken(reqParam.getSs_token());
    }

    /**
     * 获取用户信息
     * 调用方：游戏服务
     *
     * @param reqParam
     * @return
     */
    @ApiOperation("get_user_info")
    @PostMapping("/get_user_info")
    public Object getUserInfo(@RequestBody() GetUserInfoReq reqParam, HttpServletRequest request) {
        return gameService.getUserInfo(reqParam.getSs_token(), request);
    }

    /**
     * 游戏上报
     * 调用方：游戏服务
     *
     * @param reqParam
     * @return
     */
    @ApiOperation("report_game")
    @PostMapping("/report_game")
    public Object reportGameInfo(@RequestBody() GameReportInfo reqParam) {
        return gameService.reportGameInfo(reqParam);
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public RestResult<LoginResp> login(@RequestBody() LoginReq reqParam) {
        return gameService.login(reqParam.getUserId());
    }

    @ApiOperation("获取游戏列表")
    @GetMapping("/list")
    public RestResult<List<GameResp>> getGameList(){
       return gameService.getGameList();
    }

    @ApiOperation("游戏上报")
    @PostMapping("/report")
    public RestResult report(@RequestBody() GameReportReq reqParam,
                             @RequestParam(value = "signTimestamp") Long signTimestamp,
                             @RequestParam(value = "nonce") String nonce,
                             @RequestParam(value = "signature") String signature) {
        return gameService.report(reqParam,signTimestamp, nonce, signature);
    }

    @ApiOperation("快速加入游戏")
    @GetMapping("/join")
    public RestResult<ResRoomInfo> join(@ApiParam(name = "gameId", value = "游戏id")
                                        @RequestParam(value = "gameId") String gameId,
                                        @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser) throws Exception {
        return gameService.join(gameId, jwtUser);
    }

    @ApiOperation("游戏状态切换")
    @PostMapping("/toggle/game_status")
    public RestResult toggleGameStatus(@RequestBody() GameStatusReq gameStatusReq) {
        return gameService.toggleGameStatus(gameStatusReq);
    }

    @ApiOperation("游戏切换")
    @PostMapping("/toggle/game_id")
    public RestResult toggleGameId(@RequestBody() GameStatusReq gameStatusReq,
                                   @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) JwtUser jwtUser) {
        return gameService.toggleGameId(gameStatusReq, jwtUser);
    }
}
