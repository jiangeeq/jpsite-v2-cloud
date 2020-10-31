package com.mty.jls.dovecommon.utils;

import com.mty.jls.dovecommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author jiangpeng
 * @date 2019/12/1819:09
 */
@Component
@Slf4j
public class RestTemplateUtils {
    private static RestTemplate restTemplate;
    private static HttpServletRequest request;

    @Bean
    public RestTemplate restTemplate() {
        val httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(5000);
        httpRequestFactory.setReadTimeout(5000);
        return new RestTemplate(httpRequestFactory);
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate, HttpServletRequest httpServletRequest) {
        RestTemplateUtils.restTemplate = restTemplate;
        RestTemplateUtils.request = httpServletRequest;
    }

    public static <T> T executeHttpPost(String url, Object req, Class<T> clazz) {
        return executeHttpPost(url, null, req, clazz);
    }

    public static String executeHttpPost(String url, Object req) {
        return executeHttpPost(url, null, req, String.class);
    }

    public static <T> T executeHttpPost(String url, Map<String, String> headerMap, Object req, Class<T> clazz) {
        val headers = new HttpHeaders();
        if (Objects.nonNull(headerMap)) {
            for (String key : headerMap.keySet()) {
                headers.add(key, headerMap.get(key));
            }
        } else {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        val formEntity = new HttpEntity<>(JsonUtil.encode(req), headers);

        val result = execute("executeHttpPost", url, formEntity, () -> restTemplate.postForObject(url, formEntity, String.class));
        if ("String".equals(clazz.getSimpleName())) {
            return (T) result;
        } else {
            return JsonUtil.decode(result, clazz);
        }
    }


    public static <T> T executeHttpPostFormData(String url, Map<String, String> header, Object req, Class<T> clazz) {
        val targetParam = req instanceof Map ? (Map<String, Object>) req : objectToMap(req);
        val map = new LinkedMultiValueMap<String, String>();

        targetParam.forEach((k, v) -> map.add(k, String.valueOf(v)));

        val headers = new HttpHeaders();
        for (String key : header.keySet()) {
            headers.add(key, header.get(key));
        }

        val formEntity = new HttpEntity<>(map, headers);
        val result = execute("executeHttpPostFormData", url, formEntity, () -> restTemplate.postForObject(url, formEntity, String.class));
        if ("String".equals(clazz.getSimpleName())) {
            return (T) result;
        } else {
            return JsonUtil.decode(result, clazz);
        }
    }

    public static <T> T executeHttpGitLabHeaderGet(String url, Class<T> clazz) {
        return executeHttpGitLabHeader(url, clazz, HttpMethod.GET);
    }

    public static <T> T executeHttpGitLabHeader(String url, Class<T> clazz, HttpMethod method) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        val result = execute("executeHttpGitLabHeaderGet", url, requestEntity,
                () -> restTemplate.exchange(url, method, requestEntity, String.class).getBody());
        if ("String".equals(clazz.getSimpleName())) {
            return (T) result;
        } else {
            return JsonUtil.decode(result, clazz);
        }
    }

    /**
     * multipart/form-data post 请求
     *
     * @param url 地址
     * @param req 请求
     * @return
     */
    public static String executeMultipartFormData(String url, Object req) {
        try {
            val headers = new HttpHeaders();
            val targetParam = req instanceof Map ? (Map<String, Object>) req : objectToMap(req);
            val bodyMap = new LinkedMultiValueMap<String, Object>();

            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            targetParam.forEach((k, v) -> {
                bodyMap.add(k, v);
            });

            val formEntity = new HttpEntity<>(bodyMap, headers);

            final ResponseEntity<String> responseEntity = execute("executeMultipartFormData", url, formEntity,
                    () -> restTemplate.postForEntity(url, formEntity, String.class));
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException("请求失败", e);
        }
    }

    /**
     * 一般用于文件上传,以及一次性提交多种不同数据格式的请求。
     * multipart/form-data 请求体本质上就是。一个请求体包含多个Part，每个Part有自己独立的header和body。
     *
     * @param url
     * @param req
     * @param mediaType
     * @return
     */
    public static String executeMoreMultipartFormData(String url, Object req, MediaType mediaType) {
        try {
            val targetParam = req instanceof Map ? (Map<String, Object>) req : objectToMap(req);

            val headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();

            // ----------------- 表单 part
            multipartBodyBuilder.part("name", "KevinBlandy");
            targetParam.forEach((k, v) -> {
                if (!(v instanceof File)) {
                    multipartBodyBuilder.part(k, v);
                }
            });

            /**
             * 每个表单项都有独立的header
             * 在这个表单项后额外添加了一个header
             */
//            multipartBodyBuilder.part("skill", Arrays.asList("Java", "Python", "Javascript")).header("myHeader", "myHeaderVal");

            // ----------------- 文件 part
            // 从磁盘读取文件
            multipartBodyBuilder.part("file", new FileSystemResource((File) targetParam.get("file")), mediaType);


            // build完整的消息体
            MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();

            final ResponseEntity<String> responseEntity = execute("executeMultipartFormData", url, multipartBody,
                    () -> restTemplate.postForEntity(url, multipartBody, String.class));
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException("请求失败", e);
        }
    }

    public static <T> T executeHttpGet(String url, Class<T> clazz) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        val result = execute("executeHttpGet", url, requestEntity,
                () -> restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
        if ("String".equals(clazz.getSimpleName())) {
            return (T) result;
        } else {
            return JsonUtil.decode(result, clazz);
        }
    }

    public static String executeHttpGet(String url, Object req) {
        val targetParam = req instanceof Map ? (Map<String, Object>) req : objectToMap(req);
        val targetUrl = buildGetUrlByMap(url, targetParam);

        return execute("executeHttpGet", targetUrl, targetParam, () -> restTemplate.getForObject(targetUrl, String.class, targetParam));
    }

    private static <T> T execute(String methodName, String url, Object requestBody, Supplier<T> supplier) {
        log.debug("执行http [{}] 请求url: [{}] 请求参数：[{}]", methodName, url, requestBody);
        final T t = supplier.get();
        log.debug("接收http [{}] 请求url: [{}] 响应结果: [{}]", methodName, url, t);
        return t;
    }

    /**
     * 将Object对象里面的属性和值转化成Map对象
     *
     * @param obj
     * @return
     */
    private static Map<String, Object> objectToMap(Object obj) {
        return Arrays.stream(obj.getClass().getDeclaredFields()).peek(field -> field.setAccessible(true)).collect(
                Collectors.toMap(Field::getName, field -> {
                    try {
                        return Optional.ofNullable(field.get(obj)).orElse("");
                    } catch (IllegalAccessException e) {
                        return "";
                    }
                })
        );
    }

    public static String buildGetUrlByMap(final String baseUrl, final Map<?, ?> requestParams) {
        if (requestParams == null || requestParams.isEmpty()) {
            return baseUrl;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(baseUrl);
        if (baseUrl.contains("?")) {
            builder.append("&");
        }else{
            builder.append("?");
        }
        requestParams.forEach((k,v)->{
            builder.append(k).append("=").append(v).append("&");
        });

        return builder.substring(0, builder.length() - 1);
    }
}
