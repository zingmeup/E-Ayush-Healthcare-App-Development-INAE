package com.example.akhil.e_ayush.Singleton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.akhil.e_ayush.Activities.LanguageActivity;
import com.example.akhil.e_ayush.Activities.LoginActivity;
import com.example.akhil.e_ayush.Models.User;


public class SharedPrefManager {
    //the constants
    private static final String SHARED_PREF_NAME = "sharedpref";
    private static final String KEY_LANGUAGE = "keylanguage";
    private static final String KEY_ID = "keyid";
    private static final String KEY_MAIL = "keymail";
    private static final String KEY_IMEI = "imei";
    private static final String KEY_INTERNET = "internet";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG="lng";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void internet(boolean b){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_INTERNET,b);
        editor.apply();
    }

    public boolean checkInter(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean b;
        b=sharedPreferences.getBoolean(KEY_INTERNET,false);
        return b;
    }

    public void setLoc(Double lat,Double lng){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LAT,lat.toString());
        editor.putString(KEY_LNG,lng.toString());
        editor.apply();
    }

    public String getLat(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String lat;
        lat=sharedPreferences.getString(KEY_LAT, null);
        return lat;
    }

    public String getLng(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String lng;
        lng=sharedPreferences.getString(KEY_LNG, null);
        return lng;
    }

    public void userLang(String lang) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LANGUAGE, lang);
        editor.apply();
    }

    public boolean isLang() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LANGUAGE, null) != null;
    }

    public String getLang() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String lang;
        lang=sharedPreferences.getString(KEY_LANGUAGE, null);
        return lang;
    }

    public void clearLang() {
        SharedPreferences mySPrefs =mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(KEY_LANGUAGE);
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LanguageActivity.class));
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getUniqueId());
        editor.putString(KEY_MAIL, user.getMail());
        editor.putString(KEY_IMEI, user.getImei());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        User user=new User();
        user.setUniqueId(sharedPreferences.getString(KEY_ID, null));
        user.setMail(sharedPreferences.getString(KEY_MAIL,null));
        user.setImei(sharedPreferences.getString(KEY_IMEI,null));
        return user;
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences mySPrefs =mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(KEY_ID);
        editor.remove(KEY_IMEI);
        editor.remove(KEY_MAIL);
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
