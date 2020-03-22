package designpatternchan.c12proxy.customizeproxy;

public class GamePlayerProxy implements IGamePlayer,IProxy {
    private IGamePlayer gamePlayer = null;

    public GamePlayerProxy(IGamePlayer iGamePlayer){
        this.gamePlayer = iGamePlayer;
    }
    @Override
    public void login(String user, String password) {
        this.gamePlayer.login(user,password);
    }

    @Override
    public void killBoss() {
        this.gamePlayer.killBoss();
    }

    @Override
    public void upgrade() {
        this.gamePlayer.upgrade();
        this.count();
    }

    @Override
    public IGamePlayer getProxy() {
        return this;
    }

    @Override
    public void count() {
        System.out.println("升级总费用是：150元");
    }
}
