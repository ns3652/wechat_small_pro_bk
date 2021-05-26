package com.wyh.game_platform.pojo;


public class SysUser {

    private Integer userId;

    private String username;

    private String password;

//    private Integer modifyByUserId;         //信息被谁修改

    public SysUser(Integer userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public SysUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
