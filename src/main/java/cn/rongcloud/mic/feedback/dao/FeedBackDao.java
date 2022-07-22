package cn.rongcloud.mic.feedback.dao;

import cn.rongcloud.mic.feedback.model.TFeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedBackDao extends JpaRepository<TFeedBack, Long>, JpaSpecificationExecutor<TFeedBack> {

}
