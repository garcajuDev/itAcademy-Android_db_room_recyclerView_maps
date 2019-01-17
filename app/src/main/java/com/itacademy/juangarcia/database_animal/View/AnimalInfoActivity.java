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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.itacademy.juangarcia.database_animal.Model.Animal;
import com.itacademy.juangarcia.database_animal.R;

public class AnimalInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String MAP_KEY = "MapKey";
    private MapView mapView;
    private GoogleMap gMap;
    EditText editTextName, editTextType, editTextAge, editTextDate,
            editTextLat, editTextLon;
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

        Bundle mapViewbundle = null;
        if (savedInstanceState != null) {
            mapViewbundle = savedInstanceState.getBundle(MAP_KEY);
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewbundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double x;
        double y;

        Intent animalFromMainactivityIntent = getIntent();
        Bundle bundle =animalFromMainactivityIntent.getBundleExtra("bundle");
        Animal currentAnimal = (Animal) bundle.getSerializable("animal");

        x = Double.valueOf(currentAnimal.getLatitude());
        y = Double.valueOf(currentAnimal.getLongitude());

        gMap = googleMap;
        gMap.setMinZoomPreference(14);

        UiSettings uiSettings = gMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);


        LatLng ny = new LatLng(x, y);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(ny));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        gMap.addMarker(markerOptions);

        gMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}
