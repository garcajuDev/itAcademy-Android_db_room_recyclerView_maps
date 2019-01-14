package com.itacademy.juangarcia.database_animal.View;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.itacademy.juangarcia.database_animal.Model.Animal;
import com.itacademy.juangarcia.database_animal.R;
import com.itacademy.juangarcia.database_animal.ViewModel.AnimalViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AnimalViewModel animalViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_activity_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AnimalAdapter adapter = new AnimalAdapter();
        recyclerView.setAdapter(adapter);

        animalViewModel = ViewModelProviders.of(this).get(AnimalViewModel.class);
        animalViewModel.getAllAnimals().observe(this, new Observer<List<Animal>>() {
            @Override
            public void onChanged(@Nullable List<Animal> animals) {
                adapter.setAnimals(animals);
            }
        });

        //swipe to delete a animal
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                animalViewModel.delete(adapter.getAnimalAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Animal deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //do a element item clickable
        adapter.setOnItemClickListener(new AnimalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Animal animal) {
                Intent toInfoActivity = new Intent(MainActivity.this,
                        AnimalInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("animal",animal);
                toInfoActivity.putExtra("bundle",bundle);
                startActivity(toInfoActivity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAllNotes:
                animalViewModel.deleteAll();
                Toast.makeText(this, "All animals deleted!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addAnimal(View view) {
        Intent intent = new Intent(MainActivity.this, AddAnimalActivity.class);
        startActivity(intent);
    }
}
