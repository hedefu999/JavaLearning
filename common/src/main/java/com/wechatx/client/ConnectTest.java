package com.wechatx.client;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.junit.Test;

import java.net.URISyntaxException;

public class ConnectTest {
    public static void main(String[] args) throws Exception {
        String scrmWechatServerURL1 = ClientUtils.getScrmWechatServerURL(ClientDict.mainUrl, ClientDict.deviceId,ClientDict.wechatId,ClientDict.spyVersion,ClientDict.transport);
        byte[] msgBytes1 = ClientUtils.getEncryptMsgData(ClientDict.wechatId,ClientDict.spyVersion,ClientUtils.readJSONFromFile(ClientDict.jaonpath1));
        IO.Options options = new IO.Options();
        Socket socket = IO.socket(scrmWechatServerURL1,options);
        socket.connect();
        socket.emit("/im/wechat",msgBytes1);
        //socket.disconnect();
        //if (socket.connected()){
        //    socket.disconnect();
        //    socket.close();
        //}
    }

    @Test
    public void test() throws Exception{
        //即使启用新线程也不可以
        new Runnable(){
            @Override
            public void run() {
                IO.Options options = new IO.Options();
                //options.
                Socket socket = null;
                try {
                    socket = IO.socket("http://10.255.15.110:9090",options);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                socket.connect();
                socket.emit("send-message","你好");
            }
        };

    }
}
