package com.example.akhil.e_ayush.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Akhil on 16-03-2018.
 */
@Dao
public interface HospitalDao {
    @Insert
    void insertAll(HospitalEntity... hospitalEntities);

    @Delete
    void delete(HospitalEntity hospitalEntity);

    @Query("SELECT * FROM Hospitals")
    List<HospitalEntity> getAll();

    @Query("SELECT id FROM Hospitals")
    List<String> getN();
}
