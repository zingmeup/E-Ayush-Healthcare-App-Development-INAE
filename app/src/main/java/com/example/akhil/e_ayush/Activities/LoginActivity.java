package com.example.akhil.e_ayush.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.akhil.e_ayush.Helper.Base;
import com.example.akhil.e_ayush.Models.User;
import com.example.akhil.e_ayush.R;
import com.example.akhil.e_ayush.Singleton.SharedPrefManager;
import com.example.akhil.e_ayush.Singleton.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button SignBtn, logBtn;
    private int responseCode;
    private String TAG = "LOG_TAG";
    private String Email, password;
    private String url = Base.Basic_Url + "users/login";
    private EditText mail, pass;
    private ProgressDialog progressDialog;
    private String imei;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private TextView skip;
    private int c = 0;

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPrefManager.getInstance(LoginActivity.this).isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, Dashboard.class));
            finish();
        }
        Base.setLang(this, getBaseContext());
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
            }
        }
        findIds();
    }

    private void findIds() {
        progressDialog = new ProgressDialog(LoginActivity.this);

        mail = (EditText) findViewById(R.id.login_mail);
        pass = (EditText) findViewById(R.id.login_pass);

        skip = (TextView) findViewById(R.id.log_skip);

        skip.setOnClickListener(this);

        logBtn = (Button) findViewById(R.id.login_btn);
        SignBtn = (Button) findViewById(R.id.sign_btn);

        SignBtn.setOnClickListener(this);
        logBtn.setOnClickListener(this);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                boolean ImeiAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (!ImeiAccepted) {
                    super.onBackPressed();
                } else {
                    if (c == 1) {
                        c = 0;
                        loginRequest();
                    }
                }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_btn:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.login_btn:
                loginRequest();
                break;
            case R.id.log_skip:
                startActivity(new Intent(LoginActivity.this, Dashboard.class));
                finish();
                break;
        }
    }

    private void loginRequest() {
        Email = mail.getText().toString().trim();
        password = pass.getText().toString().trim();

        if (Email.equals("") || password.equals("")) {
            if (Email.equals("")) {
                mail.setError("Required");
            }
            if (password.equals("")) {
                pass.setError("Required");
            }
        } else {
            password = Base.sha256(password);
            Log.e("deb", password);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!checkPermission()) {
                    c = 1;
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
                } else {
                    req();
                }
            } else {
                req();
            }
        }
    }

    private void req() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        imei = telephonyManager.getDeviceId();
        showprogress();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.cancel();
                        if (responseCode == 200) {
                            try {
                                String token = response.getString("token");
                                Log.e("deb", token);
                                User user = new User();
                                user.setMail(Email);
                                user.setUniqueId(token);
                                user.setImei(imei);
                                SharedPrefManager.getInstance(LoginActivity.this).userLogin(user);
                                startActivity(new Intent(LoginActivity.this, Dashboard.class));
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("deb", "unexpected success " + responseCode);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.cancel();
                        if (error.networkResponse != null) {
                            responseCode = error.networkResponse.statusCode;
                            if (responseCode == 404) {
                                Log.e("deb", "Invalid Credentials");
                                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            } else if (responseCode == 403) {
                                Toast.makeText(LoginActivity.this, "Log out from aother device", Toast.LENGTH_SHORT).show();
                            } else if (responseCode == 500) {
                                Log.e("deb", error + " " + responseCode);
                                Toast.makeText(LoginActivity.this, "Sorry we are unable to connect", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("deb", "unexpected" + error + " " + responseCode);
                                Toast.makeText(LoginActivity.this, "Check Internet", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("deb", "NULL output" + error);
                            Toast.makeText(LoginActivity.this, "Check Internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response != null) {
                    responseCode = response.statusCode;
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = Email + ":" + password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                headers.put("device", "m");
                headers.put("mac", imei);
                return headers;
            }
        };
        jsonObjectRequest.setTag(TAG);
        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    //showing progressbar
    private void showprogress() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(VolleySingleton.getInstance(this)!=null)
            VolleySingleton.getInstance(this).cancelRequest(TAG);
    }
}

