package com.gabrielgrs1.teste_cedro_android.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gabrielgrs1.teste_cedro_android.R;
import com.gabrielgrs1.teste_cedro_android.adapter.NewURLAdapter;
import com.gabrielgrs1.teste_cedro_android.dao.NewURLDAO;
import com.gabrielgrs1.teste_cedro_android.model.NewURL;
import com.gabrielgrs1.teste_cedro_android.model.User;

import java.util.List;

public class ManagerActivity extends AppCompatActivity {

    public static ProgressDialog imageLoading;
    public User user;
    private ListView listViewURL;
    private FloatingActionButton buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        // Get Extras
        if (getIntent().getExtras().get("user") != null) {
            user = (User) getIntent().getExtras().get("user");
        }

        if (NewURLRegisterActivity.registerDialog != null) {
            NewURLRegisterActivity.registerDialog.cancel();
        }

        verifyIsLogged();

        // Get Fields
        buttonAdd = findViewById(R.id.novo_site);
        listViewURL = findViewById(R.id.lista_site);


        // Click event listeners
        listViewURL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                NewURL newURL = (NewURL) listViewURL.getItemAtPosition(position);
                newURL.setMailUser(user.getEmail());

                imageLoading = ProgressDialog.show(ManagerActivity.this, null, "Carregando dados!", true, true);

                Intent goToDetails = new Intent(ManagerActivity.this, DetailActivity.class);
                goToDetails
                        .putExtra("newURL", newURL)
                        .putExtra("user", user);

                startActivity(goToDetails);
            }
        });

        registerForContextMenu(listViewURL);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToUrlRegister = new Intent(ManagerActivity.this, NewURLRegisterActivity.class);
                goToUrlRegister.putExtra("user", user);
                startActivity(goToUrlRegister);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manager, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_deslogar:
                startActivity(new Intent(this, LoginActivity.class));
                Toast.makeText(ManagerActivity.this, "Deslogado com sucesso!", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final NewURL newURL = (NewURL) listViewURL.getItemAtPosition(info.position);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmação")
                .setMessage("Você tem certeza que deseja deslogar?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ManagerActivity.this, LoginActivity.class));
                        Toast.makeText(ManagerActivity.this, "Deslogado com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                })
                .setNegativeButton("Não", null)
                .show();
    }

    /**
     * Verify user is logged and have valid token
     */
    private void verifyIsLogged() {
        if (user.getToken() == null || user == null || user.getToken().equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, "Sua sessão expirou, favor logar novamente!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Get the list of register of a certain user using the email to search
     */
    private void carregaLista() {
        NewURLDAO dao = new NewURLDAO(this);
        List<NewURL> newURLList = dao.searchAllRegisters(user.getEmail());

        dao.close();

        listViewURL.setAdapter(new NewURLAdapter(this, newURLList, this));
    }

}
