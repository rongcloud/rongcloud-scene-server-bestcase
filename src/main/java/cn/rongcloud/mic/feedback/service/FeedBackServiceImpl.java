package cn.rongcloud.mic.feedback.service;

import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.feedback.dao.FeedBackDao;
import cn.rongcloud.mic.feedback.model.TFeedBack;
import cn.rongcloud.mic.feedback.pojos.RequestFeedback;
import cn.rongcloud.mic.common.jwt.JwtUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FeedBackServiceImpl implements FeedBackService {


    @Autowired
    private FeedBackDao feedBackDao;

    @Override
    public RestResult add(RequestFeedback requestFeedback, JwtUser jwtUser) {
        //验证暂时不加
        try {
            TFeedBack feedBack = new TFeedBack();
            feedBack.setCreateDt(new Date());
            feedBack.setType(0);
            feedBack.setGood(requestFeedback.getIsGoodFeedback());
            feedBack.setUserId(jwtUser.getUserId());
            feedBack.setReason(requestFeedback.getReason());

            feedBackDao.save(feedBack);
            return RestResult.success();
        } catch (Exception e) {
            return RestResult.generic(RestResultCode.ERR_REQUEST_PARA_ERR);
        }

    }

    @Override
    public RestResult<Page<TFeedBack>> list(Integer page, Integer size, String userId) {
        Specification<TFeedBack> specification = new Specification<TFeedBack>() {
            @Override
            public Predicate toPredicate(Root<TFeedBack> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (StringUtils.isNotBlank(userId)) {
                    predicates.add(cb.equal(root.<String>get("userId"), userId));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        Page<TFeedBack> list = feedBackDao.findAll(specification, PageRequest.of(page, size, Sort.Direction.DESC, "id"));
        return RestResult.success(list);
    }
}
