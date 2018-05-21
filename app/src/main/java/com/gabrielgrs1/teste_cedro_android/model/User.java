package com.gabrielgrs1.teste_cedro_android.model;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String name;
    private Token token;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token.getToken();
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token.getToken() + '\'' +
                '}';
    }
}
