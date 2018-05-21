package com.gabrielgrs1.teste_cedro_android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielgrs1.teste_cedro_android.R;
import com.gabrielgrs1.teste_cedro_android.dao.NewURLDAO;
import com.gabrielgrs1.teste_cedro_android.model.NewURL;
import com.gabrielgrs1.teste_cedro_android.model.User;
import com.kazakago.cryptore.CipherAlgorithm;
import com.kazakago.cryptore.Cryptore;
import com.kazakago.cryptore.EncryptResult;

public class NewURLRegisterActivity extends AppCompatActivity {

    public static ProgressDialog registerDialog;
    public User user;
    private TextView textViewURL;
    private TextView textViewUser;
    private TextView textViewPassword;
    private Button btnRegister;

    private String urlString;
    private String userString;
    private String passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_url_register);

        // Get Extras
        if (getIntent().getExtras().get("user") != null) {
            user = (User) getIntent().getExtras().get("user");
        }

        // Get Fields
        textViewURL = findViewById(R.id.edt_url_cadastro_site);
        textViewUser = findViewById(R.id.edt_usuario_cadastro_site);
        textViewPassword = findViewById(R.id.edt_senha_cadastro_site);
        btnRegister = findViewById(R.id.btn_cadastro_site);

        // Click event listeners
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDialog = ProgressDialog.show(NewURLRegisterActivity.this, "Cadastrado", "Cadastrando novo registro!", true, true);

                urlString = textViewURL.getText().toString();
                userString = textViewUser.getText().toString();
                passwordString = textViewPassword.getText().toString();

                try {
                    new NewURLDAO(NewURLRegisterActivity.this)
                            .insert(new NewURL(urlString, userString, user.getEmail(), encrypt(passwordString)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent goToManager = new Intent(NewURLRegisterActivity.this, ManagerActivity.class);
                goToManager.putExtra("user", user);
                startActivity(goToManager);

                Toast.makeText(NewURLRegisterActivity.this, "Site cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_url_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_voltar:
                Intent goManager = new Intent(NewURLRegisterActivity.this, ManagerActivity.class);
                goManager.putExtra("user", user);
                startActivity(goManager);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private String encrypt(String plainStr) throws Exception {
        byte[] plainByte = plainStr.getBytes();
        EncryptResult result = getCryptore(this, user.getEmail() + urlString).encrypt(plainByte);
        return Base64.encodeToString(result.getBytes(), Base64.DEFAULT);
    }

    private Cryptore getCryptore(Context context, String alias) throws Exception {
        Cryptore.Builder builder = new Cryptore.Builder(alias, CipherAlgorithm.RSA);
        builder.setContext(context); //Need Only RSA on below API Lv22.
        return builder.build();
    }
}
