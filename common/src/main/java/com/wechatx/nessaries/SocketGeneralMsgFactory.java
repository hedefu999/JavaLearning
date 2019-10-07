package com.wechatx.nessaries;

import java.util.UUID;

public class SocketGeneralMsgFactory {

    private SocketGeneralMsgFactory() {}

    /**
     * 生成将下发给客户端的命令，使用SocketGeneralMsg包装
     * @param msgTypeEnum
     * @param deviceId
     * @param wechatId
     * @param data
     * @param extra
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T,E> SocketGeneralMsg<T,E> buildCommand(MsgTypeEnum msgTypeEnum, String deviceId, String wechatId, T data, E extra){
        SocketGeneralMsg<T,E> generalMsg = new SocketGeneralMsg<>();
        generalMsg.setMsgType(msgTypeEnum.getCode());
        generalMsg.setDeviceId(deviceId);
        generalMsg.setWechatId(wechatId);
        generalMsg.setCreateTime(System.currentTimeMillis());
        generalMsg.setData(data);
        generalMsg.setExtra(extra);
        generalMsg.setTraceId(UUID.randomUUID().toString());
        return generalMsg;
    }
}
