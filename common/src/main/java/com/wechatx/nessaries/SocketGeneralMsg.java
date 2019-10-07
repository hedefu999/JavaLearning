package com.wechatx.nessaries;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @program: scrmwechat
 * @author: Mr.Guo
 * @description: Socket通用消息体
 * @create: 2019-06-22 11:14
 */
public class SocketGeneralMsg<T, E> implements Serializable {

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * traceId
     */
    private String traceId;

    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 设备Id
     */
    private String deviceId;

    /**
     * 操作的微信Id
     */
    private String wechatId;

    /**
     * 数据
     */
    private T data;

    /**
     * 预留额外数据字段
     */
    private E extra;

    /**
     * 组装数据
     * @param msgType 消息类型
     * @param traceId UUID
     * @param createTime 创建时间
     * @param deviceId 设备ID
     * @param wechatId 微信ID
     * @param data 数据
     * @param extra 额外数据
     */
    public void init(String msgType, String traceId, long createTime, String deviceId, String wechatId, T data, E extra) {
        this.msgType = msgType;
        this.traceId = traceId;
        this.createTime = createTime;
        this.deviceId = deviceId;
        this.wechatId = wechatId;
        this.data = data;
        this.extra = extra;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public E getExtra() {
        return extra;
    }

    public void setExtra(E extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
