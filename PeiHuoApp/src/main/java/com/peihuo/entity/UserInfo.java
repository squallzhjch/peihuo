package com.peihuo.entity;

/**
 * Created by 123 on 2017/8/28.
 */

public class UserInfo {
    private String userName;
    private String userId;
    private String account;
    private String rale;// 用户角色
    private String repositoryid;//仓库号

    public String getRepositoryid() {
        return repositoryid;
    }

    public void setRepositoryid(String repositoryid) {
        this.repositoryid = repositoryid;
    }

    public String getUrole() {
        return rale;
    }

    public void setUrole(String urole) {
        this.rale = urole;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
