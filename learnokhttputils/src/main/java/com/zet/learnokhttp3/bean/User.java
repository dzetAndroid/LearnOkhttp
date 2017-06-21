package com.zet.learnokhttp3.bean;

/**
 * Created by Zet on 2017/6/21.
 */

public class User {

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String username;
    public String password;
}
