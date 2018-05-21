package com.gabrielgrs1.teste_cedro_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.gabrielgrs1.teste_cedro_android.R;
import com.gabrielgrs1.teste_cedro_android.activity.ManagerActivity;
import com.gabrielgrs1.teste_cedro_android.model.NewURL;
import com.gabrielgrs1.teste_cedro_android.model.User;

import java.util.List;


public class NewURLAdapter extends BaseAdapter {
    private final List<NewURL> newURLList;
    private final Context context;
    private final ManagerActivity activity;
    private User user;
    private TextView urlField;
    private TextView userField;
    private ImageView imageField;


    public NewURLAdapter(Context context, List<NewURL> newURLList, ManagerActivity activity) {
        this.context = context;
        this.newURLList = newURLList;
        this.activity = activity;
        user = activity.user;
    }

    @Override
    public int getCount() {
        return newURLList.size();
    }

    @Override
    public Object getItem(int position) {
        return newURLList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return newURLList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewURL newURL = newURLList.get(position);

        // Inflate view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        // Get Fields
        urlField = view.findViewById(R.id.site_lista);
        userField = view.findViewById(R.id.usuario_lista);
        imageField = view.findViewById(R.id.img_lista);

        // Set text fields
        urlField.setText(newURL.getUrl());
        userField.setText(newURL.getUser());


        // Set url image search with glide
        GlideUrl glideUrl = new GlideUrl("https://dev.people.com.ai/mobile/api/v2/logo/" + newURL.getUrl(), new LazyHeaders.Builder()
                .addHeader("authorization", user.getToken())
                .build());

        // Set image using glideurl return
        Glide
                .with(context)
                .load(glideUrl)
                .into(imageField);

        return view;
    }


}
