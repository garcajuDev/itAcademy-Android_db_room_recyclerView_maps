package com.itacademy.juangarcia.database_animal.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.itacademy.juangarcia.database_animal.Model.Animal;
import com.itacademy.juangarcia.database_animal.Repository.Repository;

import java.util.List;

public class AnimalViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<Animal>> allAnimals;

    public AnimalViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allAnimals = repository.getAllAnimals();
    }

    public LiveData<List<Animal>> getAllAnimals(){ return allAnimals; }

    public void insert(Animal animal){ repository.insert(animal); }

    public void update(Animal animal){ repository.update(animal); }

    public void delete(Animal animal){
        repository.delete(animal);
    }

    public void deleteAll(){
        repository.deleteAll();
    }


}
