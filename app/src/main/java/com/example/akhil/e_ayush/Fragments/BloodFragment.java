package com.example.akhil.e_ayush.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.akhil.e_ayush.Adapters.BloodAdapter;
import com.example.akhil.e_ayush.Adapters.MyAppointment;
import com.example.akhil.e_ayush.Helper.FragmentCallbacks;
import com.example.akhil.e_ayush.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodFragment extends Fragment{

    private Context context;
    private FragmentCallbacks fragmentCallbacks;
    private String blood[]={"A +ve","A -ve","B +ve","B -ve","O +ve","O -ve","AB +ve","AB -ve"};

    public BloodFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public BloodFragment(FragmentCallbacks fragmentCallbacks) {
        // Required empty public constructor
        this.fragmentCallbacks=fragmentCallbacks;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blood, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRecyclerView(view);
    }

    private void setRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.blood_frg_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        BloodAdapter adapter = new BloodAdapter(blood,context,fragmentCallbacks);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentCallbacks.callback(2);
    }
}
