package cn.rongcloud.mic.game.service;

import cn.rongcloud.mic.common.exception.BusinessException;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.game.entity.Game;
import cn.rongcloud.mic.game.entity.GameReportInfo;
import cn.rongcloud.mic.game.pojos.BaseResp;
import cn.rongcloud.mic.game.pojos.GameReportReq;
import cn.rongcloud.mic.game.pojos.GameResp;
import cn.rongcloud.mic.game.pojos.GameStatusReq;
import cn.rongcloud.mic.game.pojos.GetSSTokenResp;
import cn.rongcloud.mic.game.pojos.GetUserInfoResp;
import cn.rongcloud.mic.game.pojos.LoginResp;
import cn.rongcloud.mic.game.pojos.UpdateSSTokenResp;
import cn.rongcloud.mic.room.pojos.ResRoomInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface GameService {

    int save(Game game);

    List<Game> findAll();

    RestResult<List<GameResp>> getGameList();

    RestResult report(GameReportReq reqParam, Long signTimestamp, String nonce, String signature);

    RestResult<ResRoomInfo> join(String gameId, JwtUser jwtUser) throws Exception;

    RestResult toggleGameStatus(GameStatusReq gameStatusReq) throws BusinessException;

    RestResult toggleGameId(GameStatusReq gameStatusReq, JwtUser jwtUser) throws BusinessException;

}
