package cn.rongcloud.mic.common.jwt;

/**
 * Created by sunyinglong on 2020/6/25
 */

import cn.rongcloud.common.utils.GsonUtil;
import cn.rongcloud.common.utils.IdentifierUtils;
import cn.rongcloud.common.utils.SecurityUtils;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

public class JwtTokenHelper {
    private static final String ISSUER = "cn/rongcloud";
    private static final String CLAIM_MARK = "mark";
    private static final String CLAIM_DATA = "data";
    private String secret;
    private long tokenTTLInMilliSec;

    public JwtTokenHelper(String secret, long tokenTTLInMilliSec) {
        this.secret = secret;
        this.tokenTTLInMilliSec = tokenTTLInMilliSec;
    }

    public JwtTokenHelper(String secret) {
        this(secret, Long.MAX_VALUE);
    }

    /**
     * @param tokenTTLInMilliSec
     * @param tokenData
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public JwtToken createJwtToken(long tokenTTLInMilliSec, JwtUser tokenData) throws IOException {
        boolean neverExpire = -1 == tokenTTLInMilliSec;

        JwtToken token = new JwtToken();
        token.setUserId(tokenData.getUserId());
        token.setTokenId(IdentifierUtils.uuid());
        token.setIssuedTime(System.currentTimeMillis());
        token.setExpiredTime(neverExpire ? -1 : token.getIssuedTime() + tokenTTLInMilliSec);
        String mark = SecurityUtils.generateSalt(16);
        token.setToken(Jwts.builder().signWith(SignatureAlgorithm.HS256, getKey(secret, mark)).setIssuer(ISSUER)
                .setId(token.getTokenId()).setIssuedAt(new Date(token.getIssuedTime()))
                .setExpiration(neverExpire ? null : new Date(token.getExpiredTime())).claim(CLAIM_MARK, mark)
                .claim(CLAIM_DATA, GsonUtil.toJson(tokenData)).compact());

        return token;
    }

    /**
     * @param tokenData
     * @return
     */
    public JwtToken createJwtToken(JwtUser tokenData) {
        try {
            return createJwtToken(tokenTTLInMilliSec, tokenData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JwtUser checkJwtToken(String token) throws IOException {
        return (JwtUser) GsonUtil.fromJson((String) checkJwtTokenInternal(token, secret).get(CLAIM_DATA),
                JwtUser.class);
    }

    /**
     * check jwt token and return payload
     *
     * @param token
     * @param secret
     * @return
     * @throws ExpiredJwtException
     * @throws MalformedJwtException
     * @throws IOException
     */
    private static Claims checkJwtTokenInternal(String token, String secret) throws IOException {
        String[] contents = token.split("\\.");
        if (contents.length == 3) {
            Map<String, Object> map = (Map<String, Object>) GsonUtil.fromJson(new String(
                Base64.decodeBase64(contents[1])), Map.class);
            byte[] key = getKey(secret, (String) map.get(CLAIM_MARK));
            // check token
            Jws<Claims> jws = Jwts.parser().setSigningKey(key).requireIssuer(ISSUER).parseClaimsJws(token);
            return jws.getBody();
        }

        throw new MalformedJwtException("the token format is wrong!");
    }

    private static byte[] getKey(String secret, String nonce) throws IOException {
        return SecurityUtils.encryptHMAC(secret, nonce, SecurityUtils.HmacAlgorithm.HMAC_MD5);
    }
}