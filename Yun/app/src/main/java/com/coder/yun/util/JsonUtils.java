package com.coder.yun.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * fastjson解析工具类
 */

public class JsonUtils {

    public JsonUtils() {

    }

    /**
     * 对MapObject类型数据进行解析
     * @param json	要解析的json字符串
     * @return
     */
    public static Map<String, Object> getMapObj(String json)throws Exception {
        return JSON.parseObject(json, new TypeReference<Map<String, Object>>(){});
    }

    /**
     * 对listmap类型进行解析
     * @param json    要解析的json字符串
     * @return
     */
    public static List<Map<String, Object>> getListMap(String json)throws Exception {
        return JSON.parseObject(json,new TypeReference<List<Map<String, Object>>>(){});
    }

}
