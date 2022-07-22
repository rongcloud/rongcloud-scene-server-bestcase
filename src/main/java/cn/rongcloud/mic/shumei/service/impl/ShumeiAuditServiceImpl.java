package cn.rongcloud.mic.shumei.service.impl;

import cn.rongcloud.mic.shumei.entity.ShumeiAudit;
import cn.rongcloud.mic.shumei.mapper.ShumeiAuditMapper;
import cn.rongcloud.mic.shumei.service.ShumeiAuditService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShumeiAuditServiceImpl implements ShumeiAuditService {

    @Resource
    ShumeiAuditMapper shumeiAuditMapper;

    @Override
    public void save(ShumeiAudit shumeiAudit) {
        shumeiAuditMapper.insert(shumeiAudit);
    }

}
