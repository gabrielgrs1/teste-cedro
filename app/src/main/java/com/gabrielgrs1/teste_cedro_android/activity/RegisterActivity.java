package com.gabrielgrs1.teste_cedro_android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielgrs1.teste_cedro_android.R;
import com.gabrielgrs1.teste_cedro_android.callback.RegisterCallback;
import com.gabrielgrs1.teste_cedro_android.model.Token;
import com.gabrielgrs1.teste_cedro_android.model.User;
import com.gabrielgrs1.teste_cedro_android.service.RegisterService;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    public ProgressDialog registerDialog;                // Used in RegisterCallback
    public TextView textViewErrorRegister;                  // Used in RegisterCallback
    public User user;                                    // Used in RegisterCallback
    private RegisterService registerService;
    private EditText fieldMail;
    private EditText fieldPassword;
    private EditText fieldName;
    private Button btnRegister;
    private String mailString;
    private String passwordString;
    private String nameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get Fields
        btnRegister = findViewById(R.id.btn_cadastro_cadastro);
        fieldMail = findViewById(R.id.edt_email_cadastro);
        fieldPassword = findViewById(R.id.edt_senha_cadastro);
        fieldName = findViewById(R.id.edt_nome_cadastro);
        textViewErrorRegister = findViewById(R.id.tv_erro_cadastro);

        // Focus event listeners
        fieldMail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                togglErrorField(View.GONE);
            }
        });

        fieldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                togglErrorField(View.GONE);

                if (hasFocus) {
                    toggleParametersPassword(View.VISIBLE);
                } else {
                    toggleParametersPassword(View.GONE);
                }
            }
        });

        // Connection Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev.people.com.ai/mobile/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        registerService = retrofit.create(RegisterService.class);

        // Click event listeners
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mailString = fieldMail.getText().toString();
                passwordString = fieldPassword.getText().toString();
                nameString = fieldName.getText().toString();

                if (validatePassword(passwordString)) {

                    user = new User(mailString, passwordString, nameString);
                    registerDialog = ProgressDialog.show(RegisterActivity.this, "Cadastro", "Efetuando cadastro", true, true);
                    Call<Token> call = registerService.register(user);
                    call.enqueue(new RegisterCallback(RegisterActivity.this));

                } else {
                    togglErrorField(View.VISIBLE);
                    toggleParametersPassword(View.VISIBLE);

                    textViewErrorRegister.setText(R.string.tv_requisito_senha_erro);

                    // Verify device support auto focus
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        fieldPassword.setFocusable(View.FOCUSABLE);
                    }
                }
            }
        });
    }

    /**
     * Toggle textview error visibility
     *
     * @param visibility recive VISIBLE or GONE
     */
    public void togglErrorField(int visibility) {
        textViewErrorRegister.setVisibility(visibility);
    }

    /**
     * Toggle password parameters visibility
     *
     * @param visibility recive VISIBLE or GONE
     */
    public void toggleParametersPassword(int visibility) {
        findViewById(R.id.lista_requisitos_senha).setVisibility(visibility);
    }

    /**
     * After set token for user change activity to ManagerActivity
     */
    public void changeScreenAfterRegister() {
        Toast.makeText(RegisterActivity.this, "Cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();
        Intent goToManager = new Intent(RegisterActivity.this, ManagerActivity.class);
        goToManager.putExtra("user", user);
        startActivity(goToManager);
    }

    /**
     * Validate user password have 1 special character, 1 capital letter, 1 number and 10 or more characters
     *
     * @param password password informed for user
     * @return true if all parameters are valid
     */
    private boolean validatePassword(String password) {
        if (password.length() < 10) return false;

        boolean numberBool = false;
        boolean uppercaseBool = false;
        boolean lowrcaseBool = false;
        boolean specialBool = false;

        for (char c : password.toCharArray()) {
            if (c >= '0' && c <= '9') {
                numberBool = true;
            } else if (c >= 'A' && c <= 'Z') {
                uppercaseBool = true;
            } else if (c >= 'a' && c <= 'z') {
                lowrcaseBool = true;
            } else {
                specialBool = true;
            }
        }

        return numberBool && uppercaseBool && lowrcaseBool && specialBool;
    }

}
