package com.gabrielgrs1.teste_cedro_android.callback;

import android.view.View;

import com.gabrielgrs1.teste_cedro_android.R;
import com.gabrielgrs1.teste_cedro_android.activity.LoginActivity;
import com.gabrielgrs1.teste_cedro_android.helper.CallbackHelper;
import com.gabrielgrs1.teste_cedro_android.model.Token;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class LoginCallback implements retrofit2.Callback<Token> {

    private LoginActivity activity;

    public LoginCallback(LoginActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onResponse(Call<Token> call, Response<Token> response) {
        if (response.isSuccessful()) {
            activity.user.setToken(response.body());
            activity.changeScreenAfterLogin();
        } else {
            try {
                if (response.errorBody() != null) {
                    activity.textViewErrorLogin.setText(CallbackHelper.getErrorMessage(response.errorBody().string()));
                }
                activity.textViewErrorLogin.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        activity.loginDialog.cancel();
    }

    @Override
    public void onFailure(Call<Token> call, Throwable t) {
        activity.loginDialog.cancel();
        activity.textViewErrorLogin.setText(R.string.erro_conexao);

    }
}
