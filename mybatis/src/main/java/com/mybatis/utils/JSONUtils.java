package com.mybatis.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONUtils {
    public static String toString(Object data){
        return JSON.toJSONString(data, SerializerFeature.WriteNullStringAsEmpty);
    }
}
