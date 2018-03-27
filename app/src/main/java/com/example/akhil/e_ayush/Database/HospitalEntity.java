package com.example.akhil.e_ayush.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Akhil on 16-03-2018.
 */
@Entity(tableName = "Hospitals")
public class HospitalEntity {
    @PrimaryKey
    @NonNull
    String id;
}
