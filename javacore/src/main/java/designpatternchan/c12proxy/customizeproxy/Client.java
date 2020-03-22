package designpatternchan.c12proxy.customizeproxy;

public class Client {

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
            升级总费用是：150元
        * */
    }

}
