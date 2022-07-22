package cn.rongcloud.mic.user.service;

import cn.rongcloud.common.utils.DateTimeUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.jwt.JwtUser;
import cn.rongcloud.mic.user.dao.UserFollowDao;
import cn.rongcloud.mic.user.mapper.UserFollowMapper;
import cn.rongcloud.mic.user.model.TUser;
import cn.rongcloud.mic.user.model.TUserFollow;
import cn.rongcloud.mic.user.pojos.ResFollowInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserFollowServiceImpl implements UserFollowService {


    @Autowired
    private UserFollowDao userFollowDao;

    @Autowired
    private UserService userService;

    @Resource
    private UserFollowMapper userFollowMapper;

    @Override
    public RestResult follow(JwtUser jwtUser, String userId) {
        //关注操作，如果已关注则取消关注，否则去关注
        TUserFollow follow = userFollowDao.findTUserFollowByUidEqualsAndAndFollowUidEquals(jwtUser.getUserId(), userId);
        Date curDate = DateTimeUtils.currentUTC();
        System.out.println(follow);
        if (follow == null) {
            follow = new TUserFollow();
            follow.setUid(jwtUser.getUserId());
            follow.setFollowUid(userId);
            follow.setStatus(0);
            follow.setCreateDt(curDate);
            follow.setUpdateDt(curDate);
        } else {
            follow.setStatus(follow.getStatus() > 0 ? 0 : 1);
            follow.setUpdateDt(curDate);
        }
        userFollowDao.save(follow);

        return RestResult.success();
    }

    @Override
    public RestResult followList(JwtUser jwtUser, Integer type, Integer page, Integer size) {
        List<String> userIds = new ArrayList<>();
        Page<TUserFollow> pageList = findByUidList(jwtUser.getUserId(), page, size, type, null);
        List<TUserFollow> follows = pageList.getContent();
        if (follows != null && !follows.isEmpty()) {
            if (type.equals(1)) {
                userIds = follows.stream().map(TUserFollow::getFollowUid).collect(Collectors.toList());
            } else {
                userIds = follows.stream().map(TUserFollow::getUid).collect(Collectors.toList());
            }
        }
        Page<TUserFollow> fansList = findByUidList(jwtUser.getUserId(), 1, size, type, userIds);
        List<TUserFollow> fans = fansList.getContent();
        List<String> fansIds = new ArrayList<>();
        if (fans != null && !fans.isEmpty()) {
            if (type.equals(1)) {
                fansIds = fans.stream().map(TUserFollow::getUid).collect(Collectors.toList());
            } else {
                fansIds = fans.stream().map(TUserFollow::getFollowUid).collect(Collectors.toList());
            }
        }
        System.out.println(follows);
        List<ResFollowInfo> res = new ArrayList<>();
        for (TUserFollow follow : follows) {
            ResFollowInfo followInfo = new ResFollowInfo();
            TUser userInfo = null;
            if (type.equals(1)) {
                userInfo = userService.getUserInfo(follow.getFollowUid());
            } else {
                userInfo = userService.getUserInfo(follow.getUid());
            }

            followInfo.setUserId(userInfo.getUid());
            followInfo.setUserName(userInfo.getName());
            followInfo.setPortrait(userInfo.getPortrait());
            followInfo.setStatus(0);
            if (fansIds.contains(userInfo.getUid())) {
                followInfo.setStatus(1);
            }
            res.add(followInfo);

        }
        HashMap<String, Object> restotal = new HashMap();
        restotal.put("total",pageList.getTotalElements());
        restotal.put("list",res);
        return RestResult.success(restotal);
    }

    @Override
    public Page<TUserFollow> findByUidList(String uid, Integer page, Integer size, Integer type, List<String> userIds) {
                page = page -1;
                PageRequest pageAble = PageRequest.of(page, size, Sort.by(Direction.DESC, "id"));
                Specification<TUserFollow> specification = new Specification<TUserFollow>() {
                    public Predicate toPredicate(Root<TUserFollow> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (CollectionUtils.isEmpty(userIds)) {
                    if (type.equals(1)) {//我关注的
                        predicates.add(cb.equal(root.<String>get("uid"), uid));
                    } else {
                        predicates.add(cb.equal(root.<String>get("followUid"), uid));
                    }
                }
                if (!CollectionUtils.isEmpty(userIds)) {
                    if (type.equals(1)) {// 我关注的，获取是否也关注我了
                        predicates.add(cb.equal(root.<String>get("followUid"), uid));
                        predicates.add(cb.in(root.<List<String>>get("uid")).value(userIds));
                    } else {
                        predicates.add(cb.equal(root.<String>get("uid"), uid));
                        predicates.add(cb.in(root.<List<String>>get("followUid")).value(userIds));
                    }
                }
                predicates.add(cb.equal(root.<Integer>get("status"), 0));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return userFollowDao.findAll(specification, pageAble);
    }

    @Override
    public void deleteByUserId(String userId) {
        userFollowMapper.deleteByUserId(userId);
    }
}
