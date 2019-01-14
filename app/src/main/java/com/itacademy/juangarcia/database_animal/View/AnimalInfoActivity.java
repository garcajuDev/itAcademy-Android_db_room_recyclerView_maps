package com.itacademy.juangarcia.database_animal.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.itacademy.juangarcia.database_animal.Model.Animal;
import com.itacademy.juangarcia.database_animal.R;

public class AnimalInfoActivity extends AppCompatActivity {

    EditText editTextName, editTextType, editTextAge, editTextDate;
    ImageView imgPhoto;
    CheckBox chkboxChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_info);

        editTextName = findViewById(R.id.txtNameInfo);
        imgPhoto = findViewById(R.id.imgInfoImage);
        editTextType = findViewById(R.id.txtTypeInfo);
        editTextDate = findViewById(R.id.txtDateInfo);
        editTextAge = findViewById(R.id.txtAgeInfo);
        chkboxChip = findViewById(R.id.chkboxChipInfo);

        Intent animalFromMainactivityIntent = getIntent();
        Bundle bundle =animalFromMainactivityIntent.getBundleExtra("bundle");
        Animal currentAnimal = (Animal) bundle.getSerializable("animal");

        fillInfo(currentAnimal);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void fillInfo(Animal currentAnimal) {
        String txtphoto = currentAnimal.getPhoto();

        editTextName.setText(currentAnimal.getName());
        imgPhoto.setImageBitmap(bse64ToBitmap(txtphoto));
        editTextType.setText(currentAnimal.getType());
        editTextDate.setText(currentAnimal.getDate());
        editTextAge.setText(String.valueOf(currentAnimal.getAge()));
        chkboxChip.setChecked(currentAnimal.isChip());
    }

    private Bitmap bse64ToBitmap(String txtphoto) {
        byte[] imageAsBytes = Base64.decode(txtphoto.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public void updateAnimal(View view) {
        Intent animalFromMainactivityIntent = getIntent();
        Bundle bundle = animalFromMainactivityIntent.getBundleExtra("bundle");
        Animal animal = (Animal) bundle.getSerializable("animal");

        Intent intentToAddAnimal = new Intent(AnimalInfoActivity.this, AddAnimalActivity.class);
        Bundle bundleReturn = new Bundle();
        bundleReturn.putSerializable("animal",animal);
        intentToAddAnimal.putExtra("bundle", bundle);
        intentToAddAnimal.putExtra("id",animal.getId());

        startActivity(intentToAddAnimal);
    }


}
