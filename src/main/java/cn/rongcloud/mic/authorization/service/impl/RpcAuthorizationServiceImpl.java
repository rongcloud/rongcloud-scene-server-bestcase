package cn.rongcloud.mic.authorization.service.impl;

import static cn.rongcloud.mic.common.constant.CustomerConstant.WX_REQUEST_SUCCESS;

import cn.rongcloud.mic.authorization.pojos.AuthorizationResp;
import cn.rongcloud.mic.authorization.pojos.WXSessionResp;
import cn.rongcloud.mic.authorization.service.RpcAuthorizationService;
import cn.rongcloud.common.utils.HttpUtils;
import cn.rongcloud.mic.common.constant.CustomerConstant;
import cn.rongcloud.mic.common.redis.RedisUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import com.alibaba.fastjson.JSONObject;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *@Author gaoxin
 *@Date 2021/9/3 10:02 上午
 *@Version 1.0
 **/
@Slf4j
@Service
public class RpcAuthorizationServiceImpl implements RpcAuthorizationService {

	@Value("${wechat.applet.appId}")
	private String appId;
	@Value("${wechat.applet.appSecret}")
	private String appSecret;
	@Value("${wechat.applet.code2SessionUrl}")
	private String code2SessionUrl;

	private RedisUtils redisUtils;

	@Autowired
	public void setRedisUtils(RedisUtils redisUtils) {
		this.redisUtils = redisUtils;
	}


	/**
	 * @Description:微信小程序获取权限
	 * @Param: [code]
	 * @return: cn.rongcloud.mic.common.rest.RestResult
	 * @Author: gaoxin
	 * @Date: 2021/9/3
	 */
	@Override
	public RestResult<AuthorizationResp> getWechatAppletAuthorization(String code) throws Exception {

		String url = String.format(code2SessionUrl, appId, appSecret, code);
		String response = HttpUtils.sendGet(url, null, null, null);

		WXSessionResp resp = JSONObject.parseObject(response, WXSessionResp.class);
		if (Objects.isNull(resp)
			|| (Objects.nonNull(resp.getErrCode()) && !Objects.equals(WX_REQUEST_SUCCESS, resp.getErrCode()))) {
			log.error("wx code2session request error, response is: {}", response);
			return RestResult.generic(RestResultCode.ERR_WX_SESSION_AUTH_ERROR);
		}

		// 缓存sessionKey 5分钟
		redisUtils.set(CustomerConstant.WX_SESSION_KEY_CACHE_PREFIX + resp.getOpenId(),
			resp.getSessionKey(), 5 * 60);

		return RestResult.success(AuthorizationResp.builder().openId(resp.getOpenId()).build());
	}
}
