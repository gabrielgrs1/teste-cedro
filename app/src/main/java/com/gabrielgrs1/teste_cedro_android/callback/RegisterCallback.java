package com.gabrielgrs1.teste_cedro_android.callback;

import android.view.View;

import com.gabrielgrs1.teste_cedro_android.R;
import com.gabrielgrs1.teste_cedro_android.activity.RegisterActivity;
import com.gabrielgrs1.teste_cedro_android.helper.CallbackHelper;
import com.gabrielgrs1.teste_cedro_android.model.Token;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class RegisterCallback implements retrofit2.Callback<Token> {

    private RegisterActivity activity;

    public RegisterCallback(RegisterActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onResponse(Call<Token> call, Response<Token> response) {
        if (response.isSuccessful()) {
            activity.user.setToken(response.body());
            activity.changeScreenAfterRegister();
        } else {
            try {
                if (response.errorBody() != null) {
                    activity.textViewErrorRegister.setText(CallbackHelper.getErrorMessage(response.errorBody().string()));
                }
                activity.togglErrorField(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        activity.registerDialog.cancel();
    }

    @Override
    public void onFailure(Call<Token> call, Throwable t) {
        activity.registerDialog.cancel();
        activity.textViewErrorRegister.setText(R.string.erro_conexao);
    }
}
