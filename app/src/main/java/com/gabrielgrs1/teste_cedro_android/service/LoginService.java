package com.gabrielgrs1.teste_cedro_android.service;

import com.gabrielgrs1.teste_cedro_android.model.Token;
import com.gabrielgrs1.teste_cedro_android.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("login")
    Call<Token> login(@Body User user);
}
