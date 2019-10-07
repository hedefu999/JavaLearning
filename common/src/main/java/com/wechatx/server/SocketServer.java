package com.wechatx.server;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.listener.PingListener;
import com.wechatx.nessaries.SocketGeneralAck;
import com.wechatx.nessaries.WechatSpyMsgUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class SocketServer {
    private final Logger log = LoggerFactory.getLogger(SocketServer.class);

    private static Map<String,SocketIOClient> clientMap = new HashMap<>();
    public static final String wechatId = "wxid_rf9ru7mnsbv922";
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger("hedefu");
        Configuration config = new Configuration();
        config.setPingInterval(2000);
        config.setPingTimeout(3000);
        config.setHostname("0.0.0.0");
        config.setPort(9094);
        config.getSocketConfig().setReuseAddress(true);
        //在这里设置需要使用的序列化框架
        //config.setJsonSupport(new );
        SocketIOServer server = new SocketIOServer(config);

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                String deviceId = WechatSpyMsgUtils.getDeviceIdFromHandShake(client);
                String wechatId = WechatSpyMsgUtils.getWechatId(client);
                log.info("设备上线：nameSpace = {}, deviceID = {}, wechatID = {}",client.getNamespace().getName(), deviceId, wechatId);
                if (!StringUtils.isEmpty(deviceId)){
                    clientMap.put(deviceId,client);
                }
            }
        });

        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                System.out.println("断连");
            }
        });

        server.addEventListener("trigger", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String command, AckRequest ackRequest) throws Exception {
                SocketIOClient client = clientMap.get("866265038357770");
                log.info("向设备发送指令 {} - {}",command,client.getRemoteAddress());
                JSONArray jsonArray = WechatSpyMsgUtils.encryptData(client, command);
                client.sendEvent("wechat", new AckCallback<SocketGeneralAck>(SocketGeneralAck.class) {
                    @Override
                    public void onSuccess(SocketGeneralAck ack) {
                        System.out.println(ack.isSuccess()?command+"成功":"失败"+ack.getMessage());
                    }
                },jsonArray);
            }
        });

        server.addEventListener("wechat", byte[].class, new DataListener<byte[]>() {
            @Override
            public void onData(SocketIOClient client, byte[] encryptData, AckRequest ackSender) throws Exception {
                String message = WechatSpyMsgUtils.decryptData(client,encryptData);
                System.out.println(message);
            }
        });

        server.addPingListener(new PingListener() {
            @Override
            public void onPing(SocketIOClient client) {
                System.out.println("ping enter");
            }
        });

        server.start();
    }
}
