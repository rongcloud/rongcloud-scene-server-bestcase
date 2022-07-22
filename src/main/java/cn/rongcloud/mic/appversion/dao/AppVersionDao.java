package cn.rongcloud.mic.appversion.dao;

import cn.rongcloud.mic.appversion.model.TAppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by sunyinglong on 2020/6/15.
 */
@Repository
public interface AppVersionDao extends JpaRepository<TAppVersion, Long> {
    TAppVersion findTAppVersionByPlatformEqualsAndVersionEquals(String platform, String version);

    @Query(nativeQuery = true, value = "select * from t_appversion v  where v.platform=?1 order by v.id desc limit 1")
    TAppVersion findTAppVersionsByPlatformEquals(String platform);
}