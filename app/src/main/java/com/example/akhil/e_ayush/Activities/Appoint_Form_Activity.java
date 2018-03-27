package com.example.akhil.e_ayush.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.akhil.e_ayush.Dialogs.DatePicker;
import com.example.akhil.e_ayush.Dialogs.TimePicker;
import com.example.akhil.e_ayush.Helper.Base;
import com.example.akhil.e_ayush.Models.Hospitals;
import com.example.akhil.e_ayush.R;
import com.example.akhil.e_ayush.Singleton.SharedPrefManager;
import com.example.akhil.e_ayush.Singleton.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Appoint_Form_Activity extends AppCompatActivity implements View.OnClickListener,DatePicker.DateCall,TimePicker.TimeCallback {

    private Spinner spinner,spec;
    private Button form_date,form_time,form_book;
    private String[] doc={"NA","Dr. Batra","Dr. G.K Vihwas","Dr. Sumit"};
    private String[] specialization={"Allergist or Immunologist","Cardiologist","Gastroenterologist","Neurologist","Gynecologist"};
    private String url= Base.Basic_Url+"appointu/new";
    private TextView hos_Name;
    private int statusCode;
    private ProgressDialog progressDialog;
    String hospitalName,srNo;
    private String TAG="FORM";
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint__form_);

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            Toast.makeText(this, "You have to log in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Appoint_Form_Activity.this,LoginActivity.class));
            finish();
        }

        hospitalName=getIntent().getStringExtra("name");
        srNo=getIntent().getStringExtra("srno");
        email=SharedPrefManager.getInstance(Appoint_Form_Activity.this).getUser().getMail();
        findIds();
        setSpinner();
    }

    private void findIds() {
        progressDialog = new ProgressDialog(Appoint_Form_Activity.this);
        spinner=findViewById(R.id.form_spinner);
        spec=findViewById(R.id.form_spinner_specialization);
        form_date=findViewById(R.id.form_date);
        form_time=findViewById(R.id.form_time);
        form_book=findViewById(R.id.form_book);
        hos_Name=findViewById(R.id.form_hospitalName);

        hos_Name.setText(hospitalName);
        form_date.setOnClickListener(this);
        form_time.setOnClickListener(this);
        form_book.setOnClickListener(this);
    }

    private void setSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, doc);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, specialization);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spec.setAdapter(dataAdapter1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.form_date:
                showDatePicker();
                break;
            case R.id.form_time:
                showTimePicker();
                break;
            case R.id.form_book:
                bookingCall();
                break;
        }
    }

    private void bookingCall() {
        final String date=form_date.getText().toString();
        final String time=form_time.getText().toString();
        final String doctor=spinner.getSelectedItem().toString();
        final String specialization=spec.getSelectedItem().toString();
        if(date.equals("DATE") || time.equals("TIME")){
            Toast.makeText(this, "Select date and time", Toast.LENGTH_SHORT).show();
        }
        else{
            showprogress();
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.cancel();
                            Log.e("deb",response);
                            if(statusCode==201){
                                Toast.makeText(Appoint_Form_Activity.this, "Appointment Requested Successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                            else{
                                Toast.makeText(Appoint_Form_Activity.this, "error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.cancel();
                    Log.e("deb",error+"");
                    progressDialog.cancel();
                    if (error.networkResponse != null) {
                        statusCode = error.networkResponse.statusCode;
                        if (statusCode == 400) {
                            Toast.makeText(Appoint_Form_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Appoint_Form_Activity.this, "Error occured", Toast.LENGTH_SHORT).show();
                            Log.e("deb",error+"");
                        }
                    } else {
                        Toast.makeText(Appoint_Form_Activity.this, "Error occured...", Toast.LENGTH_SHORT).show();
                        Log.e("deb",error+" null");
                    }
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> p=new HashMap<>();
                    p.put("mobile","true");
                    return p;
                }
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("hospital", hospitalName);
                    Log.e("deb",hospitalName);
                    params.put("hospital_no", srNo);
                    Log.e("deb",srNo);
                    params.put("doc", doctor);
                    Log.e("deb",doctor);
                    params.put("specialization",specialization);
                    Log.e("deb",specialization);
                    params.put("user_id",email );
                    Log.e("deb",email);
                    params.put("timing",time);
                    Log.e("deb",time);
                    params.put("date",date);
                    Log.e("deb",date);
                    Log.e("deb",params+"");
                    return params;
                }
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response != null) {
                        statusCode = response.statusCode;
                    }
                    return super.parseNetworkResponse(response);
                }
            };
            stringRequest.setTag(TAG);
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }
    }

    private void showTimePicker() {
        DialogFragment newFragment = new TimePicker(Appoint_Form_Activity.this);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void showDatePicker() {
        DialogFragment newFragment = new DatePicker(Appoint_Form_Activity.this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void callback(int year, int month, int day) {
        form_date.setText(day+"/"+(month+1)+"/"+year);
    }

    @Override
    public void callb(int hourOfDay, int minute) {
        form_time.setText(hourOfDay+"hrs:"+minute+"min");
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
