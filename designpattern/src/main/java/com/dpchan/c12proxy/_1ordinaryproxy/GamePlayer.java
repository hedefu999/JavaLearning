package com.dpchan.c12proxy._1ordinaryproxy;

public class GamePlayer implements IGamePlayer {
    private String name = "";
    public GamePlayer(IGamePlayer _gamePlayer, String _name){
        if (_gamePlayer == null){
            throw new RuntimeException("不能创建游戏玩家");
        }else {
            this.name = _name;
        }
    }
    @Override
    public void login(String user, String password) {
        System.out.println("登录名为"+user + " 的用户 " + this.name + "登录成功！");
    }

    @Override
    public void killBoss() {
        System.out.println(this.name + "在打怪！");
    }

    @Override
    public void upgrade() {
        System.out.println(this.name + " 又升了一级！");
    }
}
