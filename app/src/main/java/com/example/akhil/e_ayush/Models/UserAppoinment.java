package com.example.akhil.e_ayush.Models;

/**
 * Created by Akhil on 20-03-2018.
 */

public class UserAppoinment {
    String hospital,doc_name,timeing,date;
    boolean confirmed;

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getTimeing() {
        return timeing;
    }

    public void setTimeing(String timeing) {
        this.timeing = timeing;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
