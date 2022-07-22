package cn.rongcloud.mic.navigation.controller;

import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.common.jwt.filter.JwtFilter;
import cn.rongcloud.mic.navigation.pojo.req.NavigationReq;
import cn.rongcloud.mic.navigation.service.NavigationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 *@Author gaoxin
 *@Date 2021/9/7 2:28 下午
 *@Version 1.0
 **/
@RestController
@RequestMapping("/navigation")
@Slf4j
@Api(tags = "导航页设置")
@ApiIgnore
public class NavigationController {

	@Autowired
	private NavigationService navigationService;

	@ApiOperation(value = "新增导航")
	@PostMapping(value = "/addNavigation")
	public RestResult addNavigation(@RequestBody NavigationReq data, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) JwtUser jwtUser)
			throws Exception {
		log.info("addNavigation, operator:{}, data:{}", jwtUser.getUserId(), GsonUtil.toJson(data));
		return navigationService.addNavigation(data,jwtUser);
	}

	@ApiOperation(value = "删除导航")
	@ApiImplicitParam(name = "id", value = "导航id",required = true)
	@PostMapping(value = "/deleteNavigation")
	public RestResult deleteNavigation(@RequestParam Long id, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser)
			throws Exception {
		log.info("create room, operator:{}, data:{}", jwtUser.getUserId(), id);
		return navigationService.deleteNavigation(id,jwtUser);
	}

	@ApiOperation(value = "根据id查询导航")
	@PostMapping(value = "/getNavigationById")
	public RestResult getNavigationById(@RequestParam Long id, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser)
			throws Exception {
		log.info("create room, operator:{}, data:{}", jwtUser.getUserId());
		return navigationService.getNavigationById(id,jwtUser);
	}

	@ApiOperation(value = "获取导航列表")
	@PostMapping(value = "/getNavigationList")
	public RestResult getNavigationList()
			throws Exception {
		log.info("create room, operator:{}, data:{}");
		return navigationService.getNavigationList();
	}

	/**
	 * @Description:修改导航生效与否
	 * @Param: [id, effectiveType, jwtUser]
	 * @return: cn.rongcloud.mic.common.rest.RestResult
	 * @Author: gaoxin
	 * @Date: 2021/9/7
	 */
	@ApiOperation(value = "修改导航生效与否")
	@PostMapping(value = "/updateNavigationEffective")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "导航id",required = true),
			@ApiImplicitParam(name = "effectiveType", value = "生效与否：0生效，1不生效")
	})
	public RestResult updateNavigationEffective(@RequestParam Long id ,@RequestParam Integer effectiveType, @RequestAttribute(value = JwtFilter.JWT_AUTH_DATA) @ApiIgnore JwtUser jwtUser)
			throws Exception {
		log.info("create room, operator:{}, data:{}", jwtUser.getUserId(), id,effectiveType);
		return navigationService.updateNavigationEffective(id,effectiveType,jwtUser);
	}
}
