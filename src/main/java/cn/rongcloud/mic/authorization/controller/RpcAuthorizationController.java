package cn.rongcloud.mic.authorization.controller;

import cn.rongcloud.mic.authorization.service.RpcAuthorizationService;
import cn.rongcloud.common.utils.WXUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *@Author gaoxin
 *@Date 2021/9/3 9:55 上午
 *@Version 1.0
 **/
@RestController
@RequestMapping("/rpc/authorization")
@Slf4j
@Api(tags = "远程授权")
public class RpcAuthorizationController {

	@Autowired
	private RpcAuthorizationService rpcAuthorizationService;

	/**
	 * @Description:微信小程序获取权限
	 * @Param: [code]
	 * @return: cn.rongcloud.mic.common.rest.RestResult
	 * @Author: gaoxin
	 * @Date: 2021/9/3
	 */
	@ApiOperation(value = "微信小程序获取权限")
	@GetMapping(value = "/getWechatAppletAuthorization")
	@ApiImplicitParam(name = "code", value = "code",required = true)
	public RestResult getWechatAppletAuthorization(@RequestParam String code)
			throws Exception {
		log.info("RpcAuthorizationController wechatAppletAuthorization, operator:{}, data:{}", code);
		return rpcAuthorizationService.getWechatAppletAuthorization(code);
	}

	@ApiOperation(value = "解密用户微信小程序信息")
	@GetMapping(value = "/getUserDecodeInfo")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "encryptedData", value = "加密数据",required = true),
			@ApiImplicitParam(name = "sessionKey", value = "sessionKey",required = true),
			@ApiImplicitParam(name = "iv", value = "向量",required = true)
	})
	public RestResult getUserDecodeInfo(@RequestParam String encryptedData,@RequestParam String sessionKey,@RequestParam String iv)
			throws Exception{
		log.info("RpcAuthorizationController getUserDecodeInfo, operator:{}, data:{}", encryptedData,sessionKey,iv);
		JSONObject decodeInfo = WXUtils.getDecodeInfo(encryptedData, sessionKey, iv);
		return RestResult.success(decodeInfo);
	}
}
