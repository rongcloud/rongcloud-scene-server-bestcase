package cn.rongcloud.mic.shumei.service;

import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.shumei.pojos.ShumeiResp;
import cn.rongcloud.mic.shumei.pojos.ShumeiRiskReq;

public interface ShumeiService {

    RestResult imageAudit(String imageUrl);

    RestResult textAudit(String text);

    ShumeiResp riskCheck(ShumeiRiskReq req);

}
