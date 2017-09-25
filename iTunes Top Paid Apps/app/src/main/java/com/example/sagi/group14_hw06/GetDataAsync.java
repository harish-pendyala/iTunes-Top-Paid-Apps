/*
Assignment: Home Work 06
File Name: GetDataAsync.java
Full Name: Harish Pendyala, Hemanth Sai Thota
 */

package com.example.sagi.group14_hw06;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by haris on 2/23/2017.
 */

public class GetDataAsync extends AsyncTask<String, Void, ArrayList<App>> {

    IData activity;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public GetDataAsync(IData activity)
    {
        this.activity = activity;
    }

    @Override
    protected ArrayList<App> doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            StringBuilder sb = new StringBuilder();
            ArrayList<App> al = new ArrayList<App>();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while((line = br.readLine()) != null)
                sb.append(line+"\n");
            //Log.d("demo", sb.toString());
            JSONObject jo = (new JSONObject(sb.toString())).getJSONObject("feed");
            JSONArray apps = jo.getJSONArray("entry");
            for(int i=0;i<apps.length();i++)
                al.add(App.ParseApp((JSONObject) apps.get(i)));
            return al;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<App> apps) {
        //super.onPostExecute(articles);
        activity.setData(apps);
    }


    public static interface IData {
        public void setData(ArrayList<App> result);
    }


}

