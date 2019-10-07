package com.wechatx.nessaries;


/**
 * @program: scrmwechat
 * @author: Mr.Guo
 * @description: 下发指令消息类型
 * @create: 2019-06-24 10:25
 */
public enum MsgTypeEnum {


    /**
     * 微信群组模块使用的消息类型
     */
    /*-=-=-=-=-同步指令=-=-=-=-=*/
    SYNC_CHATROOM_LIST("sync_chatroom_list","同步群组列表"),
    SYNC_CHATROOM_INFO("sync_chatroom_info","同步群组信息"),
    SYNC_CHATROOM_MEMBER("sync_chatroom_member","同步指定群组的成员信息"),
    SYNC_STRANGER("sync_stranger","同步指定陌生人(群成员)信息"),
    /*-=-=-=-=-管理指令=-=-=-=-=*/
    CALL_CREATE_CHATROOM("call_create_chatroom", "创建群组"),
    CALL_ADD_CHATROOM_MEMBER("call_add_chatroom_member","添加成员进入群组"),
    CALL_INVITE_CHATROOM_MEMBER("call_invite_chatroom_member","邀请成员加入群组"),
    CALL_DELETE_CHATROOM_MEMBER("call_delete_chatroom_member","从群组移除成员"),
    CALL_SET_CHATROOM_ANNOUNCEMENT("call_set_chatroom_announcement","设置群公告"),
    CALL_TRANSFER_CHATROOM_OWNER("call_transfer_chatroom_owner","群主转让"),
    CALL_ENABLE_ALLOW_BY_IDENTITY("call_enable_allow_by_identity","设置是否允许邀请进群"),
    /*-=-=-=-=-使用指令=-=-=-=-=*/
    CALL_GET_CHATROOM_QRCODE("call_get_chatroom_qrcode","获取群组二维码"),
    CALL_UPDATE_CHATROOM_NAME("call_update_chatroom_name","更新群组名称"),
    CALL_UPDATE_CHATROOM_NICKNAME("call_update_chatroom_nickname","更新群昵称,暂不支持"),
    CALL_SAVE_TO_GROUPCARD("call_save_to_groupcard","保存群组到通讯录"),
    CALL_REMOVE_FROM_GROUPCARD("call_remove_from_groupcard","从通讯录移除群组"),
    CALL_QUIT_CHATROOM("call_quit_chatroom","退出群组"),

    /**
     * 同步指定联系人
     */
    SYNC_CONTACT("sync_contact", "同步指定联系人"),

    /**
     * 全量同步联系人
     */
    SYNC_ALL_CONTACT("sync_all_contact", "全量同步联系人"),

    /**
     * sync_contact_batch
     */
    SYNC_CONTACT_BATCH("sync_contact_batch","批量同步联系人"),



    /**
     * 心跳检测
     */
    DEVICE_CHECK("device_check", "心跳检测"),

    /**
     * 同步朋友圈
     */
    SYNC_TIMELINE("sync_timeline","同步朋友圈"),

    /**
     * 同步好友列表
     */
    SYN_FRIEND_LIST("sync_contact_list","同步好友列表"),

    /**
     * 指定好友添加指定标签
     */
    ADD_TAGS("add_tag","指定好友添加指定标签"),

    /**
     * 指定好友删除指定标签
     */
    DELETE_TAGS("delete_tag","指定好友删除指定标签"),

    /**
     * 覆盖更新指定好友指定标签
     */
    UPDATE_TAGS ("update_tag","覆盖更新指定好友指定标签"),

    /**
     * 更新联系人(修改好友备注)
     */
    CALL_UPDATE_CONTACT("call_update_contact","更新联系人"),

    /**
     *  发送消息
     */
    SEND_MESSAGE("send_message","发送微信消息"),


    /**
     *  微信状态检测
     */
    WECHAT_CHECK("wechat_check","微信状态检测");


    private String code;

    private String desc;

    MsgTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


}
