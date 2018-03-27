package com.example.akhil.e_ayush.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.akhil.e_ayush.Helper.Base;
import com.example.akhil.e_ayush.R;
import com.example.akhil.e_ayush.Singleton.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private String pwd, cell_no, mail;
    private ProgressDialog progressDialog;
    private int statusCode;
    private String msg;
    private String TAG = "SIGNUP_TAG";
    private String url = Base.Basic_Url + "users/new";
    private Button log, sign;
    private EditText in_mail, in_pass, in_mob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Base.setLang(this, getBaseContext());
        setContentView(R.layout.activity_sign_up);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        findIds();
    }

    private void findIds() {
        progressDialog = new ProgressDialog(SignUpActivity.this);

        in_mail = (EditText) findViewById(R.id.sign_mail);
        in_pass = (EditText) findViewById(R.id.sign_pass);
        in_mob = (EditText) findViewById(R.id.sign_mob);

        log = (Button) findViewById(R.id.reg_login_btn);
        sign = (Button) findViewById(R.id.reg_sign_btn);

        log.setOnClickListener(this);
        sign.setOnClickListener(this);
    }

    private void registerUser() {
        mail = in_mail.getText().toString().trim();
        pwd = in_pass.getText().toString().trim();
        cell_no = in_mob.getText().toString().trim();
        if (!pwd.equals("") && cell_no.matches(Base.contactNumberPattern) && mail.matches(Base.emailPattern)) {
            showprogress();
            pwd= Base.sha256(pwd);
            Log.e("deb",pwd);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.cancel();
                            if (statusCode == 201) {
                                Toast.makeText(SignUpActivity.this, "Created", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.cancel();
                            if (error.networkResponse != null) {
                                statusCode = error.networkResponse.statusCode;
                                msg = String.valueOf(error.networkResponse.data);
                                if (statusCode == 400) {
                                    Toast.makeText(SignUpActivity.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                                    Log.e("deb",error+"");
                                }
                            } else {
                                Toast.makeText(SignUpActivity.this, "Error occured...", Toast.LENGTH_SHORT).show();
                                Log.e("deb",error+" null");
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", mail);
                    params.put("cell_no", cell_no);
                    params.put("pwd", pwd);
                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response != null) {
                        statusCode = response.statusCode;
                        msg = String.valueOf(response.data);
                    }
                    return super.parseNetworkResponse(response);
                }
            };
            stringRequest.setTag(TAG);
            VolleySingleton.getInstance(SignUpActivity.this).addToRequestQueue(stringRequest);
        } else {
            if (pwd.equals(""))
                in_pass.setError("Field Required");
            if (!mail.matches(Base.emailPattern))
                in_mail.setError("Email Should be correct");
            if (!cell_no.matches(Base.contactNumberPattern))
                in_mob.setError("Mobile Number Should be correct");
        }
    }

    //showing progressbar
    private void showprogress() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reg_sign_btn:
                registerUser();
                break;
            case R.id.reg_login_btn:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(VolleySingleton.getInstance(this)!=null)
            VolleySingleton.getInstance(this).cancelRequest(TAG);
    }
}
