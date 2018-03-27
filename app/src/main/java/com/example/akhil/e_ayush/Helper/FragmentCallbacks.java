package com.example.akhil.e_ayush.Helper;

import com.example.akhil.e_ayush.Models.Hospitals;

/**
 * Created by Akhil on 17-03-2018.
 */

public interface FragmentCallbacks {
    public void callback(int i);
    public void nextFrag(String s,int i);
    public void appointForm(Hospitals hospitals);
}
