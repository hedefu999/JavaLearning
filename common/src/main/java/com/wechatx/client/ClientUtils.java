package com.wechatx.client;

import com.wechatx.nessaries.WechatSpyMsgUtils;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class ClientUtils {
    private Logger log = LoggerFactory.getLogger(getClass());




    public static Socket createOneClient(String scrmWechatServerURL){
        IO.Options options = new IO.Options();
        Socket socket = null;
        try {
            socket = IO.socket(scrmWechatServerURL, options);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return socket;
    }


    //创建一个每隔200毫秒发一次消息，发送times次的客户端
    public static void createOneAutoSendSocket(String scrmWechatServerURL, byte[] msgBytes, long times)throws Exception{
        Socket socket = createOneClient(scrmWechatServerURL);
        //socket.connect();
        socket.emit("wechat",msgBytes);
        //循环发送数据
        //long start = System.currentTimeMillis();
        //while (System.currentTimeMillis() - start < times*200){
        //    socket.emit("wechat",msgBytes);
        //    Thread.sleep(200);
        //}
    }


    public static String getScrmWechatServerURL(String mainUrl, String deviceId,String wechatId, String spyVersion, String transport){
        String sign = "deviceId="+deviceId;
        String encryptSign = WechatSpyMsgUtils.getEncryptSign(wechatId,spyVersion,sign);
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(mainUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        uriBuilder.addParameter("wechatId",wechatId)
                .addParameter("EIO","3")
                .addParameter("sign",encryptSign)
                .addParameter("spyVersion",spyVersion)
                .addParameter("transport",transport);
        return uriBuilder.toString();
    }

    public static byte[] getEncryptMsgData(String wechatId,String spyVersion,String msg) throws IOException {
        return WechatSpyMsgUtils.getEncryptData(wechatId,spyVersion,msg);
    }

    public static String readJSONFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        String json = FileUtils.readFileToString(file);
        return WechatSpyMsgUtils.compressJSON(json);
    }


}
