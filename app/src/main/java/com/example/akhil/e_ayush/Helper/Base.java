package com.example.akhil.e_ayush.Helper;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.akhil.e_ayush.Singleton.SharedPrefManager;
import com.example.akhil.e_ayush.Singleton.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Locale;

/**
 * Created by Akhil on 26-02-2018.
 */

public class Base {
    public static String Basic_Url = "http://188.166.191.164:80/";//"http://192.168.43.9:5050/";
    //http://188.166.191.164:80/
    public final static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public final static String contactNumberPattern = "[0-9]{10}";
    public static String disMatrix = "https://maps.googleapis.com/maps/api/distancematrix/json?";//
    // origins=30.7706972,76.5730701&destinations=30.8291941,76.5773876&key=AIzaSyBMrbfoxixuabUSOKioCCWtCoM4KxxMtSE";

    public static String getDistance(double lat1, double lat2, double lon1,
                                     double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        String s = (String.format("%.2f", distance));

        return s;
    }

    public static void setLang(Context app, Context baseApp) {
        String languageToLoad = SharedPrefManager.getInstance(app).getLang(); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        baseApp.getResources().updateConfiguration(config,
                baseApp.getResources().getDisplayMetrics());
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void getDistanceMatrix(Context context, final ForDis forDis, double sLat, double sLng, double dLat, double dLng, final int i) {
        disMatrix = disMatrix + "origins=" + sLat + "," + sLng + "&destinations=" + dLat + "," + dLng + "&key=AIzaSyBMrbfoxixuabUSOKioCCWtCoM4KxxMtSE";
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, disMatrix, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String dis, time;
                        try {
                            JSONObject js = response.getJSONArray("rows").getJSONObject(0);
                            JSONObject j = js.getJSONArray("elements").getJSONObject(0);
                            JSONObject j1 = j.getJSONObject("distance");
                            dis = j1.getString("text");
                            JSONObject j2 = j.getJSONObject("duration");
                            time = j1.getString("text");
                            forDis.getDis(dis, time, i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setTag("DIS");
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public interface ForDis {
        public void getDis(String dis, String time, int i);
    }
}
