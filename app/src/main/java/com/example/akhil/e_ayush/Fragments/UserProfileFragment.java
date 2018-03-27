package com.example.akhil.e_ayush.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.akhil.e_ayush.Activities.Appoint_Form_Activity;
import com.example.akhil.e_ayush.Activities.LoginActivity;
import com.example.akhil.e_ayush.Helper.FragmentCallbacks;
import com.example.akhil.e_ayush.R;
import com.example.akhil.e_ayush.Singleton.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private FragmentCallbacks fragmentCallbacks;
    private Spinner spinner;
    private String[] blood={"A +ve","A -ve","B +ve","B -ve","O +ve","O -ve","AB +ve","AB -ve"};

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public UserProfileFragment(FragmentCallbacks fragmentCallbacks) {
        // Required empty public constructor
        this.fragmentCallbacks=fragmentCallbacks;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner=view.findViewById(R.id.update_blood);
        setSpinner();
    }

    private void setSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, blood);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentCallbacks.callback(4);
        Log.e("deb", "onResume");
    }
}
