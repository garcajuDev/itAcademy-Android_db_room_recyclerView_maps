package com.itacademy.juangarcia.database_animal.Model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

@Database(entities = {Animal.class}, version = 2)
public abstract class DataBase extends RoomDatabase {

    public abstract AnimalDAO animalDAO();

    private static volatile DataBase DB;

    public static DataBase getDataBase(final Context context) {
        if (DB == null) {
            synchronized (DataBase.class) {
                if (DB == null) {
                    DB = Room.databaseBuilder(context.getApplicationContext(),
                            DataBase.class, "animalDb")
                            .build();
                }
            }
        }
        return DB;
    }
}
