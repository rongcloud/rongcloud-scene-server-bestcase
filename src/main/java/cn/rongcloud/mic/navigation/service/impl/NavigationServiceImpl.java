package cn.rongcloud.mic.navigation.service.impl;

import java.util.Date;
import java.util.List;

import cn.rongcloud.common.utils.DateTimeUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.user.model.TUser;
import cn.rongcloud.mic.user.service.UserService;
import cn.rongcloud.mic.navigation.dao.NavigationDao;
import cn.rongcloud.mic.navigation.model.TNavigation;
import cn.rongcloud.mic.navigation.pojo.req.NavigationReq;
import cn.rongcloud.mic.navigation.pojo.req.UpdateNavigationReq;
import cn.rongcloud.mic.navigation.pojo.resp.NavigationInfoResp;
import cn.rongcloud.mic.navigation.service.NavigationService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *@Author gaoxin
 *@Date 2021/9/7 2:53 下午
 *@Version 1.0
 **/
@Slf4j
@Service
public class NavigationServiceImpl implements NavigationService {

	@Autowired
	private UserService userService;

	@Autowired
	private NavigationDao navigationDao;

	@Override
	public RestResult addNavigation(NavigationReq data, JwtUser jwtUser) {
		//只有注册用户可操作
		/*if (!jwtUser.getType().equals(UserType.USER.getValue())) {
			return RestResult.generic(RestResultCode.ERR_ACCESS_DENIED);
		}*/
		//判断用户是否存在
		TUser tUser = userService.getUserInfo(jwtUser.getUserId());
		if (tUser == null) {
			return RestResult.generic(RestResultCode.ERR_ROOM_USER_IS_NOT_EXIST);
		}
		//组装实体并保存
		TNavigation navigation = new TNavigation();
		BeanUtils.copyProperties(data,navigation);
		navigation.setCreateUser(jwtUser.getUserId());
		navigation.setUpdateUser(jwtUser.getUserId());
		Date date = DateTimeUtils.currentUTC();
		navigation.setCreateDt(date);
		navigation.setUpdateDt(date);

		navigationDao.save(navigation);

		return RestResult.success();
	}

	@Override
	public RestResult deleteNavigation(Long id, JwtUser jwtUser) {
		//只有注册用户可操作
		/*if (!jwtUser.getType().equals(UserType.USER.getValue())) {
			return RestResult.generic(RestResultCode.ERR_ACCESS_DENIED);
		}*/
		//判断用户是否存在
		TUser tUser = userService.getUserInfo(jwtUser.getUserId());
		if (tUser == null) {
			return RestResult.generic(RestResultCode.ERR_ROOM_USER_IS_NOT_EXIST);
		}
		navigationDao.deleteById(id);
		return RestResult.success();
	}

	@Override
	public RestResult updateNavigation(UpdateNavigationReq data, JwtUser jwtUser) {


		return null;
	}

	@Override
	public RestResult getNavigationById(Long id, JwtUser jwtUser) {
		//只有注册用户可操作
		/*if (!jwtUser.getType().equals(UserType.USER.getValue())) {
			return RestResult.generic(RestResultCode.ERR_ACCESS_DENIED);
		}*/
		//判断用户是否存在
		TUser tUser = userService.getUserInfo(jwtUser.getUserId());
		if (tUser == null) {
			return RestResult.generic(RestResultCode.ERR_ROOM_USER_IS_NOT_EXIST);
		}

		TNavigation tNavigation = navigationDao.findTNavigationById(id);
		NavigationInfoResp resp = new NavigationInfoResp();
		BeanUtils.copyProperties(tNavigation,resp);

		return RestResult.success(resp);
	}

	@Override
	public RestResult getNavigationList() {

		List<TNavigation> navigationList = navigationDao.findAll();
		return RestResult.success(navigationList);
	}

	@Override
	public RestResult updateNavigationEffective(Long id, Integer effectiveType, JwtUser jwtUser) {
		//只有注册用户可操作
		/*if (!jwtUser.getType().equals(UserType.USER.getValue())) {
			return RestResult.generic(RestResultCode.ERR_ACCESS_DENIED);
		}*/
		//判断用户是否存在
		TUser tUser = userService.getUserInfo(jwtUser.getUserId());
		if (tUser == null) {
			return RestResult.generic(RestResultCode.ERR_ROOM_USER_IS_NOT_EXIST);
		}
		String uid = jwtUser.getUserId();
		Date date = DateTimeUtils.currentUTC();

		int effective = navigationDao.updateNavigationEffective(id, effectiveType,uid,date);
		return RestResult.success(effective);
	}
}
