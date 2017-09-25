/*
Assignment: Home Work 06
File Name: MainActivity.java
Full Name: Harish Pendyala, Hemanth Sai Thota
 */
package com.example.sagi.group14_hw06;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements GetDataAsync.IData {

    ListView lv;
    LinearLayout linear;
    ArrayList<App> appsList, al;
    AlertDialog ad;
    final static String App_Key = "App Key";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("iTunes Top Paid Apps");
        lv = (ListView) findViewById(R.id.list);
        linear = (LinearLayout) findViewById(R.id.linear);
        pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        if (isConected()){
            new GetDataAsync(this).execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");

        } else{
            Toast.makeText(this, "No Internet Access!",Toast.LENGTH_SHORT).show();

        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_layout, menu);
        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.refresh:

                if (isConected()){
                    new GetDataAsync(this).execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");

                } else{
                    Toast.makeText(this, "No Internet Access!",Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.fav:
                performFav();
                break;
            case R.id.asc:
                performAsc();
                break;
            case R.id.desc:
                performDesc();
                break;
        }
        return true;
    }

    private void performFav() {
        Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
        intent.putExtra(App_Key,updateData(appsList));
        startActivityForResult(intent,1);
    }

    @Override
    public void setData(ArrayList<App> result) {
        this.appsList = result;
        linear.setVisibility(View.INVISIBLE);
        lv.setVisibility(View.VISIBLE);

       AppAdapter adapter = new AppAdapter(this,R.layout.row_layout,updateData(appsList));
       lv.setAdapter(adapter);

    }
    public void saveData(App result) {


            String app_name = pref.getString(result.getName(),"");
           // Log.d("demo", app_name);
            if (app_name.equals("")){
                //store
                String app_data = gson.toJson(result);
                editor.putString(result.getName(),app_data);
                editor.commit();
            }

    }
    public ArrayList<App> updateData(ArrayList<App> result) {

        //Log.d("demo", "calling");
        al = new ArrayList<>();
        for (int i =0; i<result.size(); i++){
            App app1 = result.get(i);
           // Log.d("demo", app1.toString());
            String app_data = pref.getString(app1.getName(),"");
           // Log.d("demoHarish", "Hello: "+app_data);
            if (app_data.equals("")){
                //store

                //Log.d("demo4567", "in wrong place");
                al.add(app1);

            } else {
                // retrieve

                App app2 = gson.fromJson(app_data, App.class);
                //Log.d("demo123", app2.toString());
                al.add(app2);
            }
        }
       return al;

    }
    public void performAsc() {

        ArrayList<App> al = appsList;
        Collections.sort(al, new Comparator<App>() {
            @Override
            public int compare(App app, App t1) {
                return Double.compare(app.getAmount(), t1.getAmount());
            }
        });
        setData(al);

    }
    public void performDesc() {

        ArrayList<App> al = appsList;
        Collections.sort(al, new Comparator<App>() {
            @Override
            public int compare(App app, App t1) {
                return Double.compare(t1.getAmount(), app.getAmount());
            }
        });
        setData(al);

    }
    public boolean isConected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info!=null && info.isConnected())
            return true;
       return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("demo1", "hell");
        new GetDataAsync(this).execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");

//        if (requestCode == 1) {
//            if(resultCode == Activity.RESULT_OK){
//                new GetDataAsync(this).execute("https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json");
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//        }
    }



    protected void onImageClicked(View v) {
        final View view = v;
        LinearLayout v1 = (LinearLayout) v.getParent();
        final int index = (int) v1.getTag();
        boolean flag = false;
        if (appsList.get(index).isFav()){
            // remove from fav
            AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this).setTitle("Remove from Favorites").
                    setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            appsList.get(index).setFav(false);
                            editor.remove(appsList.get(index).getName());
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

        }else{
            //add to fav
            AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this).setTitle("Add to Favorites").setNegativeButton("No", null).
                    setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            appsList.get(index).setFav(true);
                            saveData(appsList.get(index));
                            ((ImageView)view).setImageDrawable(getResources().getDrawable(R.drawable.blackstar));


                        }
                    }).setMessage("Are you sure that you want to add this App to favorites?");
            ad = build.create();
            ad.show();
        }


    }

}
