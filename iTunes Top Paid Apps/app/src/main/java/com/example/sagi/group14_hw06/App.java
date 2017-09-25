/*
Assignment: Home Work 06
File Name: App.java
Full Name: Harish Pendyala, Hemanth Sai Thota
 */
package com.example.sagi.group14_hw06;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by haris on 2/23/2017.
 */

public class App implements Serializable {
    String name, currency, imgURL;
    double amount;
    boolean fav;

    public App(String name, String currency, String imgURL, double amount, boolean fav) {
        this.name = name;
        this.currency = currency;
        this.imgURL = imgURL;
        this.amount = amount;
        this.fav = fav;
    }

    public static App ParseApp(JSONObject js) throws JSONException {
        JSONObject jo = js.getJSONObject("im:name");
        String name = jo.getString("label");
        JSONArray ja = js.getJSONArray("im:image");
        String url = ((JSONObject)ja.get(2)).getString("label");
        JSONObject jo1 = js.getJSONObject("im:price").getJSONObject("attributes");
        String currency = jo1.getString("currency");
        double amount = Double.parseDouble(jo1.getString("amount"));
//        Log.d("demo","Name: "+name);
//        Log.d("demo","URL: "+url);
//        Log.d("demo","Currency: "+currency);
//        Log.d("demo","Amount: "+amount);
        return new App(name,currency,url,amount,false);
    }

    public String toString() {
        return name+"\nPrice: "+currency +" "+amount;
    }

//    @Override
//    public String toString() {
//        return "App{" +
//                "name='" + name + '\'' +
//                ", currency='" + currency + '\'' +
//                ", imgURL='" + imgURL + '\'' +
//                ", amount=" + amount +
//                ", fav=" + fav +
//                '}';
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }
}
