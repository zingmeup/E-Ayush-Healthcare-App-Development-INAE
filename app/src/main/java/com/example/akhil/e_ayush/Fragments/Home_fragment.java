package com.example.akhil.e_ayush.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.akhil.e_ayush.Activities.Dashboard;
import com.example.akhil.e_ayush.Activities.EmergencyActivity;
import com.example.akhil.e_ayush.Adapters.MyAdapter;
import com.example.akhil.e_ayush.Helper.Base;
import com.example.akhil.e_ayush.Helper.FragmentCallbacks;
import com.example.akhil.e_ayush.Models.UserAppoinment;
import com.example.akhil.e_ayush.R;
import com.example.akhil.e_ayush.Singleton.SharedPrefManager;
import com.example.akhil.e_ayush.Singleton.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home_fragment extends Fragment implements View.OnClickListener {

    private String url = Base.Basic_Url + "appointu/";
    private Context context;
    private FragmentCallbacks fragmentCallbacks;
    private LinearLayout dash_medicine, blood_bank, dash_aid, dash_forum, dash_consult, dash_health, dash_emergency, bookAppoint;
    private RecyclerView recyclerView;
    private int statusCode;
    private ImageView fire;
    Dashboard dashboard=new Dashboard();

    public Home_fragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public Home_fragment(FragmentCallbacks fragmentCallbacks) {
        this.fragmentCallbacks = fragmentCallbacks;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findIds(view);

        if (dashboard.userAppoinments.size() == 0) {
            if (SharedPrefManager.getInstance(context).isLoggedIn()) {
                url = url + SharedPrefManager.getInstance(context).getUser().getMail();
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.e("deb", response.toString());
                                getData(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("deb", error.toString());
                            }
                        }) {
                    @Override
                    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                        if (response != null) {
                            statusCode = response.statusCode;
                        }
                        return super.parseNetworkResponse(response);
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> p=new HashMap<>();
                        p.put("mobile","true");
                        return p;
                    }
                };
                jsonArrayRequest.setTag("App");
                VolleySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
                url = Base.Basic_Url + "appointu/";
            }
        } else {
            setRecyclerView();
        }
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        MyAdapter adapter = new MyAdapter(context, dashboard.userAppoinments);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);

        recyclerView.setNestedScrollingEnabled(false);
    }

    private void findIds(View view) {
        blood_bank = (LinearLayout) view.findViewById(R.id.dash_blood_bank);
        recyclerView = view.findViewById(R.id.dashboard_recyclerview);
        dash_emergency = view.findViewById(R.id.home_frag_emergency);
        bookAppoint = view.findViewById(R.id.linear_home_book);

        fire=view.findViewById(R.id.fire_brigade);

        Animation marquee = AnimationUtils.loadAnimation(context, R.anim.fireanim);
        fire.startAnimation(marquee);

        dash_emergency.setOnClickListener(this);
        bookAppoint.setOnClickListener(this);
        blood_bank.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentCallbacks.callback(0);
        Log.e("deb", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("deb", "onPause");
        VolleySingleton.getInstance(context).cancelRequest("App");
    }

    private void getData(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonObject = response.getJSONObject(i);
                UserAppoinment userAppoinment = new UserAppoinment();
                userAppoinment.setHospital(jsonObject.getString("hospital"));
                userAppoinment.setDoc_name(jsonObject.getString("doc_name"));
                userAppoinment.setDate(jsonObject.getString("date"));
                userAppoinment.setTimeing(jsonObject.getString("timeing"));
                userAppoinment.setConfirmed(jsonObject.getBoolean("confirmed"));
                dashboard.userAppoinments.add(userAppoinment);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setRecyclerView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dash_blood_bank:
                fragmentCallbacks.nextFrag("", 2);
                break;
            case R.id.home_frag_emergency:
                startActivity(new Intent(context, EmergencyActivity.class));
                break;
            case R.id.linear_home_book:
                fragmentCallbacks.nextFrag("", 1);
                break;
        }
    }
}
