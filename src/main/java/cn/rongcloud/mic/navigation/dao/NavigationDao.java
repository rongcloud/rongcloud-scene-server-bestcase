package cn.rongcloud.mic.navigation.dao;

import java.util.Date;

import cn.rongcloud.mic.navigation.model.TNavigation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *@Author gaoxin
 *@Date 2021/9/7 3:04 下午
 *@Version 1.0
 **/
@Repository
public interface NavigationDao extends JpaRepository<TNavigation, Long> {

	TNavigation findTNavigationById(Long id);

	@Transactional
	@Modifying
	@Query(value = "update TNavigation t  set t.effectiveType=?2,t.updateUser=?3,t.updateDt=?4 where t.id=?1")
	int updateNavigationEffective(Long id, Integer effectiveType, String uid, Date date);
}
