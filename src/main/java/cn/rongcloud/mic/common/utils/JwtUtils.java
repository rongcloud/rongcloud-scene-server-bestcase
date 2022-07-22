package cn.rongcloud.mic.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxuesong
 */
@Slf4j
public class JwtUtils {


    public static final String JWT_SECRET = "8677df7fc3a34e26a61c034d5ec8245d";

    public static final String Key_SECRET = "5677df7fc3a34e25a61c034d5e545d";

    public static String generateToken(String businessKey,String buinessValue) {
        //签名算法，选择SHA-256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //将BASE64SECRET常量字符串使用base64解码成字节数组
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JWT_SECRET);
        //使用HmacSHA256签名算法生成一个HS256的签名秘钥Key
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //添加构成JWT的参数
        Map<String, Object> headMap = new HashMap<String, Object>(4);
        headMap.put("alg", SignatureAlgorithm.HS256.getValue());
        headMap.put("typ", "JWT");
        JwtBuilder builder = Jwts.builder()
                .setHeader(headMap)
                .setIssuedAt(new Date())
                //加密后的客户编号
                .claim(businessKey, AESSecretUtil.encryptToStr(buinessValue, Key_SECRET))
                //Signature
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        return builder.compact();
    }


    private static Claims parseJWT(String jsonWebToken) {
        Claims claims = null;
        try {
            if (StringUtils.isNotBlank(jsonWebToken)) {
                //解析jwt
                claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(JWT_SECRET))
                        .parseClaimsJws(jsonWebToken).getBody();
            }else {
                log.warn("[JWTHelper]-json web token 为空");
            }
        } catch (Exception e) {
            log.error("[JWTHelper]-JWT解析异常：可能因为token已经超时或非法token");
        }
        return claims;
    }

    public static String getBusinessValue(String token,String businessKey) {
        if(StringUtils.isBlank(token)){
            return null;
        }
        Claims claims = parseJWT(token);
        if(claims==null){
            return null;
        }
        String businessValue = AESSecretUtil.decryptToStr((String)claims.get(businessKey), Key_SECRET);
        return businessValue;
    }
}
