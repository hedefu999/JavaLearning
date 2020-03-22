package designpatternchan.c12proxy.customizeproxy;

public interface IGamePlayer {
    //登录游戏
    void login(String user, String password);

    //杀怪，这是网络游戏的主要特色
    void killBoss();

    //升级
    void upgrade();

    //每个人都可以找一下自己的代理
    public IGamePlayer getProxy();
}
