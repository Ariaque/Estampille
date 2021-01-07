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
import androidx.fragment.app.FragmentManager;

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

    private double lon = 0;
    private double lat = 0;
    private String address = "";
    private String siret = "";
    private String name = "";
    private String street = "";
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);
        Intent intent = getIntent();
        fm = this.getSupportFragmentManager();
        APITransformateur transformateur = (APITransformateur) intent.getSerializableExtra("searchedTransformateur");
        if (transformateur != null) {
            siret = transformateur.getSiret();
            name = transformateur.getRaisonSociale();
            street = transformateur.getAdresse().trim().isEmpty() ? transformateur.getAdresse() : transformateur.getAdresse() + ", ";
            address = street + transformateur.getCodePostal() + " " + transformateur.getCommune();
            LatLng latLng = getCoords(address);
            lon = latLng.longitude;
            lat = latLng.latitude;
            TextView textViewAdress = findViewById(R.id.textViewAdress);
            TextView textViewName = findViewById(R.id.textViewName);
            textViewAdress.setText(Html.fromHtml(getResources().getString(R.string.adress, address)));
            textViewName.setText(Html.fromHtml(getResources().getString(R.string.name, name)));
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
            /*startActivity(otherActivity);*/
            fm.popBackStack();
            finish();
        }
        if (view.getId() == R.id.button_know_more) {
            Toast.makeText(getApplicationContext(), R.string.wip, Toast.LENGTH_SHORT).show();
        }
    }
}