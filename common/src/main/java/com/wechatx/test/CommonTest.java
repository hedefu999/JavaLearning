package com.wechatx.test;

import com.alibaba.fastjson.JSON;
import com.wechatx.nessaries.MsgTypeEnum;
import com.wechatx.nessaries.SocketGeneralMsg;
import com.wechatx.nessaries.SocketGeneralMsgFactory;
import org.junit.Test;

import java.util.Date;

public class CommonTest {
    public static final String deviceId = "866265038357770";
    public static final String wechatId = "wxid_rf9ru7mnsbv922";
    @Test
    public void test(){
        SocketGeneralMsg msg = new SocketGeneralMsg();
        msg.setMsgType("msgType");
        msg.setTraceId("hedefu-test");
        msg.setCreateTime(new Date().getTime());
        msg.setDeviceId("866265038357770");
        msg.setWechatId("wxid_rf9ru7mnsbv922");
        msg.setData(null);
        msg.setExtra(null);

        System.out.println(JSON.toJSONString(msg));
    }
    @Test
    public void getCommandJSON(){
        SocketGeneralMsg command = SocketGeneralMsgFactory.buildCommand(
                MsgTypeEnum.SYNC_CHATROOM_INFO,deviceId,wechatId,"",null);
        SocketGeneralMsg command2 = SocketGeneralMsgFactory.buildCommand(
                MsgTypeEnum.SYNC_CHATROOM_LIST,deviceId,wechatId,"",null);
        System.out.println(JSON.toJSONString(command2));
    }
}
