package com.itacademy.juangarcia.database_animal.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.itacademy.juangarcia.database_animal.Model.Animal;
import com.itacademy.juangarcia.database_animal.Model.AnimalDAO;
import com.itacademy.juangarcia.database_animal.Model.DataBase;

import java.util.List;

public class Repository {

    private AnimalDAO dao;
    private LiveData<List<Animal>> allAnimals;

    public Repository(Application application){
        DataBase db = DataBase.getDataBase(application);
        dao = db.animalDAO();
        allAnimals = dao.getAllAnmals();
    }

    public LiveData<List<Animal>> getAllAnimals() {
        return allAnimals;
    }

    public void insert (Animal animal){
        new insertAsyncTask(dao).execute(animal);
    }

    public void update (Animal animal){
        new updateAsyncTask(dao).execute(animal);
    }

    public void delete (Animal animal){
        new deleteAsyncTask(dao).execute(animal);
    }

    public void deleteAll() { new deleteAllAsyncTask(dao).execute();}

    private static class insertAsyncTask extends AsyncTask<Animal, Void, Void> {

        private AnimalDAO asyncTaksDao;

        insertAsyncTask(AnimalDAO animalDao){
            asyncTaksDao = animalDao;
        }

        @Override
        protected Void doInBackground(final Animal... params) {
            asyncTaksDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Animal, Void, Void> {

        private AnimalDAO asyncTaksDao;

        updateAsyncTask(AnimalDAO animalDao){
            asyncTaksDao = animalDao;
        }

        @Override
        protected Void doInBackground(final Animal... params) {
            asyncTaksDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Animal, Void, Void> {

        private AnimalDAO asyncTaksDao;

        deleteAsyncTask(AnimalDAO animalDao){
            asyncTaksDao = animalDao;
        }

        @Override
        protected Void doInBackground(final Animal... params) {
            asyncTaksDao.delete(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private AnimalDAO asyncTaksDao;

        deleteAllAsyncTask(AnimalDAO animalDao){
            asyncTaksDao = animalDao;
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            asyncTaksDao.deleteAll();
            return null;
        }
    }
}
