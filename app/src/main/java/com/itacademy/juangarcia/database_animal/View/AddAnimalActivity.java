package com.itacademy.juangarcia.database_animal.View;

import android.Manifest;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.itacademy.juangarcia.database_animal.Model.Animal;
import com.itacademy.juangarcia.database_animal.R;
import com.itacademy.juangarcia.database_animal.ViewModel.AnimalViewModel;

import java.io.ByteArrayOutputStream;
import java.security.SecurityPermission;
import java.util.Calendar;
import java.util.Objects;;

public class AddAnimalActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private final String TAG = "PlayServices";
    protected GoogleApiClient mGoogleApiClient;
    final int CAMERA = 1;
    String photo = "Placeholder String";
    AnimalViewModel animalViewModel;
    TextView textViewName, textViewType, textViewAge, textViewDate,
            textViewLat, textViewLon;
    CheckBox checkBoxChip;
    ImageView imageViewPhoto;
    final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animal);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        textViewName = findViewById(R.id.txtName);
        textViewType = findViewById(R.id.txtType);
        textViewAge = findViewById(R.id.txtAge);
        textViewDate = findViewById(R.id.txtDate);
        imageViewPhoto = findViewById(R.id.imgPhoto);
        checkBoxChip = findViewById(R.id.chkboxChip);
        textViewLat = findViewById(R.id.txtLat);
        textViewLon = findViewById(R.id.txtLon);

        Log.i(TAG, "onCreate: Building the GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: Connectiong to Google Play Services");

        GoogleApiAvailability gAPI = GoogleApiAvailability.getInstance();
        int resultCode = gAPI.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            gAPI.getErrorDialog(this, resultCode, 1).show();
        } else {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: Disconnecting from Google Play Services");
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause method called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume method called");

        animalViewModel = ViewModelProviders.of(this).get(AnimalViewModel.class);

        //regain the animal that be to update from AnimalInfoAcitvity
        Intent animalActivity = getIntent();
        if (animalActivity.hasExtra("id")) {
            setTitle("Edit Animal");
            Bundle bundle = animalActivity.getBundleExtra("bundle");
            Animal currentAnimal = (Animal) bundle.getSerializable("animal");
            fill(currentAnimal);
        } else {
            setTitle("New Animal");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected: Play services onConnected called");

        int permCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: Connection was suspended, cause code is: " + i);
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: Connection failed: ConnectionResult.getErrorCode = " +
                connectionResult.getErrorCode());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private Location getLocation() {
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            setLocationFields(location);
            return location;
        }catch (SecurityException e){
            return null;
        }
    }

    public void setLocationFields(Location location){
        Log.d(TAG, "Updating location fields");
        if (location != null){
            textViewLat.setText(String.format("%f", location.getLatitude()));
            textViewLon.setText(String.format("%f", location.getLongitude()));
        }
    }

    //fill the views values with the animal properties to update
    public void fill(Animal animal) {
        textViewName.setText(animal.getName());
        imageViewPhoto.setImageBitmap(bse64ToBitmap(animal.getPhoto()));
        textViewType.setText(animal.getType());
        textViewAge.setText(String.valueOf(animal.getAge()));
        textViewDate.setText(animal.getDate());
        checkBoxChip.setChecked(animal.isChip());
    }

    //no properties with empty values in the updated animal
    public Animal updatedAnimal(Animal ani) {
        if (!ani.getName().equals("")) {
            ani.setName(textViewName.getText().toString());
        }
        if (!ani.getPhoto().equals("")) {
            ani.setPhoto(photo);
        }
        if (!ani.getType().equals("")) {
            ani.setType(textViewType.getText().toString());
        }
        if (ani.getAge() > 0) {
            ani.setAge(Integer.parseInt(textViewAge.getText().toString()));
        }
        if (!ani.getDate().equals("")) {
            ani.setDate(textViewDate.getText().toString());
        }
        if (!ani.isChip()) {
            ani.setChip(checkBoxChip.isChecked());
        }
        return ani;
    }

    //stores a new or updated animal in DB
    private void saveAnimal() {
        String animalName = textViewName.getText().toString();
        String animalType = textViewType.getText().toString();
        int animalAge = ageToInt();
        String animalDate = textViewDate.getText().toString();
        boolean chip = hasChip();

        //form validator property
        boolean test = testInfo(animalName, animalType, animalAge, animalDate);

        //form validator
        if (test == false) {
            Toast.makeText(this, "Wrong info!", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        Intent animalActivity = getIntent();

        //Update existing Animal in DB
        if (animalActivity.hasExtra("id")) {
            Bundle bundle = animalActivity.getBundleExtra("bundle");
            Animal currentAnimal = (Animal) bundle.getSerializable("animal");

            Animal animalUpdated = updatedAnimal(currentAnimal);
            animalViewModel.update(animalUpdated);

            Intent navToMain = new Intent(this, MainActivity.class);
            startActivity(navToMain);
            //Create and stores a new Animal in DB
        } else {
            setTitle("New Animal");
            Animal toStoreAnimal = new Animal(animalName, photo, animalType,
                    animalDate, animalAge, chip);
            animalViewModel.insert(toStoreAnimal);
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_new_animal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_animal:
                saveAnimal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //form validator flag method.
    public boolean testInfo(String name, String type, int age, String date) {
        if (name.trim().isEmpty() || type.trim().isEmpty()
                || age <= 0 || date.trim().isEmpty()) {
            return false;
        }

        return true;
    }

    /*makes sure that the age property is always an integer type.
     The empty field will return 0 avoiding a NumberFormatException*/
    public int ageToInt() {
        int age;

        if (textViewAge.getText().toString().equals("")) {
            age = 0;
        } else {
            age = Integer.parseInt(textViewAge.getText().toString());
        }

        return age;
    }

    //set a registration date
    public void pickDate(View view) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textViewDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                //animalDate = date.getText().toString();
            }
        }, year, month, day);
        dialog.show();
    }

    //check if this animal has a chip or not
    public boolean hasChip() {
        if (checkBoxChip.isChecked() == true) {
            return true;
        } else {
            return false;
        }
    }

    public void doPhoto(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            Bitmap photobmp = (Bitmap) data.getExtras().get("data");
            imageViewPhoto.setImageBitmap(photobmp);
            photo = bitmapToBase64(photobmp);
        }
    }

    private String bitmapToBase64(Bitmap photobmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        photobmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap bse64ToBitmap(String txtphoto) {
        byte[] imageAsBytes = Base64.decode(txtphoto.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public void onLocation(View view) {
        getLocation();
    }
}
