package com.example.akhil.e_ayush.Models;

/**
 * Created by Akhil on 06-09-2017.
 */

public class User {
    private String uniqueId, mail, imei,name,mobileNumber,bloodGroup,familyMobileNumber;
    private boolean bloodDonor;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFamilyMobileNumber() {
        return familyMobileNumber;
    }

    public void setFamilyMobileNumber(String familyMobileNumber) {
        this.familyMobileNumber = familyMobileNumber;
    }

    public boolean isBloodDonor() {
        return bloodDonor;
    }

    public void setBloodDonor(boolean bloodDonor) {
        this.bloodDonor = bloodDonor;
    }
}
