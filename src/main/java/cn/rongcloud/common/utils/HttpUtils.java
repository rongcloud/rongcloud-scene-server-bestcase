package cn.rongcloud.common.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

@Slf4j
public class HttpUtils {

//    设置超时时间
//    RequestConfig requestConfig = RequestConfig.custom()
//            .setConnectTimeout(3000) //设置连接超时时间，单位毫秒  ConnectionTimeOutException
//            .setConnectionRequestTimeout(1000) //设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。
//            .setSocketTimeout(3000) //请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用  SocketTimeOutException
//            .build();
//
//            post.setConfig(requestConfig);

    /**
     * 返回成功状态码
     */
    private static final int SUCCESS_CODE = 200;
    /**
     * 建立连接  数据返回  断开连接的 总时间
     */
    private static final int CONNECT_TIMEOUT = 5000;
    /**
     * 数据返回的时间
     */
    private static final int SOCKET_TIMEOUT = 5000;

    /**
     * 发送GET请求
     *
     * @param url      请求url
     * @param paramMap 请求参数
     * @return JSON或者字符串
     * @throws Exception
     */
    public static String sendGet(String url, Map paramMap, String headerKey, String headerValue) throws Exception {


        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {

            //    设置超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT) //设置连接超时时间，单位毫秒   和服务器建立连接的timeout
//                    .setConnectionRequestTimeout(1000) //设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。 从连接池获取连接的timeout
                .setSocketTimeout(SOCKET_TIMEOUT) //请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用  从服务器读取数据的timeout
                .build();

            /**
             * 创建HttpClient对象
             */
            client = HttpClients.createDefault();
            /**
             * 创建URIBuilder
             */
            URIBuilder uriBuilder = new URIBuilder(url);
            /**
             * 设置参数
             */
            if (paramMap != null && paramMap.size() > 0) {
                List<NameValuePair> nameValuePairList = new ArrayList<>();
                for (Object key : paramMap.keySet()) {
                    nameValuePairList.add(new BasicNameValuePair(key + "", paramMap.get(key) + ""));
                }
                uriBuilder.addParameters(nameValuePairList);
            }
            /**
             * 创建HttpGet
             */
            HttpGet httpGet = new HttpGet(uriBuilder.build());

            httpGet.setConfig(requestConfig);

            /**
             * 设置请求头部编码
             */
            httpGet.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
            /**
             * 设置返回编码
             */
            if (StringUtils.isNotBlank(headerKey) && StringUtils.isNotBlank(headerValue)) {
                httpGet.setHeader(new BasicHeader(headerKey, headerValue));
            }

            httpGet.setHeader(new BasicHeader("Accept", "*/*"));
            /**
             * 请求服务
             */
            response = client.execute(httpGet);
            log.info("client请求返回,{}", response);
            /**
             * 获取响应吗
             */
            int statusCode = response.getStatusLine().getStatusCode();

            if (SUCCESS_CODE == statusCode) {
                /**
                 * 获取返回对象
                 */
                Header[] responses = response.getHeaders("response");
                HttpEntity entity = response.getEntity();
                /**
                 * 通过EntityUitls获取返回内容
                 */
                return EntityUtils.toString(entity, "UTF-8");

            } else {
                log.error("HttpClientService-line: {}, errorMsg{}", 97, "GET请求失败！");
                HttpEntity entity = response.getEntity();
                /**
                 * 通过EntityUitls获取返回内容
                 */
                return response.toString();
            }
        } catch (Exception e) {
            log.error("HttpClientService-line: {}, Exception: {}", 100, e);
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }

        }
        return null;
    }

    /**
     * 发送GET请求
     *
     * @param url        请求url
     * @param deviceKey
     * @param deviceType
     * @param appMtime
     * @param agent
     * @param uid
     * @param sign
     * @return JSON或者字符串
     * @throws Exception
     */
    public static String sendUserInfo(String url, String deviceKey, String deviceType, String appMtime, String agent, String uid, String sign) throws Exception {

        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {

            //    设置超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT) //设置连接超时时间，单位毫秒   和服务器建立连接的timeout
//                    .setConnectionRequestTimeout(1000) //设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。 从连接池获取连接的timeout
                .setSocketTimeout(SOCKET_TIMEOUT) //请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用  从服务器读取数据的timeout
                .build();

            /**
             * 创建HttpClient对象
             */
            client = HttpClients.createDefault();
            /**
             * 创建URIBuilder
             */
            URIBuilder uriBuilder = new URIBuilder(url);
            /**
             * 创建HttpGet
             */
            HttpGet httpGet = new HttpGet(uriBuilder.build());

            httpGet.setConfig(requestConfig);

            /**
             * 设置请求头部编码
             */
            httpGet.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));

            httpGet.setHeader(new BasicHeader("Accept", "*/*"));

            // 设备唯一ID
            httpGet.setHeader(new BasicHeader("App-Device-Key", deviceKey));

            //设备类型	0:ios  1:安卓
            httpGet.setHeader(new BasicHeader("App-Device-Type", deviceType));

            httpGet.setHeader(new BasicHeader("App-Mtime", appMtime));

            httpGet.setHeader(new BasicHeader("APP-Agent", agent));

            httpGet.setHeader(new BasicHeader("App-User-Pid", uid));

            httpGet.setHeader(new BasicHeader("App-Sign", sign));

            log.info("发送APP登录校验,请求头信息--------App-Device-Key:{}" +
                    "--App-Device-Type:{}---App-Mtime:{}---APP-Agent:{}---App-User-Pid:{}----App-Sign:{}",
                deviceKey, deviceType, appMtime, agent, uid, sign);

            /**
             * 请求服务
             */
            response = client.execute(httpGet);
            log.info("client请求返回,{}", response);
            /**
             * 获取响应吗
             */
            int statusCode = response.getStatusLine().getStatusCode();

            if (SUCCESS_CODE == statusCode) {
                /**
                 * 获取返回对象
                 */
                Header[] responses = response.getHeaders("response");
                HttpEntity entity = response.getEntity();
                /**
                 * 通过EntityUitls获取返回内容
                 */
                return EntityUtils.toString(entity, "UTF-8");

            } else {
                log.error("HttpClientService-line: {}, errorMsg{}", 97, "GET请求失败！");
                HttpEntity entity = response.getEntity();
                /**
                 * 通过EntityUitls获取返回内容
                 */
                return response.toString();
            }
        } catch (Exception e) {
            log.error("HttpClientService-line: {}, Exception: {}", 100, e);
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
        return null;
    }

    /**
     * 发送POST请求
     *
     * @param url
     * @param paramMap
     * @return JSON或者字符串
     * @throws Exception
     */
    public static Object sendPost(String url, Map paramMap, String headerKey, String headerValue) throws Exception {
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {

            //    设置超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT) //设置连接超时时间，单位毫秒   和服务器建立连接的timeout
//                    .setConnectionRequestTimeout(1000) //设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。 从连接池获取连接的timeout
                .setSocketTimeout(SOCKET_TIMEOUT) //请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用  从服务器读取数据的timeout
                .build();

            /**
             *  创建一个httpclient对象
             */
            client = HttpClients.createDefault();
            /**
             * 创建一个post对象
             */
            HttpPost post = new HttpPost(url);

            post.setConfig(requestConfig);

            /**
             * 包装成一个Entity对象
             */
            if (paramMap != null && paramMap.size() > 0) {
                List<NameValuePair> nameValuePairList = new ArrayList<>();
                for (Object key : paramMap.keySet()) {
                    nameValuePairList.add(new BasicNameValuePair(key + "", paramMap.get(key) + ""));
                }
                StringEntity entity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
                /**
                 * 设置请求的内容
                 */
                post.setEntity(entity);
            }

            /**
             * 设置请求的报文头部的编码
             */
//            post.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
            post.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));

            // 登录接口 加一个请求头
            if (StringUtils.isNotBlank(headerKey) && StringUtils.isNotBlank(headerValue)) {
                post.setHeader(new BasicHeader(headerKey, headerValue));
            }

            //写死的 调用校验 的请求头
            post.setHeader(new BasicHeader("App-Device-Key", "0"));
            post.setHeader(new BasicHeader("App-Version", "2.9.0"));

            post.setHeader(new BasicHeader("Accept", "*/*"));

            /**
             * 执行post请求
             */
            response = client.execute(post);
            /**
             * 获取响应码
             */
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("响应码,statusCode={}", statusCode);
            if (SUCCESS_CODE == statusCode) {
                /**
                 * 通过EntityUitls获取返回内容
                 */
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                /**
                 * 转换成json,根据合法性返回json或者字符串
                 */
                try {
                    jsonObject = JSONObject.parseObject(result);
                    return jsonObject;
                } catch (Exception e) {
                    return result;
                }
            } else {
                log.info("当前请求响应码：" + statusCode);
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.error("HttpClientService-line: {}, errorMsg：{}", 146, "POST请求失败！");
                return result;
            }
        } catch (Exception e) {
            log.error("HttpClientService-line: {}, Exception：{}", 149, e);
            return response;
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
//        return null;
    }

    /**
     * 发送POST请求
     *
     * @param url
     * @param paramMap
     * @return JSON或者字符串
     * @throws Exception
     */
    public static Map sendLoginPost(String url, Map paramMap, String headerKey, String headerValue) throws Exception {

        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            /**
             *  创建一个httpclient对象
             */
            client = HttpClients.createDefault();
            /**
             * 创建一个post对象
             */
            HttpPost post = new HttpPost(url);
            /**
             * 包装成一个Entity对象
             */
            if (paramMap != null && paramMap.size() > 0) {
                List<NameValuePair> nameValuePairList = new ArrayList<>();
                for (Object key : paramMap.keySet()) {
                    nameValuePairList.add(new BasicNameValuePair(key + "", paramMap.get(key) + ""));
                }
                StringEntity entity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
                /**
                 * 设置请求的内容
                 */
                post.setEntity(entity);
            }

            /**
             * 设置请求的报文头部的编码
             */
            post.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));

            // 登录接口 加一个请求头
            if (StringUtils.isNotBlank(headerKey) && StringUtils.isNotBlank(headerValue)) {
                post.setHeader(new BasicHeader(headerKey, headerValue));
            }

            // 写死的 调用校验 的请求头
            post.setHeader(new BasicHeader("App-Device-Key", "0"));
            post.setHeader(new BasicHeader("App-Version", "2.9.0"));

            post.setHeader(new BasicHeader("Accept", "*/*"));

            /**
             * 执行post请求
             */
            response = client.execute(post);
            /**
             * 获取响应码
             */
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE == statusCode) {

                HttpEntity entity = response.getEntity();

                Header atime = response.getFirstHeader("App-Atime");

                Header nonce = response.getFirstHeader("App-Nonce");

                map.put("atime", atime.getValue());
                map.put("nonce", nonce.getValue());

                /**
                 * 通过EntityUitls获取返回内容
                 */
                String result = EntityUtils.toString(entity, "UTF-8");
                /**
                 * 转换成json,根据合法性返回json或者字符串
                 */
                map.put("res", result);
                return map;
            } else {
                log.info("当前请求响应码：" + statusCode);
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                map.put("res", result);
                log.error("HttpClientService-line: {}, errorMsg：{}", 146, "POST请求失败！");
                return map;
            }
        } catch (Exception e) {
            log.error("HttpClientService-line: {}, Exception：{}", 149, e);
            map.put("res", response);
            return map;
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
//        return null;
    }


    /**
     * 发送POST请求, body使用json
     *
     * @param url
     * @param paramMap
     * @return JSON或者字符串
     * @throws Exception
     */
    public static Object postJsonBody(String url, Map paramMap) throws Exception {
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {

            //    设置超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT) //设置连接超时时间，单位毫秒   和服务器建立连接的timeout
//                    .setConnectionRequestTimeout(1000) //设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。 从连接池获取连接的timeout
                .setSocketTimeout(SOCKET_TIMEOUT) //请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用  从服务器读取数据的timeout
                .build();

            /**
             *  创建一个httpclient对象
             */
            client = HttpClients.createDefault();
            /**
             * 创建一个post对象
             */
            HttpPost post = new HttpPost(url);

            post.setConfig(requestConfig);

            /**
             * 包装成一个Entity对象
             */
            if (paramMap != null && paramMap.size() > 0) {
                String body = JSONObject.toJSONString(paramMap);
                StringEntity entity = new StringEntity(body, "UTF-8");
                /**
                 * 设置请求的内容
                 */
                post.setEntity(entity);
            }

            /**
             * 设置请求的报文头部的编码
             */
            post.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
            /**
             * 设置请求的报文头部的编码
             */
            post.setHeader(new BasicHeader("Accept", "*/*"));

            /**
             * 执行post请求
             */
            response = client.execute(post);
            /**
             * 获取响应码
             */
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE == statusCode) {
                /**
                 * 通过EntityUitls获取返回内容
                 */
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                /**
                 * 转换成json,根据合法性返回json或者字符串
                 */
                try {
                    jsonObject = JSONObject.parseObject(result);
                    return jsonObject;
                } catch (Exception e) {
                    return result;
                }
            } else {
                log.info("当前请求响应码：" + statusCode);
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.error("HttpClientService-line: {}, errorMsg：{}", 146, "POST请求失败！");
                return result;
            }
        } catch (Exception e) {
            log.error("HttpClientService-line: {}, Exception：{}", 149, e);
            return response;
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }



    /**
     * 发送POST请求, body使用json数组
     *
     * @param url
     * @param list
     * @return JSON或者字符串
     * @throws Exception
     */
    public static Object postJsonArrayBody(String url, List list) throws Exception {
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {

            //    设置超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT) //设置连接超时时间，单位毫秒   和服务器建立连接的timeout
//                    .setConnectionRequestTimeout(1000) //设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的。 从连接池获取连接的timeout
                .setSocketTimeout(SOCKET_TIMEOUT) //请求获取数据的超时时间，单位毫秒。 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用  从服务器读取数据的timeout
                .build();

            /**
             *  创建一个httpclient对象
             */
            client = HttpClients.createDefault();
            /**
             * 创建一个post对象
             */
            HttpPost post = new HttpPost(url);

            post.setConfig(requestConfig);

            /**
             * 包装成一个Entity对象
             */
            if (list != null && list.size() > 0) {
                String body = JSONObject.toJSONString(list);
                StringEntity entity = new StringEntity(body, "UTF-8");
                /**
                 * 设置请求的内容
                 */
                post.setEntity(entity);
            }

            /**
             * 设置请求的报文头部的编码
             */
            post.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
            /**
             * 设置请求的报文头部的编码
             */
            post.setHeader(new BasicHeader("Accept", "*/*"));

            /**
             * 执行post请求
             */
            response = client.execute(post);
            /**
             * 获取响应码
             */
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE == statusCode) {
                /**
                 * 通过EntityUitls获取返回内容
                 */
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                /**
                 * 转换成json,根据合法性返回json或者字符串
                 */
                try {
                    jsonObject = JSONObject.parseObject(result);
                    return jsonObject;
                } catch (Exception e) {
                    return result;
                }
            } else {
                log.info("当前请求响应码：" + statusCode);
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.error("HttpClientService-line: {}, errorMsg：{}", 146, "POST请求失败！");
                return result;
            }
        } catch (Exception e) {
            log.error("HttpClientService-line: {}, Exception：{}", 149, e);
            return response;
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * 发送PUT请求, body使用json
     *
     * @param url
     * @param paramMap
     * @return JSON或者字符串
     * @throws Exception
     */
    public static Object putJsonBody(String url, Map paramMap) throws Exception {
        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            /**
             *  创建一个httpclient对象
             */
            client = HttpClients.createDefault();
            /**
             * 创建一个post对象
             */
            HttpPut post = new HttpPut(url);
            /**
             * 包装成一个Entity对象
             */
            if (paramMap != null && paramMap.size() > 0) {
                String body = JSONObject.toJSONString(paramMap);
                StringEntity entity = new StringEntity(body, "UTF-8");
                /**
                 * 设置请求的内容
                 */
                post.setEntity(entity);
            }

            /**
             * 设置请求的报文头部的编码
             */
            post.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
            /**
             * 设置请求的报文头部的编码
             */
            post.setHeader(new BasicHeader("Accept", "*/*"));

            /**
             * 执行post请求
             */
            response = client.execute(post);
            /**
             * 获取响应码
             */
            int statusCode = response.getStatusLine().getStatusCode();
            if (SUCCESS_CODE == statusCode) {
                /**
                 * 通过EntityUitls获取返回内容
                 */
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                /**
                 * 转换成json,根据合法性返回json或者字符串
                 */
                try {
                    jsonObject = JSONObject.parseObject(result);
                    return jsonObject;
                } catch (Exception e) {
                    return result;
                }
            } else {
                log.info("当前请求响应码：" + statusCode);
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                log.error("HttpClientService-line: {}, errorMsg：{}", 146, "POST请求失败！");
                return result;
            }
        } catch (Exception e) {
            log.error("HttpClientService-line: {}, Exception：{}", 149, e);
            return response;
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }

    /**
     * 发送文件对象到服务端
     *
     * @param file
     * @param url
     * @param params
     * @param charset
     * @param connectTimeout
     * @param socketTimeout
     * @return
     */
    public static String postFile(File file, String url, Map<String, String> params, String charset, int connectTimeout, int socketTimeout) {

        if (StringUtils.isBlank(url)) {
            return null;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).build();
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            //将java.io.File对象添加到HttpEntity（org.apache.http.HttpEntity）对象中
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //解决上传文件，文件名中文乱码问题
            builder.setCharset(Charset.forName("utf-8"));
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式
            builder.addBinaryBody("uploadFile", file, ContentType.create("multipart/form-data"), "file");
            builder.addPart("file", new FileBody(file));
            httpPost.setEntity(builder.build());
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                result = EntityUtils.toString(resEntity);
                // 消耗掉response
                EntityUtils.consume(resEntity);
            } else {
                //响应信息
                result = response.toString();
            }
        } catch (Exception e) {
            log.error("http file request fial", e);
        }

        return result;
    }


    /* *
     * @Author jinfengsheng
     * @Description // 整理检验验证码的 请求地址
     * @Date 7:09 下午 2019/12/3
     * @Param []
     * @return java.lang.String
     **/
    public static String pcgchekSmscodeUrl(String logincodeUrl, Map<String, Object> paramMap) {

        try {
            URIBuilder uriBuilder = new URIBuilder(logincodeUrl);

            if (paramMap != null && paramMap.size() > 0) {
                List<NameValuePair> nameValuePairList = new ArrayList<>();
                for (Object key : paramMap.keySet()) {
                    nameValuePairList.add(new BasicNameValuePair(key + "", paramMap.get(key) + ""));
                }
                uriBuilder.addParameters(nameValuePairList);
            }
            return uriBuilder.build().toString();

        } catch (Exception e) {

            log.error("整理检验验证码的请求地址失败,返回:{}", e);
        }

        return logincodeUrl;


    }

    /**
     * 发送GET请求
     *
     * @param url      请求url
     * @param paramMap 请求参数
     * @return JSON或者字符串
     * @throws Exception
     */
    public static String sendGet(String url, Map paramMap) throws Exception {

        JSONObject jsonObject = null;
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            /**
             * 创建HttpClient对象
             */
            client = HttpClients.createDefault();
            /**
             * 创建URIBuilder
             */
            URIBuilder uriBuilder = new URIBuilder(url);
            /**
             * 设置参数
             */
            if (paramMap != null && paramMap.size() > 0) {
                List<NameValuePair> nameValuePairList = new ArrayList<>();
                for (Object key : paramMap.keySet()) {
                    nameValuePairList.add(new BasicNameValuePair(key + "", paramMap.get(key) + ""));
                }
                uriBuilder.addParameters(nameValuePairList);
            }
            /**
             * 创建HttpGet
             */
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            /**
             * 设置请求头部编码
             */
            httpGet.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
            /**
             * 设置返回编码
             */
//            httpGet.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));
            httpGet.setHeader(new BasicHeader("Accept", "*/*"));
            /**
             * 请求服务
             */
            response = client.execute(httpGet);
            /**
             * 获取响应吗
             */
            int statusCode = response.getStatusLine().getStatusCode();

            if (SUCCESS_CODE == statusCode) {
                /**
                 * 获取返回对象
                 */
                Header[] responses = response.getHeaders("response");
                HttpEntity entity = response.getEntity();
                /**
                 * 通过EntityUitls获取返回内容
                 */
                return EntityUtils.toString(entity, "UTF-8");

            } else {
                log.error("HttpClientService-line: {}, errorMsg{}", 97, "GET请求失败！");
                HttpEntity entity = response.getEntity();
                /**
                 * 通过EntityUitls获取返回内容
                 */
                return EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            log.error("HttpClientService-line: {}, Exception: {}", 100, e);
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
        return null;
    }
}