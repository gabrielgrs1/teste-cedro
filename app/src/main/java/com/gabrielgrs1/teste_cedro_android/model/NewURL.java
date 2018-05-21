package com.gabrielgrs1.teste_cedro_android.model;

import java.io.Serializable;

public class NewURL implements Serializable {
    private int id;
    private String url;
    private String user;
    private String mailUser;
    private String password;

    public NewURL() {
    }

    public NewURL(String url, String user, String mailUser, String password) {
        this.url = url;
        this.user = user;
        this.mailUser = mailUser;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "NewURL{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", mailUser='" + mailUser + '\'' +
                '}';
    }
}
