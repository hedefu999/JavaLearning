package com.wechatx.client;

import io.socket.client.Manager;
import io.socket.client.Socket;
import org.junit.Test;

import java.net.URI;

public class SimpleClientTest {

    public static final String mainUrl = "http://127.0.0.1:9094";
    public static final String connUrl = ClientUtils.getScrmWechatServerURL(mainUrl,"设备1","wxid_34","1.3.2","websocket");
    public static final String nameSpace = "/im";

    public static void main(String[] args) throws Exception{
        //IO.Options options = new IO.Options();
        //Socket socket = IO.socket(":9090",options);
        Manager manager = new Manager(new URI(connUrl));
        Socket socket = manager.socket(nameSpace);
        socket.connect();
        byte[] encryptMsgData = ClientUtils.getEncryptMsgData(ClientDict.wechatId, ClientDict.spyVersion, "你好，发过来一条消息");
        //socket.emit("wechat", encryptMsgData, new Ack() {
        //    @Override
        //    public void call(Object... args) {
        //        System.out.println("hedefu call");
        //    }
        //});
        socket.emit("wechat", encryptMsgData);
        //socket.disconnect();
        //socket.close();
    }
    @Test
    public void test(){
        System.out.println(connUrl);
    }
}
