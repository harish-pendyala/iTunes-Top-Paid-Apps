/*
Assignment: Home Work 06
File Name: Main2Activity.java
Full Name: Harish Pendyala, Hemanth Sai Thota
 */
package com.example.sagi.group14_hw06;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Main2Activity extends AppCompatActivity {

    ListView lv;
    LinearLayout linear;
    ArrayList<App> appsList,al;
    AlertDialog ad;
    Button finish;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("iTunes Top Paid Apps");
        lv = (ListView) findViewById(R.id.list);
        linear = (LinearLayout) findViewById(R.id.linear);
        finish = (Button) findViewById(R.id.finish);
        pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        appsList = (ArrayList<App>) getIntent().getExtras().getSerializable(MainActivity.App_Key);
        //Log.d("demo2", appsList.toString());
        setData();

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


    }
    public void setData() {
        al = new ArrayList<App>();
        App app;
        for (int i =0; i<appsList.size();i++){
            app = appsList.get(i);
            if(app.isFav()){
                al.add(app);
            }

        }
        linear.setVisibility(View.INVISIBLE);
        lv.setVisibility(View.VISIBLE);
        //Log.d("List :", appsList.toString());
        AppAdapter adapter = new AppAdapter(this,R.layout.row_layout,al);
        lv.setAdapter(adapter);

    }



    protected void onImageClicked(View v) {
        final View view = v;
        LinearLayout v1 = (LinearLayout) v.getParent();
        final int index = (int) v1.getTag();
        boolean flag = false;
        if (al.get(index).isFav()){
            // remove from fav
            AlertDialog.Builder build = new AlertDialog.Builder(Main2Activity.this).setTitle("Remove From Favorites").
                    setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            al.get(index).setFav(false);
                            editor.remove(al.get(index).getName());
                            editor.commit();

                            ((ImageView)view).setImageDrawable(getResources().getDrawable(R.drawable.whitestar));
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setMessage("Are you sure that you want to remove this App from favorites?");
            ad = build.create();
            ad.show();

        }

    }

}
