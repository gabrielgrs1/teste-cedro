package com.gabrielgrs1.teste_cedro_android.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.gabrielgrs1.teste_cedro_android.R;
import com.gabrielgrs1.teste_cedro_android.dao.NewURLDAO;
import com.gabrielgrs1.teste_cedro_android.model.NewURL;
import com.gabrielgrs1.teste_cedro_android.model.User;
import com.kazakago.cryptore.CipherAlgorithm;
import com.kazakago.cryptore.Cryptore;
import com.kazakago.cryptore.DecryptResult;
import com.kazakago.cryptore.EncryptResult;

public class DetailActivity extends AppCompatActivity {

    public EditText fieldURL;
    public EditText fieldMail;
    public EditText fieldPassword;
    public User user;
    public NewURL newURL;
    private ImageView fieldImage;
    private Button btnDelete;
    private Button btnCopy;
    private Button btnSave;
    private Intent goToManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get Extras
        if (getIntent().getExtras().get("user") != null) {
            user = (User) getIntent().getExtras().get("user");
        }
        if (getIntent().getExtras().get("newURL") != null) {
            newURL = (NewURL) getIntent().getExtras().get("newURL");
        }

        // Get Fields
        fieldURL = findViewById(R.id.tv_url_detalhes);
        fieldMail = findViewById(R.id.tv_email_detalhes);
        fieldPassword = findViewById(R.id.tv_senha_detalhes);
        fieldImage = findViewById(R.id.img_detalhes);
        btnDelete = findViewById(R.id.btn_excluir_site);
        btnCopy = findViewById(R.id.btn_copiar_senha);
        btnSave = findViewById(R.id.btn_salvar);

        // Set Fields
        fieldURL.setText(newURL.getUrl());
        fieldMail.setText(newURL.getUser());
        // Decrypt and set field password
        try {
            fieldPassword.setText(decrypt(newURL.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Image loading
        GlideUrl glideUrl = new GlideUrl("https://dev.people.com.ai/mobile/api/v2/logo/" + newURL.getUrl(), new LazyHeaders
                .Builder()
                .addHeader("authorization", user.getToken())
                .build());

        Glide
                .with(this)
                .load(glideUrl)
                .into(fieldImage);

        // Cancel ProgressDialog
        ManagerActivity.imageLoading.cancel();

        // Instance intent to back manager
        goToManager = new Intent(DetailActivity.this, ManagerActivity.class);
        goToManager.putExtra("user", user);

        // Click event listeners
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailActivity.this)
                        .setTitle("Confirmação")
                        .setMessage("Você tem certeza que deseja excluir esse registro? Essa ação é irreversível!")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new NewURLDAO(DetailActivity.this).delete(newURL);

                                Toast.makeText(DetailActivity.this, "Site excluído!", Toast.LENGTH_SHORT).show();
                                startActivity(goToManager);
                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newURL.setUser(fieldMail.getText().toString());
                newURL.setUrl(fieldURL.getText().toString());
                newURL.setPassword(fieldPassword.getText().toString());


                try {
                    newURL.setPassword(encrypt(newURL.getPassword()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new NewURLDAO(DetailActivity.this).update(newURL);

                startActivity(goToManager);
                Toast.makeText(DetailActivity.this, "Registro editado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Copy password to clipboard
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) DetailActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                if (!fieldPassword.getText().toString().equals("")) {
                    try {
                        clipboard.setText(decrypt(newURL.getPassword()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(DetailActivity.this, "Senha copiada para a area de transferência!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_voltar:
                startActivity(goToManager);
                break;
            default:
                Toast.makeText(DetailActivity.this, "Clique inválido! Reinicie a aplicação", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);

        return true;
    }

    private Cryptore getCryptore(Context context, String alias) throws Exception {
        Cryptore.Builder builder = new Cryptore.Builder(alias, CipherAlgorithm.RSA);
        builder.setContext(context); //Need Only RSA on below API Lv22.
        return builder.build();
    }

    private String decrypt(String encryptedStr) throws Exception {
        byte[] encryptedByte = Base64.decode(encryptedStr, Base64.DEFAULT);
        DecryptResult result = getCryptore(this, user.getEmail() + newURL.getUrl()).decrypt(encryptedByte, null);
        return new String(result.getBytes());
    }

    private String encrypt(String plainStr) throws Exception {
        byte[] plainByte = plainStr.getBytes();
        EncryptResult result = getCryptore(this, user.getEmail() + newURL.getUrl()).encrypt(plainByte);
        return Base64.encodeToString(result.getBytes(), Base64.DEFAULT);
    }

}
