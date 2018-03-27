package com.example.akhil.e_ayush.Models;

import java.io.Serializable;

/**
 * Created by Akhil on 19-03-2018.
 */

public class Hospitals implements Serializable{
    private double lat,lng,Sr_No;
    private String Hospital_Name,Location,distance;
    private int cell_no;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getSr_No() {
        return Sr_No;
    }

    public void setSr_No(double sr_No) {
        Sr_No = sr_No;
    }

    public String getHospital_Name() {
        return Hospital_Name;
    }

    public void setHospital_Name(String hospital_Name) {
        Hospital_Name = hospital_Name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getCell_no() {
        return cell_no;
    }

    public void setCell_no(int cell_no) {
        this.cell_no = cell_no;
    }
}
