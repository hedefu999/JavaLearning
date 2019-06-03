package com.effectivejava.c11.readresolve;

import java.io.Serializable;
import java.util.Arrays;

public class Elvis implements Serializable {
  public static final Elvis INSTANCE = new Elvis();
  private Elvis(){}
  private String[] favoriteSongs = {"物凄","远野幻想物语"};
  public void printFavorites(){
    System.out.println(Arrays.toString(favoriteSongs));
  }
  private Object readResolve(){
    return INSTANCE;
  }
}
