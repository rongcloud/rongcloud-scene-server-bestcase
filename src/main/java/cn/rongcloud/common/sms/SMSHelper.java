package cn.rongcloud.common.sms;

import cn.rongcloud.common.utils.HttpHelper;
import cn.rongcloud.common.sms.pojos.SMSCodeVerifyResult;
import cn.rongcloud.common.sms.pojos.SMSSendCodeResult;
import cn.rongcloud.common.utils.GsonUtil;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Slf4j
@Component
public class SMSHelper {

    private static final String UTF8 = "UTF-8";

    @Autowired
    HttpHelper httpHelper;

    /**
     * 发送短信验证码方法。
     *
     * @param mobile:接收短信验证码的目标手机号，每分钟同一手机号只能发送一次短信验证码，同一手机号 1 小时内最多发送 3 次。（必传）
     * @param templateId:短信模板 Id，在开发者后台->短信服务->服务设置->短信模版中获取。（必传）
     * @param region:手机号码所属国家区号，目前只支持中图区号 86）
     * @param verifyId:图片验证标识 Id ，开启图片验证功能后此参数必传，否则可以不传。在获取图片验证码方法返回值中获取。
     * @return SMSSendCodeResult
     **/
    public SMSSendCodeResult sendCode(String mobile, String templateId, String region,
        String verifyId, String verifyCode) throws Exception {
        log.info(
            "---into SMS.sendCode, mobile: " + mobile + ",templateId: " + templateId
                + ",region: " + region + ",verifyId: " + verifyId + ",verifyCode: " + verifyCode);
        if (mobile == null) {
            throw new IllegalArgumentException("Paramer 'mobile' is required");
        }

        if (templateId == null) {
            throw new IllegalArgumentException("Paramer 'templateId' is required");
        }

        if (region == null) {
            throw new IllegalArgumentException("Paramer 'region' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&mobile=").append(URLEncoder.encode(mobile, UTF8));
        sb.append("&templateId=").append(URLEncoder.encode(templateId, UTF8));
        sb.append("&region=").append(URLEncoder.encode(region, UTF8));

        if (verifyId != null) {
            sb.append("&verifyId=").append(URLEncoder.encode(verifyId, UTF8));
        }

        if (verifyCode != null) {
            sb.append("&verifyCode=").append(URLEncoder.encode(verifyCode, UTF8));
        }
        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }
        log.info("---begin sms request--");
        HttpURLConnection conn = httpHelper
            .createSMSPostHttpConnection("/sendCode.json", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (SMSSendCodeResult) GsonUtil.fromJson(httpHelper.returnResult(conn), SMSSendCodeResult.class);
    }

    /**
     * 验证码验证方法
     *
     * @return SMSCodeVerifyResult
     **/
    public SMSCodeVerifyResult verifyCode(String sessionId, String code) {
        if (sessionId == null) {
            throw new IllegalArgumentException("Paramer 'sessionId' is required");
        }

        if (code == null) {
            throw new IllegalArgumentException("Paramer 'code' is required");
        }

        SMSCodeVerifyResult result = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("&sessionId=").append(URLEncoder.encode(sessionId, UTF8));
            sb.append("&code=").append(URLEncoder.encode(code, UTF8));
            String body = sb.toString();
            if (body.indexOf("&") == 0) {
                body = body.substring(1, body.length());
            }

            HttpURLConnection conn = httpHelper
                .createSMSPostHttpConnection("/verifyCode.json", "application/x-www-form-urlencoded");
            httpHelper.setBodyParameter(body, conn);
            result = (SMSCodeVerifyResult) GsonUtil.fromJson(httpHelper.returnResult(conn), SMSCodeVerifyResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    public SMSSendCodeResult sendInternationalCode(String mobile, String templateId, String region,String p1) throws Exception {
        log.info("send internation sms mobile:{},templateId:{},region:{},p1:{}",mobile,templateId,region,p1);
        if (mobile == null) {
            throw new IllegalArgumentException("Paramer 'mobile' is required");
        }
        if (templateId == null) {
            throw new IllegalArgumentException("Paramer 'templateId' is required");
        }
        if (region == null) {
            throw new IllegalArgumentException("Paramer 'region' is required");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("&mobile=").append(URLEncoder.encode(mobile, UTF8));
        sb.append("&templateId=").append(URLEncoder.encode(templateId, UTF8));
        sb.append("&region=").append(URLEncoder.encode(region, UTF8));
        sb.append("&p1=").append(URLEncoder.encode(p1, UTF8));

        String body = sb.toString();
        if (body.indexOf("&") == 0) {
            body = body.substring(1, body.length());
        }
        HttpURLConnection conn = httpHelper
                .createSMSPostHttpConnection("/sendIntl.json ", "application/x-www-form-urlencoded");
        httpHelper.setBodyParameter(body, conn);

        return (SMSSendCodeResult) GsonUtil.fromJson(httpHelper.returnResult(conn), SMSSendCodeResult.class);
    }


}
