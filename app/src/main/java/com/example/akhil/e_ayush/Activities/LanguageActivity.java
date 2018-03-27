package com.example.akhil.e_ayush.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.akhil.e_ayush.Adapters.LanguageAdapter;
import com.example.akhil.e_ayush.Helper.Base;
import com.example.akhil.e_ayush.R;
import com.example.akhil.e_ayush.Singleton.SharedPrefManager;

public class LanguageActivity extends AppCompatActivity implements View.OnClickListener{

    private String languageArray[]={"English","বাংলা","हिन्दी","ગુજરાતી","ਪੰਜਾਬੀ","मराठी","ಕನ್ನಡ"};
    private RecyclerView recyclerView;
    private LanguageAdapter languageAdapter;
    private LinearLayoutManager mLayoutManager;
    private Typeface typeface;
    private String lang[]={"en","bn","hi","en","en","en","en"};
    private Button confirm;
    private ImageView back;
    public static int status=0;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        if(status==0) {
            if (SharedPrefManager.getInstance(LanguageActivity.this).isLang()) {
                Log.e("deb",SharedPrefManager.getInstance(this).getLang());
                startActivity(new Intent(LanguageActivity.this, LoginActivity.class));
                finish();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!checkPermission()) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
                }
            }
        }

        back=(ImageView)findViewById(R.id.language_back);
        back.setOnClickListener(this);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        languageAdapter=new LanguageAdapter(LanguageActivity.this,languageArray);

        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(languageAdapter);

        confirm=(Button)findViewById(R.id.lang_confirm);
        confirm.setOnClickListener(this);
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
                if (!ImeiAccepted){
                    onBackPressed();
                }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lang_confirm:
                if(languageAdapter.lastSelectedPosition!=-1) {
                    if(status==0) {
                        SharedPrefManager.getInstance(this).userLang(lang[languageAdapter.lastSelectedPosition]);
                        startActivity(new Intent(LanguageActivity.this, LoginActivity.class));
                        finish();
                    }
                    else if(status==1){
                        status=0;
                        SharedPrefManager.getInstance(this).userLang(lang[languageAdapter.lastSelectedPosition]);
                        startActivity(new Intent(LanguageActivity.this, Dashboard.class));
                        finish();
                    }
                }
                break;
            case R.id.language_back:
                status=0;
                onBackPressed();
                break;
        }
    }
}
