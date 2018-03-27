package com.example.akhil.e_ayush.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.akhil.e_ayush.Adapters.MyAppointment;
import com.example.akhil.e_ayush.Adapters.OffAppointAdapter;
import com.example.akhil.e_ayush.Dialogs.CustomSurveyDialog;
import com.example.akhil.e_ayush.Dialogs.LocDialog;
import com.example.akhil.e_ayush.Helper.Base;
import com.example.akhil.e_ayush.Helper.FragmentCallbacks;
import com.example.akhil.e_ayush.Helper.GPS;
import com.example.akhil.e_ayush.Models.Hospitals;
import com.example.akhil.e_ayush.Models.OffHospital;
import com.example.akhil.e_ayush.R;
import com.example.akhil.e_ayush.Singleton.SharedPrefManager;
import com.example.akhil.e_ayush.Singleton.VolleySingleton;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;

public class EmergencyActivity extends AppCompatActivity implements GPS.locCallback, FragmentCallbacks, CustomSurveyDialog.Observer, LocDialog.LocObserver {

    private boolean canDrive = false, havingInternet = false, canSpeak = true;
    private final int PERMISSION_REQUEST_CODE = 100;
    private double lat = 0, lng = 0;
    private LocationRequest mLocationRequest;
    private final int REQUEST_CHECK_SETTINGS = 101;
    private GPS gps;
    private Location mLocation;
    private RecyclerView recyclerView;
    int locStatus = 0;
    private ArrayList<Hospitals> hos = new ArrayList<>();
    private FragmentCallbacks fragmentCallbacks;
    private String url = Base.Basic_Url + "hospital/";
    private String TAG = "EMER";
    private View progress;
    private final int PERMISSION_REQUEST_CODE_CALL = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        findIds();
        checkDrive();
    }

    private void findIds() {
        recyclerView = findViewById(R.id.emergency_recyclerview);
        progress = findViewById(R.id.emergency_progressbar);
        progress.setVisibility(View.VISIBLE);

        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(progress.getVisibility()==View.VISIBLE){
                    if(progress.getAlpha()<1){
                        progress.setAlpha((float) (progress.getAlpha()+(0.1)));
                    }
                    else{
                        progress.setAlpha(0);
                    }
                    handler.postDelayed(this,1000);
                }
            }
        },1000);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                Log.e("deb", "user lat " + lat + "\nuser lng " + lng);
                String dis = Base.getDistance(lat, hospitals.getLat(), hospitals.getLng(), lng);
                hospitals.setDistance(dis);
                hos.add(hospitals);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        MyAppointment adapter = new MyAppointment(this, hos, EmergencyActivity.this, 1);
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        url = url + lat + ":" + lng + "/7";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        recyclerView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        setRecyclerView(response);
                        Log.e("deb", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("deb", error.toString());
                        recyclerView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        Toast.makeText(EmergencyActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", "true");
                return params;
            }
        };
        jsonArrayRequest.setTag(TAG);
        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public void checkDrive() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.drive_dialog);

        dialog.show();

        ImageView yes = dialog.findViewById(R.id.drive_yes);
        ImageView no = dialog.findViewById(R.id.drive_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                canDrive = true;
                havingInternet = Base.isConnected(EmergencyActivity.this);
                proceed();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                canDrive = false;
                havingInternet = Base.isConnected(EmergencyActivity.this);
                proceed();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.cancel();
                    canDrive = false;
                    havingInternet = Base.isConnected(EmergencyActivity.this);
                    proceed();
                }
            }
        }, 5000);
    }

    private void proceed() {
        if (canDrive && havingInternet) {
            if (!checkpermission()) {
                requestpermission();
            } else {
                createLocationRequest();
            }
        } else if (canDrive && !havingInternet) {
            Log.e("deb", "can drive but not having internet");
            if (SharedPrefManager.getInstance(this).getLat() != null) {
                getOffData();
            } else {
                if (!checkpermission()) {
                    requestpermission();
                } else {
                    createLocationRequest();
                }
            }
        } else if (!canDrive && havingInternet) {
            calAmThroughIVRS();
            startSurvey();

        } else {
            callAmbulance();
        }
    }

    private void getOffData() {
        recyclerView.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        String myJson = inputStreamToString(this.getResources().openRawResource(R.raw.hospitaldata));
        OffHospital myModel = new Gson().fromJson(myJson, OffHospital.class);
        Log.e("deb", myModel.list.size() + "\n" + myModel.list.toString());
        for (int i = 0; i < myModel.list.size(); i++) {
            myModel.list.get(i).distance = Base.getDistance(Double.parseDouble(SharedPrefManager.getInstance(this).getLat()), Double.parseDouble(myModel.list.get(i).lat), Double.parseDouble(myModel.list.get(i).lng), Double.parseDouble(SharedPrefManager.getInstance(this).getLng()));
        }
        OffAppointAdapter adapter = new OffAppointAdapter(this, myModel.list, 0);
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

    private void calAmThroughIVRS() {
        Toast.makeText(this, "IVRS call will be generated through server", Toast.LENGTH_SHORT).show();
    }

    private void startSurvey() {
        final CustomSurveyDialog customSurveyDialog = new CustomSurveyDialog(this, this);
        customSurveyDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (customSurveyDialog.isShowing()) {
                    customSurveyDialog.cancel();
                    secSurvey();
                }
            }
        }, 5000);
    }

    private void secSurvey() {
        final LocDialog locDialog = new LocDialog(this, this);
        locDialog.show();
    }

    private void buzzer() {

    }

    private void callAmbulance() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{CALL_PHONE}, PERMISSION_REQUEST_CODE_CALL);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9650089134"));
            startActivity(intent);
        }
    }

    private boolean checkpermission() {
        int result = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestpermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CALL_PHONE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        createLocationRequest();
                } else {
                    String s = SharedPrefManager.getInstance(this).getLat();
                    String s1 = SharedPrefManager.getInstance(this).getLng();
                    if (s != null && s1 != null) {
                        lat = Double.parseDouble(s);
                        lng = Double.parseDouble(s1);
                        getData();
                    } else {
                        Toast.makeText(this, "Permission needed", Toast.LENGTH_SHORT).show();
                    }
                }
            case PERMISSION_REQUEST_CODE_CALL:
                if (grantResults.length > 0) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9650089134"));
                        startActivity(intent);
                    }
                }
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.e("deb", "success");
                startLoc();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(EmergencyActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            Log.e("deb", "onAc");
            startLoc();
        }
    }

    private void startLoc() {
        gps = new GPS(EmergencyActivity.this, EmergencyActivity.this);
        mLocation = gps.getLocation();

        if (mLocation != null) {
            locStatus++;
            SharedPrefManager.getInstance(this).setLoc(mLocation.getLatitude(), mLocation.getLongitude());
            Log.e("deb", "last");
            updateLoc(mLocation.getLatitude(), mLocation.getLongitude());
        }

    }

    private void updateLoc(double latitude, double longitude) {
        lat = latitude;
        lng = longitude;
        Log.e("deb", "update");
        if (locStatus == 1 && havingInternet) {
            getData();
        } else if (locStatus == 1) {
            getOffData();
        }
    }

    @Override
    public void ca(Location location) {
        if (location != null) {
            locStatus++;
            updateLoc(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void callback(int i) {

    }

    @Override
    public void nextFrag(String s, int i) {

    }

    @Override
    public void appointForm(Hospitals hospitals) {
        String add = "http://maps.google.com/maps?saddr=" + lat + "," + lng + "&daddr=" + hospitals.getLat() + "," + hospitals.getLng();
        Uri uri = Uri.parse(add);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    @Override
    public void response() {
        secSurvey();
    }

    @Override
    public void locCallback() {
        buzzer();
    }
}
