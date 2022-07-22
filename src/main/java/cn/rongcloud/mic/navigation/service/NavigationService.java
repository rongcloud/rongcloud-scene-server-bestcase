package cn.rongcloud.mic.navigation.service;

import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.navigation.pojo.req.NavigationReq;
import cn.rongcloud.mic.navigation.pojo.req.UpdateNavigationReq;

public interface NavigationService {

	/**
	 * @Description:新增导航
	 * @Param: [data, jwtUser]
	 * @return: cn.rongcloud.mic.common.rest.RestResult
	 * @Author: gaoxin
	 * @Date: 2021/9/7
	 */
	RestResult addNavigation(NavigationReq data, JwtUser jwtUser);

	/**
	 * @Description:删除导航
	 * @Param: [id, jwtUser]
	 * @return: cn.rongcloud.mic.common.rest.RestResult
	 * @Author: gaoxin
	 * @Date: 2021/9/7
	 */
	RestResult deleteNavigation(Long id, JwtUser jwtUser);

	/**
	 * @Description:修改导航
	 * @Param: [data, jwtUser]
	 * @return: cn.rongcloud.mic.common.rest.RestResult
	 * @Author: gaoxin
	 * @Date: 2021/9/7
	 */
	RestResult updateNavigation(UpdateNavigationReq data, JwtUser jwtUser);

	/**
	 * @Description:根据id查询导航
	 * @Param: [id, jwtUser]
	 * @return: cn.rongcloud.mic.common.rest.RestResult
	 * @Author: gaoxin
	 * @Date: 2021/9/7
	 */
	RestResult getNavigationById(Long id, JwtUser jwtUser);

	/**
	 * @Description:获取导航列表
	 * @Param: []
	 * @return: cn.rongcloud.mic.common.rest.RestResult
	 * @Author: gaoxin
	 * @Date: 2021/9/7
	 */
	RestResult getNavigationList();

	/**
	 * @Description:修改导航生效与否
	 * @Param: [id, effectiveType, jwtUser]
	 * @return: cn.rongcloud.mic.common.rest.RestResult
	 * @Author: gaoxin
	 * @Date: 2021/9/7
	 */
	RestResult updateNavigationEffective(Long id, Integer effectiveType, JwtUser jwtUser);

}
