package cn.rongcloud.common.utils;

import cn.rongcloud.common.im.config.IMProperties;
import cn.rongcloud.common.im.config.RCXFileProperties;
import cn.rongcloud.common.sms.config.SMSProperties;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sunyinglong on 2020/6/25
 */
@Slf4j
@Component
public class HttpHelper {
    private static final String APPKEY = "App-Key";
    private static final String NONCE = "Nonce";
    private static final String TIMESTAMP = "Timestamp";
    private static final String SIGNATURE = "Signature";

    private SSLContext sslCtx = null;

    @Autowired
    IMProperties imProperties;

    @Autowired
    SMSProperties smsProperties;

    @Autowired
    RCXFileProperties rcxFileProperties;

    @PostConstruct
    private void init() {
        log.info("init HttpHelper");
        try {
            sslCtx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslCtx.init(null, new TrustManager[]{tm}, null);
        } catch (Exception e) {
            log.error("SSLContext exception:{}", e);
        }

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }

        });

        HttpsURLConnection.setDefaultSSLSocketFactory(sslCtx.getSocketFactory());
    }

    public HttpURLConnection createGetHttpConnection(String uri) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(30000);
        conn.setRequestMethod("GET");
        return conn;
    }

    public void setBodyParameter(String str, HttpURLConnection conn) {
        log.info("Call IM server api with url: {}, data: {}", conn.getURL().toString(), str);
        try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
            out.write(str.getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public HttpURLConnection createCommonPostHttpConnection(String host, String appKey,
        String appSecret, String uri, String contentType) throws IOException {
        String nonce = IMSignUtil.getNonce();
        String timestamp = IMSignUtil.getTimestamp();
        String sign = IMSignUtil.toSign(appSecret, nonce, timestamp);
        uri = host + uri;
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn == null) {
            log.info("open url connectin fail, url={}", uri);
            return null;
        }

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        conn.setRequestProperty(APPKEY, appKey);
        conn.setRequestProperty(NONCE, nonce);
        conn.setRequestProperty(TIMESTAMP, timestamp);
        conn.setRequestProperty(SIGNATURE, sign);
        conn.setRequestProperty("Content-Type", contentType);
        return conn;
    }

    public HttpURLConnection createCommonGetHttpConnection(String host, String appKey,
        String appSecret, String uri, String contentType) throws IOException {
        String nonce = IMSignUtil.getNonce();
        String timestamp = IMSignUtil.getTimestamp();
        String sign = IMSignUtil.toSign(appSecret, nonce, timestamp);
        URL url = new URL(host + uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn == null) {
            log.info("open url connectin fail, url={}", uri);
            return null;
        }

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        conn.setRequestProperty(APPKEY, appKey);
        conn.setRequestProperty(NONCE, nonce);
        conn.setRequestProperty(TIMESTAMP, timestamp);
        conn.setRequestProperty(SIGNATURE, sign);
        conn.setRequestProperty("Content-Type", contentType);
        return conn;
    }

    public HttpURLConnection createIMGetHttpConnection(String uri, String contentType) throws IOException {
        return createCommonGetHttpConnection(imProperties.getHost(), imProperties.getAppKey(), imProperties.getSecret(), uri,
            contentType);
    }

    public HttpURLConnection createIMPostHttpConnection(String uri, String contentType)
        throws IOException {
        return createCommonPostHttpConnection(imProperties.getHost(), imProperties.getAppKey(), imProperties.getSecret(), uri,
            contentType);
    }

    public HttpURLConnection createSMSPostHttpConnection(String uri, String contentType) throws IOException {
        return createCommonPostHttpConnection(smsProperties.getHost(), smsProperties.getAppKey(), smsProperties.getSecret(), uri,
            contentType);
    }

    public HttpURLConnection createHereWhitePostHttpConnection(String host, String uri, String contentType, String token) throws IOException {
        uri = host + uri;
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn == null) {
            log.info("open url connectin fail, url={}", uri);
            return null;
        }

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setRequestProperty("token", token);
        conn.setRequestProperty("Content-Type", contentType);
        return conn;
    }

    public HttpURLConnection createRongAdminPostHttpConnection(String host, String uri, String contentType) throws IOException {
        uri = host + uri;
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn == null) {
            log.info("open url connectin fail, url={}", uri);
            return null;
        }

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setRequestProperty("Content-Type", contentType);
        return conn;
    }

    public HttpURLConnection createHereWhitePatchHttpConnection(String host, String uri, String contentType, String token) throws IOException {
        URL url = new URL(host + uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("PATCH");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setRequestProperty("token", token);
        conn.setRequestProperty("Content-Type", contentType);
        return conn;
    }

    public HttpURLConnection createWhiteBoardPostHttpConnection(String host, String uri, String contentType)
            throws MalformedURLException, IOException, ProtocolException {

        URL url = new URL(host + uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        conn.setRequestProperty("Content-Type", contentType);

        return conn;
    }

    public HttpURLConnection createRCXMediaGetHttpConnection(String uri, String contentType) throws IOException {
        return createCommonGetHttpConnection(rcxFileProperties.getHost(), rcxFileProperties.getAppKey(),
                rcxFileProperties.getSecret(), uri, contentType);
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    public String returnResult(HttpURLConnection conn) throws Exception {
        InputStream input = null;
        String result = "";
        try {
            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 201) {
                input = conn.getInputStream();
            } else {
                input = conn.getErrorStream();
            }
            result = new String(readInputStream(input), StandardCharsets.UTF_8);
        } catch (UnknownHostException e) {
            result = getExceptionMessage("request:" + conn.getURL() + " ,UnknownHostException:" + e.getMessage());
        } catch (SocketTimeoutException e) {
            result = getExceptionMessage("request:" + conn.getURL() + " ,SocketTimeoutException:" + e.getMessage());
        } catch (IOException e) {
            result = getExceptionMessage("request:" + conn.getURL() + " ,IOException:" + e.getMessage());
        } catch (Exception e) {
            throw e;
        }
        log.info("IM server api response:{}", result);
        return result;
    }

    private static String getExceptionMessage(String error) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 400);
        result.put("msg", error);
        return GsonUtil.toJson(result);
    }


    public HttpURLConnection createPostHttpConnection(String uri, String contentType)
        throws IOException {

        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn == null) {
            log.info("open url connection fail, url={}", uri);
            return null;
        }
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setRequestProperty("Content-Type", contentType);
        return conn;
    }
}
