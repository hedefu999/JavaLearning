package com.wechatx.nessaries;

import com.alibaba.fastjson.JSONArray;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * 与微信间谍通信设计的加解密及数据获取
 */
public class WechatSpyMsgUtils {
    /**
     * 获取客户端的微信ID
     * @param client
     * @return
     */
    public static String getWechatId(SocketIOClient client){
        Map<String, List<String>> handShakeDataMap = client.getHandshakeData().getUrlParams();
        List<String> list = handShakeDataMap.get(CommunicationConstants.HANDSHAKE_WECHATID);
        String wechatId = "";
        if (!CollectionUtils.isEmpty(list)) {
            wechatId = list.get(0);
        }
        return wechatId;
    }

    /**
     * 获取客户端使用的微信间谍版本号
     * @param client
     * @return
     */
    public static String getSpyVersion(SocketIOClient client){
        Map<String, List<String>> handShakeDataMap = client.getHandshakeData().getUrlParams();
        List<String> list = handShakeDataMap.get(CommunicationConstants.HANDSHAKE_SPYVESION);
        String deviceId = "";
        if (!CollectionUtils.isEmpty(list)){
            deviceId = list.get(0);
        }
        return deviceId;
    }

    /**
     * 获取客户端的IP地址
     * @param client
     * @return
     */
    public static String getClientAddress(SocketIOClient client){
        return client.getHandshakeData().getAddress().getHostString();
    }

    /**
     * 从握手数据中获取deviceId
     * @param client
     * @return
     */
    public static String getDeviceIdFromHandShake(SocketIOClient client){
        String sign = decryptHandShakeSign(client);
        return StringUtils.isEmpty(sign) ? "" : sign.split("=")[1];
    }

    /**
     * 检查deviceClientMap中是否存在此设备，如果没有就加入此Map
     * @param client
     * @param deviceClientMap
     */
    public static void checkDeviceClientMap(SocketIOClient client, Map<String,SocketIOClient> deviceClientMap){
        String deviceId = getDeviceIdFromHandShake(client);
        if (!StringUtils.isEmpty(deviceId) && !deviceClientMap.containsKey(deviceId)){
            deviceClientMap.put(deviceId,client);
        }
    }

    /**
     * 从握手数据中解密签名sign字段
     * @param client
     * @return
     */
    public static String decryptHandShakeSign(SocketIOClient client){
        String result = "";
        HandshakeData handshakeData = client.getHandshakeData();
        List<String> signList = handshakeData.getUrlParams().get(CommunicationConstants.HANDSHAKE_SIGN);
        if (!CollectionUtils.isEmpty(signList)){
            String encryptData = signList.get(0);
            byte[] asBytes = Base64.getDecoder().decode(encryptData);
            result = decryptData(client,asBytes);
        }
        return result;
    }

    /**
     * 解密客户端发来的json消息
     * @param client
     * @param encryptData
     * @return
     */
    public static String decryptData(SocketIOClient client, byte[] encryptData){
        //1. 从handShakeData中获取微信ID、微信间谍版本号，用于数据解密
        //HandShakeDataMap中提供的数据包括：wechatId EIO nsp sign spyVersion transport
        String wechatId = getWechatId(client);
        String spyVersion = getSpyVersion(client);
        //2. 开始解密数据
        return CodecUtils.javaDecode(wechatId,spyVersion,encryptData);
    }

    /**
     * 加密发往客户端的jsonArray数据
     * 2019-06-21 此处与客户端联调发现，虽然scrmwechat接受的是byte[]密文，但加密发过去的却应当是jsonArray,应进行转化
     * @param client
     * @param json
     * @return
     */
    public static JSONArray encryptData(SocketIOClient client, String json){
        //去除空格换行符
        json = json.replaceAll(" ","").replaceAll("\n","").replaceAll("\\n","");
        byte[] data = json.getBytes();
        String wechatId = getWechatId(client);
        String spyVersion = getSpyVersion(client);
        long timeStamp = System.currentTimeMillis();
        byte[] bytes = CodecUtils.javaEncode(wechatId,spyVersion,timeStamp,data);
        JSONArray jsonArray = new JSONArray();
        for (byte b : bytes){
            jsonArray.add(b);
        }
        return jsonArray;
    }

/************* 为客户端添加的方法 *************/
    public static String compressJSON(String json){
        return json.replaceAll(" ","").replaceAll("\n","").replaceAll("\\n","");
    }

    public static String getEncryptSign(String wechatId, String spyVersion, String sign){
        byte[] priEncrypt = getEncryptData(wechatId, spyVersion, sign);
        return Base64.getEncoder().encodeToString(priEncrypt);
    }

    public static byte[] getEncryptData(String wechatId, String spyVersion, String jsonMsg){
        byte[] signBytes = jsonMsg.getBytes();
        return CodecUtils.javaEncode(wechatId,spyVersion,System.currentTimeMillis(),signBytes);
    }


}
