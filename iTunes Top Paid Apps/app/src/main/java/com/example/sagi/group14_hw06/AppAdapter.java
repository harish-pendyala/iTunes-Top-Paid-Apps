/*
Assignment: Home Work 06
File Name: AppAdapter.java
Full Name: Harish Pendyala, Hemanth Sai Thota
 */
package com.example.sagi.group14_hw06;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by haris on 2/23/2017.
 */

public class AppAdapter extends ArrayAdapter<App> {

    Context context;
    List<App> objects;
    int resource;
    ImageView icon, fav;
    Gson gson = new Gson();


    public AppAdapter(Context context, int resource, List<App> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.resource = resource;
        //Log.d("demo", "in Adapter");
    }

    //@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Log.d("demo :", "View invoked");

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout, parent, false);
            //ViewHolder holder = new ViewHolder((ImageView) convertView.findViewById(R.id.icon),(TextView) convertView.findViewById(R.id.code));
            //convertView.setTag(holder);
        }

        //ViewHolder holder = (ViewHolder) convertView.getTag();


        icon = (ImageView) convertView.findViewById(R.id.icon);

        TextView code = (TextView) convertView.findViewById(R.id.code);
        App app1 = objects.get(position);
        if (app1.getImgURL() != null){
            Picasso.with(context).load(app1.getImgURL()).into(icon);
        }



        code.setText(app1.toString());
        fav = (ImageView) convertView.findViewById(R.id.fav);
        if (app1.isFav()){
            fav.setImageResource(R.drawable.blackstar);
        } else{
            fav.setImageResource(R.drawable.whitestar);
        }

        convertView.setTag(position);

        return convertView;
    }



 }

class ViewHolder {
    private TextView name, code;

    public ViewHolder(TextView name, TextView code) {
        this.name = name;
        this.code = code;
    }

    public TextView getName() {
        return name;
    }

    public TextView getCode() {
        return code;
    }
}
