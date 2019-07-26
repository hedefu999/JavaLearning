package com.dpchan.c12proxy.forceproxy;

public class Client {
    public static void main2(String[] args) {
        IGamePlayer player = new GamePlayer("张三");
        player.login("zhangSan", "password");
        player.killBoss();
        player.upgrade();

        IGamePlayer proxy = new GamePlayerProxy(player);
        proxy.login("zhangSan", "password");
        proxy.killBoss();
        proxy.upgrade();
        /*
        *   请使用代理访问
            请使用代理访问
            请使用代理访问
            请使用代理访问
            请使用代理访问
            请使用代理访问
        * */
    }

    public static void main(String[] args) {
        //GamePalyerProxy必须由GamePlayer对象自己new才被承认
        IGamePlayer player = new GamePlayer("张三");
        IGamePlayer proxy = player.getProxy();
        proxy.login("zhangsan","password");
        proxy.killBoss();
        proxy.upgrade();
        /*
        *   登录名为zhangsan 的用户 张三登录成功！
            张三在打怪！
            张三 又升了一级！
        * */
    }

}
