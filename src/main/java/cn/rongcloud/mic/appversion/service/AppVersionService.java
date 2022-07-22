package cn.rongcloud.mic.appversion.service;


import cn.rongcloud.mic.appversion.pojos.ReqAppVersionCreate;
import cn.rongcloud.mic.appversion.pojos.ReqAppVersionUpdate;
import cn.rongcloud.mic.appversion.pojos.RespAppVersion;
import cn.rongcloud.mic.common.rest.RestResult;

/**
 * Created by sunyinglong on 2020/6/3
 */
public interface AppVersionService {

    RestResult publishAppVersion(ReqAppVersionCreate data);

    RestResult updateAppVersion(String platform, String version, ReqAppVersionUpdate data);

    RestResult deleteAppVersion(String platform, String version);

    RestResult<RespAppVersion> getLatestAppVersion(String platform);

}
