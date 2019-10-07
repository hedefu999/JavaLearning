package com.wechatx.client;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * 中台项目上报消息测试案例
 */
public class MiddleTestClient {

    public static final String mainUrl = "http://172.17.40.108:9090";
    public static final String mainUrl2 = "http://127.0.0.1:9090";
    public static final String connUrl = ClientUtils.getScrmWechatServerURL(mainUrl2,ClientDict.deviceId,ClientDict.wechatId,ClientDict.spyVersion,ClientDict.transport);

    public static void main(String[] args) throws Exception{
        String chatRoomAvatar = "ChatRoomAvatar.json";
        String chatRoomAddMemer = "ChatRoomADD.json";
        String chatRoomSyncInfo = "ChatRoomSync.json";
        String chatRoomRemove = "ChatRoomRemove.json";
        String chatRoomAddOrRemove = "MemberAddOrRemove.json";
        String member_record = "ChatRoomMemberRecord.json";
        String chatRoomBasicInfo = "ChatRoomBasicInfo.json";
        String memberAddOrRemove = "MemberAddOrRemove.json";
        String chat_room_list = "ChatRoomListHash.json";
        emitMessage(chat_room_list);
    }

    public static void emitMessage(String msgFile){
        Socket socket = null;
        byte[] encryptMsgData = null;
        try {
            socket = IO.socket(connUrl);
            socket.connect();
            String filePath = "/Users/hedefu/WorkSpace/IDEA/DASOUCHE/plainproject/web/src/main/java/com/souche/dubbotest/scrmwechat/message/";
            filePath = filePath + msgFile;
            String message = ClientUtils.readJSONFromFile( filePath);
            encryptMsgData = ClientUtils.getEncryptMsgData(ClientDict.wechatId, ClientDict.spyVersion, message);
        }catch (Exception e){
            e.printStackTrace();
        }
        socket.emit("wechat",encryptMsgData);
    }

}
