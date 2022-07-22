package cn.rongcloud.mic.game.service;

import cn.rongcloud.mic.common.exception.BusinessException;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.game.entity.Game;
import cn.rongcloud.mic.game.entity.GameReportInfo;
import cn.rongcloud.mic.game.pojos.*;
import cn.rongcloud.mic.room.pojos.ResRoomInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface GameService {

    int save(Game game);

    List<Game> findAll();

    BaseResp<GetSSTokenResp> getSSToken(String code);

    BaseResp<UpdateSSTokenResp> updateSSToken(String ssToken);

    BaseResp<GetUserInfoResp> getUserInfo(String ssToken, HttpServletRequest request);

    BaseResp reportGameInfo(GameReportInfo reqParam);

    RestResult<LoginResp> login(String userId);

    RestResult<List<GameResp>> getGameList();

    RestResult report(GameReportReq reqParam, Long signTimestamp, String nonce, String signature);

    RestResult<ResRoomInfo> join(String gameId, JwtUser jwtUser) throws Exception;

    RestResult toggleGameStatus(GameStatusReq gameStatusReq) throws BusinessException;

    RestResult toggleGameId(GameStatusReq gameStatusReq, JwtUser jwtUser) throws BusinessException;

}
