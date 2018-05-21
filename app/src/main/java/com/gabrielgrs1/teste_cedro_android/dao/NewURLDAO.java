package com.gabrielgrs1.teste_cedro_android.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.gabrielgrs1.teste_cedro_android.model.NewURL;

import java.util.ArrayList;
import java.util.List;

public class NewURLDAO extends SQLiteOpenHelper {


    public NewURLDAO(Context context) {
        super(context, "NewURL", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Sites (id INTEGER PRIMARY KEY, " +
                "url VARCHAR(50) NOT NULL, " +
                "usuario VARCHAR(50) NOT NULL, " +
                "emailUsuario INTEGER NOT NULL, " +
                "senha varchar(60) NOT NULL" +
                ");";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @NonNull
    private ContentValues takeDataUrl(NewURL newURL) {
        ContentValues dados = new ContentValues();
        dados.put("url", newURL.getUrl());
        dados.put("usuario", newURL.getUser());
        dados.put("emailUsuario", newURL.getMailUser());
        dados.put("senha", newURL.getPassword());
        return dados;
    }

    /**
     * Insert new register for data url
     *
     * @param newURL url object
     */
    public void insert(NewURL newURL) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = takeDataUrl(newURL);

        db.insert("Sites", null, dados);
    }

    /**
     * Get all url registers for determinated user
     *
     * @param userMail user email
     * @return returns a list of url records
     */
    public List<NewURL> searchAllRegisters(String userMail) {
        String sql = "SELECT * FROM Sites WHERE emailUsuario = ?;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, new String[]{userMail});

        List<NewURL> newURLList = new ArrayList<>();
        while (c.moveToNext()) {
            NewURL newURL = new NewURL();
            newURL.setId(c.getInt(c.getColumnIndex("id")));
            newURL.setUrl(c.getString(c.getColumnIndex("url")));
            newURL.setUser(c.getString(c.getColumnIndex("usuario")));
            newURL.setMailUser(c.getString(c.getColumnIndex("emailUsuario")));
            newURL.setPassword(c.getString(c.getColumnIndex("senha")));
            newURLList.add(newURL);
        }

        c.close();
        return newURLList;
    }


    /**
     * Remove a url object from url table
     *
     * @param newURL url object
     */
    public void delete(NewURL newURL) {
        SQLiteDatabase db = getWritableDatabase();

        String[] params = {Integer.toString(newURL.getId())};
        db.delete("Sites", "id = ?", params);
    }

    /**
     * Update a url object from url table
     *
     * @param newURL url object
     */
    public void update(NewURL newURL) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = takeDataUrl(newURL);
        String[] params = {Integer.toString(newURL.getId())};
        db.update("Sites", dados, "id = ?", params);
    }

}
