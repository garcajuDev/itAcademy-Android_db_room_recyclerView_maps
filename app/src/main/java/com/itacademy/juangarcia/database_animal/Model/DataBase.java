package com.itacademy.juangarcia.database_animal.Model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

@Database(entities = {Animal.class}, version = 1)
public abstract class DataBase extends RoomDatabase {

    public abstract AnimalDAO animalDAO();
    private static volatile DataBase DB;

    public static DataBase getDataBase(final Context context){
        if (DB == null){
            synchronized (DataBase.class){
                if (DB == null){
                  DB = Room.databaseBuilder(context.getApplicationContext(),
                          DataBase.class,"animalDb")
                          .addCallback(roomDataBaseCallback)
                          .build();
                }
            }
        }
        return DB;
    }

    public static RoomDatabase.Callback roomDataBaseCallback =
            new RoomDatabase.Callback(){
                public void onCreate(@NonNull SupportSQLiteDatabase database){
                    super.onCreate(database);
                    new PopulateDbSync(DB).execute();
                }
    };

    private static class PopulateDbSync extends AsyncTask<Void, Void, Void>{

        private final AnimalDAO dao;

        private PopulateDbSync(DataBase db) {
            dao = db.animalDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Animal animal1 = new Animal("Yoshi", "imgChuso","perro",
                    "04/12/2011", 7, true);

            dao.insert(animal1);

            Animal animal2 = new Animal("Chispi", "imgChispi", "perro1",
                    "04/12/2011", 7, false);

            dao.insert(animal2);
            return null;
        }
    }
}
