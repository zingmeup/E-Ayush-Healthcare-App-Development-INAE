package com.example.akhil.e_ayush.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.akhil.e_ayush.Activities.Dashboard;
import com.example.akhil.e_ayush.Activities.LoginActivity;
import com.example.akhil.e_ayush.Adapters.MyAdapter;
import com.example.akhil.e_ayush.Adapters.MyAppointment;
import com.example.akhil.e_ayush.Adapters.OffAppointAdapter;
import com.example.akhil.e_ayush.Helper.Base;
import com.example.akhil.e_ayush.Helper.FragmentCallbacks;
import com.example.akhil.e_ayush.Models.Hospitals;
import com.example.akhil.e_ayush.Models.OffHospital;
import com.example.akhil.e_ayush.R;
import com.example.akhil.e_ayush.Singleton.SharedPrefManager;
import com.example.akhil.e_ayush.Singleton.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Appointment extends Fragment implements View.OnClickListener {

    private FragmentCallbacks fragmentCallbacks;
    private View sort, filter, map;
    private Context context;
    private String url = Base.Basic_Url + "hospital/";///<lat>:<lng>/<limit>
    //22.31:87.30/100
    private String TAG = "Appoint";
    private RecyclerView recyclerView;
    private String lng, lat;
    private ArrayList<Hospitals> hos = new ArrayList<>();

    public Appointment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public Appointment(FragmentCallbacks fragmentCallbacks) {
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
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sort = view.findViewById(R.id.appoint_sort);
        filter = view.findViewById(R.id.appoint_filter);
        map = view.findViewById(R.id.appoint_map);

        recyclerView = view.findViewById(R.id.appointment_recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        lat = SharedPrefManager.getInstance(context).getLat();
        lng = SharedPrefManager.getInstance(context).getLng();
        if (lat != null && lng != null) {
            if (Base.isConnected(context))
                getData();
            else
                getOffData();

            sort.setOnClickListener(this);
            filter.setOnClickListener(this);
            map.setOnClickListener(this);
        }
    }

    private void getOffData() {
        String myJson = inputStreamToString(context.getResources().openRawResource(R.raw.hospitaldata));
        OffHospital myModel = new Gson().fromJson(myJson, OffHospital.class);
        Log.e("deb", myModel.list.size() + "\n" + myModel.list.toString());
        for (int i = 0; i < myModel.list.size(); i++) {
            myModel.list.get(i).distance = Base.getDistance(Double.parseDouble(lat), Double.parseDouble(myModel.list.get(i).lat), Double.parseDouble(myModel.list.get(i).lng), Double.parseDouble(lng));
        }
        OffAppointAdapter adapter = new OffAppointAdapter(context, myModel.list, 0);
        recyclerView.setAdapter(adapter);
    }

    public String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }

    private void setRecyclerView(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = response.getJSONObject(i);
                Hospitals hospitals = new Hospitals();
                hospitals.setHospital_Name(jsonObject.getString("Hospital_Name"));

                hospitals.setCell_no(jsonObject.getInt("Telephone"));
                String loc = jsonObject.getString("Location");
                loc.replace("\\n", " ");
                hospitals.setLocation(loc);
                JSONArray jsonArray = jsonObject.getJSONArray("Location_Coordinates");
                hospitals.setLat(jsonArray.getDouble(0));
                hospitals.setLng(jsonArray.getDouble(1));
                hospitals.setSr_No(jsonObject.getDouble("Sr_No"));
                String dis = Base.getDistance(Double.parseDouble(lat), hospitals.getLat(), hospitals.getLng(), Double.parseDouble(lng));
                hospitals.setDistance(dis);
                //Base.getDistanceMatrix(context, Appointment.this,Double.parseDouble(lat), hospitals.getLat(), hospitals.getLng(), Double.parseDouble(lng),i);
                hos.add(hospitals);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        MyAppointment adapter = new MyAppointment(context, hos, fragmentCallbacks, 0);
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        url = url + lat + ":" + lng + "/7";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        setRecyclerView(response);
                        Log.e("deb", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("deb", error.toString());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", "true");
                return params;
            }
        };
        jsonArrayRequest.setTag(TAG);
        VolleySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
        url = Base.Basic_Url + "hospital/";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appoint_sort:
                popUp();
                break;
            case R.id.appoint_filter:
                break;
            case R.id.appoint_map:
                break;
        }
    }

    private void popUp() {
        PopupMenu popupMenu = new PopupMenu(context, sort);

        popupMenu.getMenuInflater().inflate(R.menu.appont_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentCallbacks.callback(1);
    }
}
