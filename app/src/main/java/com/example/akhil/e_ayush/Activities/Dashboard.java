package com.example.akhil.e_ayush.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.akhil.e_ayush.Fragments.Appointment;
import com.example.akhil.e_ayush.Fragments.BloodBankCategoryFragment;
import com.example.akhil.e_ayush.Fragments.BloodFragment;
import com.example.akhil.e_ayush.Fragments.Home_fragment;
import com.example.akhil.e_ayush.Fragments.UserProfileFragment;
import com.example.akhil.e_ayush.Helper.Base;
import com.example.akhil.e_ayush.Helper.FragmentCallbacks;
import com.example.akhil.e_ayush.Helper.GPS;
import com.example.akhil.e_ayush.Models.Hospitals;
import com.example.akhil.e_ayush.Models.User;
import com.example.akhil.e_ayush.Models.UserAppoinment;
import com.example.akhil.e_ayush.R;
import com.example.akhil.e_ayush.Recievers.UpdateReciever;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.akhil.e_ayush.R.id.nav_logout;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, FragmentCallbacks, GPS.locCallback {

    private Home_fragment home_fragment;
    private FragmentManager fm;
    public ArrayList<UserAppoinment> userAppoinments = new ArrayList<>();
    private FragmentTransaction ft;
    private FrameLayout frame;
    private UpdateReciever updateReciever;
    private final int PERMISSION_REQUEST_CODE = 200;
    private LocationRequest mLocationRequest;
    private final int REQUEST_CHECK_SETTINGS = 201;
    private GPS gps;
    private Location mLocation;
    private Handler mHandler;
    private TextView cityText, blood_text;
    private ProgressDialog progressDialog;
    private int statusCode;
    private String url = Base.Basic_Url + "users/logout";
    private String msg;
    private String TAG = "DASH";
    private NavigationView navigationView;
    Menu menu;
    MenuItem menuItem;
    LinearLayout dash_appoint, dash_home, dash_user;
    private Appointment appointment;
    private BloodFragment bloodFragment;
    private Toolbar toolbar;
    private BloodBankCategoryFragment bloodBankCategoryFragment;
    private String title = "Blood Bank";
    int loc = 0;
    private UserProfileFragment userProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Base.setLang(this, getBaseContext());
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        frame = (FrameLayout) findViewById(R.id.dash_frag);
        findIds();
        fm = getSupportFragmentManager();
        set(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateReciever = new UpdateReciever();
            registerReceiver(updateReciever, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }

        location();
    }

    private void findIds() {
        progressDialog = new ProgressDialog(Dashboard.this);
        cityText = (TextView) findViewById(R.id.dash_city);

        Animation marquee = AnimationUtils.loadAnimation(this, R.anim.transition);
        cityText.startAnimation(marquee);

        blood_text = (TextView) findViewById(R.id.blood_bank_text);
        menu = navigationView.getMenu();
        menuItem = menu.findItem(R.id.nav_logout);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            menuItem.setTitle(R.string.logout_txt);
        } else {
            menuItem.setTitle(R.string.log);
        }
        dash_home = findViewById(R.id.dash_home);
        dash_appoint = findViewById(R.id.dash_appoint);
        dash_user = findViewById(R.id.dash_user);

        dash_user.setOnClickListener(this);
        dash_appoint.setOnClickListener(this);
        dash_home.setOnClickListener(this);
    }

    private void location() {
        String lat = SharedPrefManager.getInstance(this).getLat();
        String lng = SharedPrefManager.getInstance(this).getLng();
        if (lat != null && lng != null) {
            updateLoc(lat, lng);
            Log.e("deb", lat + "\n" + lng);
        } else {
            if (!checkpermission()) {
                requestpermission();
            }
            if (checkpermission()) {
                createLocationRequest();
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
                        resolvable.startResolutionForResult(Dashboard.this,
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

    private void updateLoc(String lat, String lng) {
        if (Base.isConnected(this)) {
            Geocoder geocoder = new Geocoder(Dashboard.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
                //List<Address> addresses = geocoder.getFromLocation(22.314544,87.309068, 1);
                String city;
                if (addresses.get(0).getFeatureName() != null) {
                    city = addresses.get(0).getFeatureName();
                } else {
                    if (addresses.get(0).getAdminArea() != null)
                        city = addresses.get(0).getAdminArea();
                    else
                        city = "place";
                }
                //String state=addresses.get(0).getAdminArea();
                cityText.setText(city);
                Log.e("deb", addresses.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startLoc() {
        gps = new GPS(Dashboard.this, Dashboard.this);
        mLocation = gps.getLocation();

        if (mLocation != null) {
            SharedPrefManager.getInstance(this).setLoc(mLocation.getLatitude(), mLocation.getLongitude());
            Log.e("deb", "last");
            gps.stopUpdates();
            updateLoc(String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude()));
        } else {
            mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLocation = GPS.location;
                    if (mLocation != null) {
                        SharedPrefManager.getInstance(Dashboard.this).setLoc(mLocation.getLatitude(), mLocation.getLongitude());
                        gps.stopUpdates();
                        updateLoc(String.valueOf(mLocation.getLatitude()), String.valueOf(mLocation.getLongitude()));
                    } else {
                        mHandler.postDelayed(this, 3000);
                    }
                }
            }, 3000);
        }
    }

    private void requestpermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkpermission() {
        int result = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    createLocationRequest();
                } else {

                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            unregisterReceiver(updateReciever);
    }

    private void set(int x) {
        /*
        0-->home
        1-->appointment
        2-->blood_bank
        3-->blood_bank_category
        4-->userProfile
         */
        loc = x;
        if (x == 0) {
            home_fragment = new Home_fragment(Dashboard.this);
            ft = fm.beginTransaction();
            ft.add(R.id.dash_frag, home_fragment, null);
            ft.commit();
        } else if (x == 1) {
            appointment = new Appointment(Dashboard.this);
            ft = fm.beginTransaction();
            ft.replace(R.id.dash_frag, appointment, null);
            ft.addToBackStack(appointment.getTag());
            ft.commit();
        } else if (x == 2) {
//            bloodFragment = new BloodFragment(Dashboard.this);
//            ft = fm.beginTransaction();
//            ft.replace(R.id.dash_frag, bloodFragment, null);
//            ft.addToBackStack(bloodFragment.getTag());
//            ft.commit();
            bloodBankCategoryFragment = new BloodBankCategoryFragment(Dashboard.this);
            ft = fm.beginTransaction();
            ft.replace(R.id.dash_frag, bloodBankCategoryFragment, null);
            ft.addToBackStack(bloodBankCategoryFragment.getTag());
            ft.commit();
        } else if (x == 3) {
            bloodBankCategoryFragment = new BloodBankCategoryFragment(Dashboard.this);
            ft = fm.beginTransaction();
            ft.replace(R.id.dash_frag, bloodBankCategoryFragment, null);
            ft.commit();
        } else if (x == 4) {
            userProfileFragment = new UserProfileFragment(Dashboard.this);
            ft = fm.beginTransaction();
            ft.replace(R.id.dash_frag, userProfileFragment, null);
            ft.addToBackStack(userProfileFragment.getTag());
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (loc == 0) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lang) {
            LanguageActivity.status = 1;
            startActivity(new Intent(Dashboard.this, LanguageActivity.class));
            finish();
        } else if (id == nav_logout) {
            if (SharedPrefManager.getInstance(this).isLoggedIn())
                logoutRequest();
            else {
                startActivity(new Intent(Dashboard.this, LanguageActivity.class));
                finish();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //showing progressbar
    private void showprogress() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void logoutRequest() {
        showprogress();
        final User user = SharedPrefManager.getInstance(Dashboard.this).getUser();
        Log.e("deb", user.getUniqueId());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.cancel();
                        if (statusCode == 201) {
                            try {
                                SharedPrefManager.getInstance(Dashboard.this).logout();
                                Toast.makeText(Dashboard.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("deb", "unexpected success");
                            Toast.makeText(Dashboard.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.cancel();
                        if (error.networkResponse != null) {
                            statusCode = error.networkResponse.statusCode;
                            if (statusCode == 404) {
                                Log.e("deb", "Invalid Credentials");
                                Toast.makeText(Dashboard.this, "User not found", Toast.LENGTH_SHORT).show();
                            } else if (statusCode == 403) {
                                Toast.makeText(Dashboard.this, "Invalid request", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Dashboard.this, "Check Internet", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Dashboard.this, "Check Internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("device", "m");
                params.put("token", user.getUniqueId());
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response != null) {
                    statusCode = response.statusCode;
                    msg = String.valueOf(response.data);
                }
                return super.parseNetworkResponse(response);
            }
        };
        request.setTag(TAG);
        VolleySingleton.getInstance(Dashboard.this).addToRequestQueue(request);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dash_appoint:
                set(1);
                break;
            case R.id.dash_home:
                set(0);
                break;
            case R.id.dash_user:
                if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                    set(4);
                } else {
                    popUp();
                }
                break;
        }
    }

    private void popUp() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.login_popup);

        dialog.show();

        Button yes = dialog.findViewById(R.id.pop_log);
        Button no = dialog.findViewById(R.id.pop_back);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                startActivity(new Intent(Dashboard.this, LoginActivity.class));
                finish();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    @Override
    public void callback(int i) {
        loc = i;
        if (i == 0) {
            cityText.setVisibility(View.VISIBLE);
            blood_text.setVisibility(View.GONE);
            dash_appoint.setBackgroundColor(ContextCompat.getColor(this, R.color.wht_grd));
            dash_home.setBackgroundColor(ContextCompat.getColor(this, R.color.light_bg));
            dash_user.setBackgroundColor(ContextCompat.getColor(this, R.color.wht_grd));
        } else if (i == 1) {
            cityText.setVisibility(View.VISIBLE);
            blood_text.setVisibility(View.GONE);
            dash_home.setBackgroundColor(ContextCompat.getColor(this, R.color.wht_grd));
            dash_appoint.setBackgroundColor(ContextCompat.getColor(this, R.color.light_bg));
            dash_user.setBackgroundColor(ContextCompat.getColor(this, R.color.wht_grd));
        } else if (i == 2) {
            cityText.setVisibility(View.GONE);
            blood_text.setVisibility(View.VISIBLE);
            blood_text.setText(R.string.blood);
            dash_home.setBackgroundColor(ContextCompat.getColor(this, R.color.wht_grd));
            dash_appoint.setBackgroundColor(ContextCompat.getColor(this, R.color.wht_grd));
            dash_user.setBackgroundColor(ContextCompat.getColor(this, R.color.wht_grd));

        } else if (i == 3) {
            cityText.setVisibility(View.VISIBLE);
            //blood_text.setVisibility(View.VISIBLE);
            //blood_text.setText(title);
            dash_home.setBackgroundColor(ContextCompat.getColor(this, R.color.wht_grd));
            dash_appoint.setBackgroundColor(ContextCompat.getColor(this, R.color.wht_grd));
            dash_user.setBackgroundColor(ContextCompat.getColor(this, R.color.wht_grd));
        } else if (i == 4) {
            if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
                Toast.makeText(this, "You have to log in", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
            cityText.setVisibility(View.VISIBLE);
            blood_text.setVisibility(View.GONE);
            dash_home.setBackgroundColor(ContextCompat.getColor(this, R.color.wht_grd));
            dash_appoint.setBackgroundColor(ContextCompat.getColor(this, R.color.wht_grd));
            dash_user.setBackgroundColor(ContextCompat.getColor(this, R.color.light_bg));
        }
    }

    @Override
    public void nextFrag(String s, int i) {
        if (i == 3) {
            title = s;
        }
        set(i);
    }

    @Override
    public void appointForm(Hospitals hospitals) {
        Intent intent = new Intent(Dashboard.this, Appoint_Form_Activity.class);
        intent.putExtra("name", hospitals.getHospital_Name());
        intent.putExtra("srno", hospitals.getSr_No() + "");
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (VolleySingleton.getInstance(this) != null)
            VolleySingleton.getInstance(this).cancelRequest(TAG);
    }

    @Override
    public void ca(Location location) {

    }
}
