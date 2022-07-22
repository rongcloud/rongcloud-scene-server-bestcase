package cn.rongcloud.mic.room.service;

import cn.rongcloud.common.im.IMHelper;
import cn.rongcloud.common.utils.IdentifierUtils;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.utils.SlideWindowUtil;
import cn.rongcloud.mic.room.pojos.callback.AuditCallBackContentDto;
import cn.rongcloud.mic.room.pojos.callback.AuditCallBackDto;
import cn.rongcloud.mic.shumei.entity.ShumeiAudit;
import cn.rongcloud.mic.shumei.enums.AuditTypeEnum;
import cn.rongcloud.mic.shumei.enums.RiskLevelEnum;
import cn.rongcloud.mic.shumei.key.ShumeiKey;
import cn.rongcloud.mic.shumei.message.ShumeiAuditFreezeMessage;
import cn.rongcloud.mic.shumei.service.ShumeiAuditService;
import cn.rongcloud.mic.user.enums.UserStatusEnum;
import cn.rongcloud.mic.user.service.UserService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class CallBackServiceImpl implements CallBackServcie {

    @Autowired
    private ShumeiAuditService shumeiAuditService;
    @Autowired
    private UserService userService;
    @Autowired
    IMHelper imHelper;
    @Autowired
    private RoomService roomService;

    @Override
    public RestResult auditSync(AuditCallBackDto dto) {
        log.info("audit sync dto:{}",dto);
        // 保存到数据库 只保存有问题的
        if(dto==null||dto.getType().intValue()== AuditTypeEnum.AUDIT_START.getValue()||dto.getType().intValue()==AuditTypeEnum.AUDIT_STOP.getValue()){
            return RestResult.success();
        }

        // 将有问题的用户 冻结
        if(AuditTypeEnum.AUDIT_CALLBACK.getValue()==dto.getType().intValue()) {
            AuditCallBackContentDto contentDto = dto.getContent();
            Integer riskLevel = contentDto.getRiskLevel();
            if (riskLevel.intValue() == RiskLevelEnum.PASS.getValue()||riskLevel.intValue()==RiskLevelEnum.REVIEW.getValue()) {
                return RestResult.success();
            }
            try {
                String uid = "_SYSTEM_";
                String userId = dto.getUserId();
                ShumeiAuditFreezeMessage message = new ShumeiAuditFreezeMessage();
                message.setUserId(userId);
                boolean go = SlideWindowUtil.isGo(userId, 3, 1000 * 60 * 5L);
                if (!go) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
                    Date freezeTime = new Date();

                    freezeTime = DateUtils.addMinutes(freezeTime, 2);
                    log.info("audit sync freezeTime:{}", sdf.format(freezeTime));

                    userService.freezeUser(userId, UserStatusEnum.FREEZE.getValue(), freezeTime);
                    //发消息
                    message.setStatus(2);
                    message.setMessage(String.format(ShumeiKey.FREEZE_MESSAGE, sdf.format(freezeTime)));
                    imHelper.publishSysMessage(uid, Lists.newArrayList(userId), message);

                    // 关房间
                    roomService.chrmDelete(dto.getRoomId());

                }else{
                    // 如果没有达到冻结标准，那么发送消息，App 端谈提示消息
                    message.setStatus(1);
                    message.setMessage(contentDto.getDesc());
                    imHelper.publishSysMessage(uid, Lists.newArrayList(userId), message);
                }
                ShumeiAudit shumeiAudit = new ShumeiAudit();
                BeanUtils.copyProperties(dto,shumeiAudit);
                shumeiAudit.setUid(IdentifierUtils.uuid());
                shumeiAudit.setContent(JSON.toJSONString(dto.getContent()));
                shumeiAuditService.save(shumeiAudit);
            } catch (Exception e) {
                log.error("audit sync error message:{}",e.getMessage(),e);
            }

        }
        return RestResult.success();
    }
}
