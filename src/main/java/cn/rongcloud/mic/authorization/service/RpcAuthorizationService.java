package cn.rongcloud.mic.authorization.service;

import cn.rongcloud.mic.common.rest.RestResult;

public interface RpcAuthorizationService {

	/**
	 * @Description:微信小程序获取权限
	 * @Param: [code]
	 * @return: cn.rongcloud.mic.common.rest.RestResult
	 * @Author: gaoxin
	 * @Date: 2021/9/3
	 */
	RestResult getWechatAppletAuthorization(String code) throws Exception;

}
