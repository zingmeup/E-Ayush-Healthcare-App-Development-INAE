package com.example.akhil.e_ayush.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.akhil.e_ayush.Adapters.AllBloodBank;
import com.example.akhil.e_ayush.Adapters.MyAdapter;
import com.example.akhil.e_ayush.Models.BloodBank;
import com.example.akhil.e_ayush.Models.VolBloodBank;
import com.example.akhil.e_ayush.R;
import com.example.akhil.e_ayush.Singleton.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BankActivity extends AppCompatActivity implements View.OnClickListener{

    int status=0;
    private RecyclerView recyler;
    LinearLayout linear;
    private ImageView back;
    private TextView bl;
    private ArrayList<BloodBank> bloodBanks;
    private ArrayList<VolBloodBank> volBloodBanks;
    private String url="https://api.data.gov.in/resource/fced6df9-a360-4e08-8ca0-f283fc74ce15?format=json&api-key=579b464db66ec23bdd000001c93a214f26d64f8d58207e014b6a2662&filters[_state]=West%20Bengal&filters[_district]=MEDINIPUR%20WEST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        status=getIntent().getIntExtra("val",0);

        bl=findViewById(R.id.blood_Dis);
        back=findViewById(R.id.blood_back);

        back.setOnClickListener(this);

        setRecycler();
    }

    private void setRecycler() {
        recyler=findViewById(R.id.bank_recyclerview);
        linear=findViewById(R.id.linear);
        recyler.setHasFixedSize(true);
        recyler.setLayoutManager(new LinearLayoutManager(this));
        if(status==0){
            bl.setText("Blood Bank");
            recyler.setVisibility(View.VISIBLE);
            linear.setVisibility(View.GONE);
            getBankData();
        }
        else{
            bl.setText("Volunteer Blood Bank");
            recyler.setVisibility(View.GONE);
            linear.setVisibility(View.VISIBLE);
            getVolData();
        }
        //recyler.setAdapter(adapter);
    }

    private void getVolData() {
        volBloodBanks=new ArrayList<>();
    }

    private void getBankData() {
        bloodBanks=new ArrayList<>();
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            try {
                                JSONArray jsonArray=response.getJSONArray("records");
                                for(int i=0;i<jsonArray.length();i++){
                                    BloodBank bloodBank=new BloodBank();
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    bloodBank.setName(jsonObject.getString("_blood_bank_name"));
                                    bloodBank.setCity(jsonObject.getString("_city"));
                                    bloodBank.setAddress(jsonObject.getString("_address"));
                                    bloodBank.setAvail(jsonObject.getString("_blood_component_available"));
                                    bloodBank.setCon(jsonObject.getString("_contact_no"));
                                    bloodBank.setEmail(jsonObject.getString("_email"));
                                    bloodBank.setMob(jsonObject.getString("_mobile"));
                                    bloodBanks.add(bloodBank);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            AllBloodBank allBloodBank=new AllBloodBank(BankActivity.this,bloodBanks);
                            recyler.setAdapter(allBloodBank);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BankActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setTag("BANK");
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.blood_back:
                onBackPressed();
                break;
        }
    }
}
