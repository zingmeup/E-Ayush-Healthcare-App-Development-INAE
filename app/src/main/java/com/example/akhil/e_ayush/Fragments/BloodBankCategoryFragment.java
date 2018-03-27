package com.example.akhil.e_ayush.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.akhil.e_ayush.Activities.BankActivity;
import com.example.akhil.e_ayush.Helper.FragmentCallbacks;
import com.example.akhil.e_ayush.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodBankCategoryFragment extends Fragment implements View.OnClickListener{


    private FragmentCallbacks fragmentCallbacks;
    private CardView bank,vol;

    public BloodBankCategoryFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public BloodBankCategoryFragment(FragmentCallbacks fragmentCallbacks){
        this.fragmentCallbacks=fragmentCallbacks;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blood_bank_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bank=view.findViewById(R.id.blood_bank_list);
        vol=view.findViewById(R.id.blood_bank_list_volunteer);

        bank.setOnClickListener(this);
        vol.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentCallbacks.callback(3);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.blood_bank_list:
                callBank(0);
                break;
            case R.id.blood_bank_list_volunteer:
                callBank(1);
                break;
        }
    }

    private void callBank(int i) {
        Intent intent=new Intent(getContext(), BankActivity.class);
        intent.putExtra("val",i);
        startActivity(intent);
    }
}
