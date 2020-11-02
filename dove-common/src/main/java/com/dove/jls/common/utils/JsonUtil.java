package com.dove.jls.common.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * JsonUtils
 *
 * @author PANG.JIAN
 * @date 2018/4/10
 */
@Slf4j
public class JsonUtil {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final JavaType StringObjectMap = OBJECT_MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class);
    public static final JavaType StringStringMap = OBJECT_MAPPER.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
    public static final JavaType ObjectList = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, Object.class);
    public static final JavaType StringList = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, String.class);

    static {
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }

    private JsonUtil() {
    }

    public static String encode(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            log.error("转换异常：{}", obj, e);
            return "";
        }
    }

    /**
     * 获取Json实例
     *
     * @return Json实例
     */
    public static <K, V> Map<K, V> toMap(String json, JavaType javaType) {

        return fromJson(json, javaType);
    }

    /**
     * 获取Json实例
     *
     * @return Json实例
     */
    public static Map<String, String> toMap(String json) {
        return fromJson(json, StringStringMap);
    }


    /**
     * 将json string反序列化成对象
     *
     * @param json
     * @param valueType
     * @return
     */
    public static <T> T decode(String json, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(json, valueType);
        } catch (IOException e) {
            log.error("转换异常：{}", json, e);
            return null;
        }
    }

    public static <T> T decode(InputStream ins, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(ins, valueType);
        } catch (IOException e) {
            log.error("转换异常：{}", ins, e);
            return null;
        }
    }

    /**
     * 将json array反序列化为对象
     *
     * @param json
     * @param typeReference
     * @return
     */
    public static <T> T decode(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            log.error("转换异常：{}", json, e);
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, contructCollectionType()或contructMapType()构造类型, 然后调用本函数.
     */
    public static <T> T fromJson(String json, JavaType javaType) {
        try {
            return (T) OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            log.error("转换异常：{}", json, e);
            return null;
        }
    }

    /**
     * 构造Collection类型.
     */
    public static JavaType buildCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return OBJECT_MAPPER.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    /**
     * 构造Map类型.
     */
    public static JavaType buildMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return OBJECT_MAPPER.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }
}
