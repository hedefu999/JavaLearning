package com.wechatx.server;


import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;

public class NameSpaceSocketServer {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(9094);
        config.getSocketConfig().setReuseAddress(true);
        SocketIOServer server = new SocketIOServer(config);

        /**
         * 一般地，自定义namespace不必在添加连接事件监听，因为某个namespace收到连接，默认namespace总是会收到事件，
         * 这样便会触发+1次
         */
        //server.addNamespace("/im").addConnectListener(new ConnectListener() {
        //    @Override
        //    public void onConnect(SocketIOClient client) {
        //        System.out.println("namespace为im的服务收到连接");
        //    }
        //});
        //server.addNamespace("/am").addConnectListener(new ConnectListener() {
        //    @Override
        //    public void onConnect(SocketIOClient client) {
        //        System.out.println("namespace为am的服务收到连接");
        //    }
        //});
        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                //NamespaceClient namespaceClient = new NamespaceClient();

            }
        });

        //SocketIONamespace imNamespace = server.getNamespace("/im");
        //SocketIONamespace amNamespace = server.getNamespace("/am");
        //下面演示namespace添加独立的监听事件
        //imNamespace.addEventListener("chat", String.class, new DataListener<String>() {
        //    @Override
        //    public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
        //        //这样子取client所属的namespace
        //        String namespace = client.getNamespace().getName();
        //        //client的握手数据里没有namespace信息,client里才有
        //        //{"address":{"address":"0:0:0:0:0:0:0:1","port":63760},"httpHeaders":{"empty":false},"local":{"address":"0:0:0:0:0:0:0:1","port":9093},"time":1562675851507,"url":"/socket.io/?EIO=3&transport=polling&t=MlMlJ3l","urlParams":{"EIO":["3"],"transport":["polling"],"t":["MlMlJ3l"]},"xdomain":false}
        //        System.out.println("im namespace收到数据："+data);
        //        client.sendEvent("");
        //    }
        //});




        //测试记录  byte[]  ByteBufInputStream  MsgObj  String  JSONArray   JSONObject
        server.addEventListener("wechat", MsgObj.class, new DataListener<MsgObj>() {
            @Override
            public void onData(SocketIOClient client, MsgObj data, AckRequest ackSender) throws Exception {
                System.out.println("am namespace收到数据："+ JSON.toJSONString(data));
                client.sendEvent("chat","我已经收到了");
            }
        });




        //TODO 如何向namespace注入事件
        server.start();
    }
}
