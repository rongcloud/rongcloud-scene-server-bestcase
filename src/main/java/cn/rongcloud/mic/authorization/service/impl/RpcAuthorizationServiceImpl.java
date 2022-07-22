package cn.rongcloud.mic.authorization.service.impl;

import cn.rongcloud.mic.authorization.service.RpcAuthorizationService;
import cn.rongcloud.common.utils.HttpUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

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


	/**
	 * @Description:微信小程序获取权限
	 * @Param: [code]
	 * @return: cn.rongcloud.mic.common.rest.RestResult
	 * @Author: gaoxin
	 * @Date: 2021/9/3
	 */
	@Override
	public RestResult getWechatAppletAuthorization(String code) throws Exception {

		String url = String.format(code2SessionUrl, appId, appSecret, code);
		Object reponse = HttpUtils.sendGet(url, null, null, null);

		JSONObject jsonObject = JSONObject.parseObject(reponse.toString());

		return RestResult.success(jsonObject);



	}
}
