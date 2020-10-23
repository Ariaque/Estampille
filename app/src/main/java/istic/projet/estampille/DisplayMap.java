package istic.projet.estampille;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DisplayMap extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener{

    double lon = 0;
    double lat = 0;
    String[] tab = new String[0];
    String address = "";
    String siret = "";
    String name = "";
    private ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);

        Bundle mapBundle = getIntent().getExtras();
        if(mapBundle != null){
            tab = mapBundle.getStringArray("Infos");
            assert tab != null;
            siret = tab[2];
            name = tab[3];
            address = tab[4] + ", " + tab[5] + " " + tab[6];
            LatLng latLng = getCoords(address);
            lon = latLng.longitude;
            lat = latLng.latitude;
        }

        TextView textViewAdress = (TextView) findViewById(R.id.textViewAdress);
        TextView textViewSiret = (TextView) findViewById(R.id.textViewSiret);
        TextView textViewName = (TextView) findViewById(R.id.textViewName);
        backButton = findViewById(R.id.backButton);
        textViewAdress.setText(Html.fromHtml(getResources().getString(R.string.adress,address)));
        textViewSiret.setText(Html.fromHtml(getResources().getString(R.string.siret,siret)));
        textViewName.setText(Html.fromHtml(getResources().getString(R.string.name,name)));

        backButton.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng coords = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions()
                .position(coords)
                .title(name));
        CameraPosition camPos = new CameraPosition.Builder().target(coords).zoom(8).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
    }

    private LatLng getCoords(String address){
        Geocoder geocoder = new Geocoder(this.getApplicationContext());
        List<Address> addresses = new ArrayList<Address>();
        try {
            addresses = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size() > 0) {
            double latitude= addresses.get(0).getLatitude();
            double longitude= addresses.get(0).getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            return latLng;
        }
        else{
            LatLng latLng = new LatLng(0, 0);
            return latLng;
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.backButton){
            Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(otherActivity);
            finish();
        }
    }
}