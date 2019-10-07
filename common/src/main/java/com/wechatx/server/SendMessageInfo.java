package com.wechatx.server;

import java.io.Serializable;

/**
 * @Description 文本消息映射实体
 * @Author liaoze
 * @Date 2019/7/9 下午9:59
 **/
public class SendMessageInfo implements Serializable {

    private String msgType;

    private long createTime;

    private String traceId;

    private String wechatId;

    private Object data;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
