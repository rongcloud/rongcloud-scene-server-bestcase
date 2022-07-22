package cn.rongcloud.mic.user.dao;

import cn.rongcloud.mic.user.model.TUserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowDao extends JpaRepository<TUserFollow, Long>, JpaSpecificationExecutor<TUserFollow> {

    TUserFollow findTUserFollowByUidEqualsAndAndFollowUidEquals(String uid, String followUid);



}
