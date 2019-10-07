package com.wechatx.client;

import io.socket.client.Socket;

public class SocketIOClientTest {


    public static void main(String[] args) throws Exception{
        String scrmWechatServerURL1 = ClientUtils.getScrmWechatServerURL(ClientDict.mainUrl, ClientDict.deviceId,ClientDict.wechatId,ClientDict.spyVersion,ClientDict.transport);
        String scrmWechatServerURL2 = ClientUtils.getScrmWechatServerURL(ClientDict.mainUrl, ClientDict.deviceId2, ClientDict.wechatId2, ClientDict.spyVersion2, ClientDict.transport2);
        byte[] msgBytes1 = ClientUtils.getEncryptMsgData(ClientDict.wechatId,ClientDict.spyVersion,ClientUtils.readJSONFromFile(ClientDict.jaonpath1));
        byte[] msgBytes2 = ClientUtils.getEncryptMsgData(ClientDict.wechatId,ClientDict.spyVersion,ClientUtils.readJSONFromFile(ClientDict.jsonpath2));

        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                Socket client1 = ClientUtils.createOneClient(scrmWechatServerURL1);
                client1.connect();
                //循环发送数据
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < 100 * 200) {
                    client1.emit("wechat", msgBytes1);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                Socket client2 = ClientUtils.createOneClient(scrmWechatServerURL2);
                client2.connect();
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < 100 * 200) {
                    client2.emit("wechat", msgBytes1);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        runnable1.run();
        runnable2.run();
        //createOneAutoSendSocket(scrmWechatServerURL1,msgBytes1,100);


    }

    public static void test1(){
        //List<Runnable> clientThreads = new ArrayList<>();
        ////创建500个客户端
        //for (int i = 0; i < 5; i++) {
        //    clientThreads.add(new Runnable() {
        //        @Override
        //        public void run() {
        //            try {
        //                createOneAutoSendSocket(scrmWechatServerURL,msgBytes,100);
        //            } catch (Exception e) {
        //                e.printStackTrace();
        //            }
        //        }
        //    });
        //}


        //
        //Thread.sleep(900);
        //
        //Runnable runnable2 = new Runnable() {
        //    @Override
        //    public void run() {
        //        Socket client2 = createOneClient(scrmWechatServerURL2);
        //        //循环发送数据
        //        long start = System.currentTimeMillis();
        //        while (System.currentTimeMillis() - start < 100*200){
        //            client2.emit("wechat",msgBytes2);
        //            try {
        //                Thread.sleep(200);
        //            } catch (InterruptedException e) {
        //                e.printStackTrace();
        //            }
        //        }
        //        client2.close();
        //    }
        //};
        //
        //runnable1.run();
        //runnable2.run();
        ////启动这些傀儡客户端
        //clientThreads.forEach(Runnable::run);
    }
}
