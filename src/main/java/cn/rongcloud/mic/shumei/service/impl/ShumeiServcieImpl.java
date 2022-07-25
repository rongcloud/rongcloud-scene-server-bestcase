package cn.rongcloud.mic.shumei.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.rongcloud.mic.common.rest.RestResult;
import cn.rongcloud.mic.common.rest.RestResultCode;
import cn.rongcloud.mic.common.utils.ShumeiUtils;
import cn.rongcloud.mic.shumei.pojos.ShumeiResp;
import cn.rongcloud.mic.shumei.pojos.ShumeiRiskReq;
import cn.rongcloud.mic.shumei.service.ShumeiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;


@Service
@Slf4j
public class ShumeiServcieImpl implements ShumeiService {

    @Value("${shumei.imageUrl}")
    private String imageUrl;
    @Value("${shumei.textUrl}")
    private String textUrl;
    @Value("${shumei.riskUrl}")
    private String riskUrl;
    @Value("${shumei.accessKey}")
    private String ACCESS_KEY;
    @Value("${shumei.appKey}")
    private String appKey;

    @Override
    public RestResult imageAudit(String url){
        HashMap<String, Object> payload = new HashMap<String, Object>();
        payload.put("type", "POLITICS_PORN_OCR_AD_LOGO_MINOR_SCREEN_SCENCE_QR_VIOLENCE_BAN");
        payload.put("eventId","headImage");

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("img", url);

        return execute(payload,data,imageUrl);
    }

    @Override
    public RestResult textAudit(String text) {
        HashMap<String, Object> payload = new HashMap<String, Object>();
        payload.put("eventId", "nickname");
        payload.put("type", "ALL");

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("text", text);

        return execute(payload,data,textUrl);
    }

    @Override
    public ShumeiResp riskCheck(ShumeiRiskReq req) {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("accessKey", ACCESS_KEY);
        payload.put("appId", appKey);
        payload.put("eventId","login");
        payload.put("data",JSONUtil.parseObj(req));
        JSONObject result = ShumeiUtils.httpPost(riskUrl, JSONUtil.parseObj(payload));
        ShumeiResp shumeiResp = JSONUtil.toBean(result, ShumeiResp.class);
        log.info("shumei riskCheck param:{}, result:{}",JSONUtil.parseObj(payload),shumeiResp);
        return shumeiResp;
    }

    private RestResult execute(HashMap<String, Object> payload,HashMap<String, Object> data,String url){
        data.put("tokenId", UUID.randomUUID());
        payload.put("accessKey", ACCESS_KEY);
        payload.put("appId", "default");
        payload.put("data", data);
        JSONObject json = JSONUtil.parseObj(payload);
        JSONObject result = ShumeiUtils.httpPost(url, json);
        ShumeiResp shumeiResp = JSONUtil.toBean(result, ShumeiResp.class);
        log.info("shumei audit url:{} param:{}, result:{}",url,json,shumeiResp);
        if(shumeiResp.getCode().intValue()==1100&&"PASS".equals(shumeiResp.getRiskLevel())){
            return RestResult.success();
        }
        if(shumeiResp.getCode().intValue()==1902){
            return RestResult.generic(shumeiResp.getCode(),"必须是清晰有效的图片，请重新上传");
        }
        if(shumeiResp.getCode().intValue()==1903){
            return RestResult.generic(shumeiResp.getCode(),"数美审核服务器连接失败，请联系客服人员。");
        }
        return RestResult.generic(RestResultCode.ERR_SHUMEI_AUDIT_FAIL);
    }

}
