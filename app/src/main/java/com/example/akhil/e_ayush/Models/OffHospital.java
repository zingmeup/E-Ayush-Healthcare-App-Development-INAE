package com.example.akhil.e_ayush.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Akhil on 22-03-2018.
 */

public class OffHospital {
    @SerializedName("records")
    public ArrayList<MyObject> list;

    static public class MyObject {
        @SerializedName("lat")
        public String lat;
        @SerializedName("lng")
        public String lng;
        @SerializedName("Sr_No")
        public  String Sr_No;
        @SerializedName("Hospital_Name")
        public String Hospital_Name;
        @SerializedName("Location")
        public String Location;
        public String distance;
        @SerializedName("Telephone")
        public String cell_no;
    }
}
