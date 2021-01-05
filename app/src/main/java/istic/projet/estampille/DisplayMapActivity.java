package istic.projet.estampille;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import istic.projet.estampille.models.APITransformateur;

public class DisplayMapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    double lon = 0;
    double lat = 0;
    String[] tab = new String[0];
    String address = "";
    String siret = "";
    String name = "";
    String street = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);
        Intent intent = getIntent();
        APITransformateur transformateur = null;
        transformateur = (APITransformateur) intent.getSerializableExtra("searchedTransformateur");
        if (transformateur != null) {
//            tab = mapBundle.getStringArray("Infos");
//            assert tab != null;
//            siret = tab[1];
//            name = tab[2];
//            street = tab[3].trim().isEmpty() ? tab[3] : tab[3] + ", ";
//            address = street + tab[4] + " " + tab[5];
//            LatLng latLng = getCoords(address);
//            lon = latLng.longitude;
//            lat = latLng.latitude;
            Log.wtf("API : result act2: ", transformateur.toString());
            TextView textViewAdress = findViewById(R.id.textViewAdress);
            TextView textViewName = findViewById(R.id.textViewName);
            textViewAdress.setText(Html.fromHtml(getResources().getString(R.string.adress, transformateur.getAdresse())));
            textViewName.setText(Html.fromHtml(getResources().getString(R.string.name, transformateur.getRaisonSociale())));
        }
        ImageButton backButton = findViewById(R.id.backButton);
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

    private LatLng getCoords(String address) {
        Geocoder geocoder = new Geocoder(this.getApplicationContext());
        List<Address> addresses = new ArrayList<Address>();
        try {
            addresses = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            double latitude = addresses.get(0).getLatitude();
            double longitude = addresses.get(0).getLongitude();
            return new LatLng(latitude, longitude);
        } else {
            return new LatLng(0, 0);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backButton) {
            Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(otherActivity);
            finish();
        }
        if (view.getId() == R.id.button_know_more) {
            Toast.makeText(getApplicationContext(), R.string.wip, Toast.LENGTH_SHORT).show();
        }
    }
}