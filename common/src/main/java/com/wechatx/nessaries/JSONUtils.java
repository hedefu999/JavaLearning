package com.wechatx.nessaries;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONUtils {
    /**
     * 仅用于打印日志
     * @param obj
     * @return
     */
	public static String toString(Object obj) {
		return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullStringAsEmpty);
	}

    /**
     * 向客户端发送指令时使用此方法
     * @param obj
     * @return
     */
	public static String getCommandJSON(Object obj){
	    return JSON.toJSONString(obj);
    }
}
