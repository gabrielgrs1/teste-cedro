package com.gabrielgrs1.teste_cedro_android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielgrs1.teste_cedro_android.R;
import com.gabrielgrs1.teste_cedro_android.callback.LoginCallback;
import com.gabrielgrs1.teste_cedro_android.model.Token;
import com.gabrielgrs1.teste_cedro_android.model.User;
import com.gabrielgrs1.teste_cedro_android.service.LoginService;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    public ProgressDialog loginDialog;                 // Used in LoginCallback
    public TextView textViewErrorLogin;                 // Used in LoginCallback
    public User user;                                    // Used in LoginCallback
    private LoginService loginService;
    private EditText fieldLogin;
    private EditText fieldPassword;
    private TextView btnRegister;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get fields
        fieldLogin = findViewById(R.id.edt_login_login);
        fieldPassword = findViewById(R.id.edt_senha_login);
        textViewErrorLogin = findViewById(R.id.tv_erro_login);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_cadastro_login);

        // Connection retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev.people.com.ai/mobile/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        loginService = retrofit.create(LoginService.class);


        // Event focus listeners
        fieldLogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                textViewErrorLogin.setVisibility(View.GONE);

            }
        });

        fieldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                textViewErrorLogin.setVisibility(View.GONE);
            }
        });

        // Click event listeners
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewErrorLogin.setText("");
                textViewErrorLogin.setVisibility(View.VISIBLE);
                user = new User(fieldLogin.getText().toString(), fieldPassword.getText().toString());

                loginDialog = ProgressDialog.show(LoginActivity.this, "Login", "Efetuando login", true, true);
                Call<Token> call = loginService.login(user);
                call.enqueue(new LoginCallback(LoginActivity.this));
            }
        });

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vaiPraCadastro = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(vaiPraCadastro);
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }


    /**
     * After set token for user change activity to ManagerActivity
     */
    public void changeScreenAfterLogin() {
        Toast.makeText(LoginActivity.this, "Logado com Sucesso!", Toast.LENGTH_SHORT).show();
        Intent goToManager = new Intent(LoginActivity.this, ManagerActivity.class);
        goToManager.putExtra("user", user);
        startActivity(goToManager);
    }
}