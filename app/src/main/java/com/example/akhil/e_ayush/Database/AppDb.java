package com.example.akhil.e_ayush.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Akhil on 11-01-2018.
 */
@Database(entities = {HospitalEntity.class}, version = 1)
public abstract class AppDb extends RoomDatabase {

    private static AppDb INSTANCE;

    public abstract HospitalDao empDao();

    public static AppDb getINSTANCE(Context context){
        if (INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context,
                    AppDb.class, "DB").build();
        }
        return INSTANCE;
    }

    public static void destroyInstace(){
        INSTANCE=null;
    }
}
