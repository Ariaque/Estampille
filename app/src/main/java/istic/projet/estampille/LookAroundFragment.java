package istic.projet.estampille;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LookAroundFragment extends Fragment implements OnMapReadyCallback {


    private static final String TAG = LookAroundFragment.class.getName();
    private SupportMapFragment mapFragment;
    private HashMap<String, LatLng> markersToAdd = new HashMap<>();
    private ViewGroup containerView;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_look_around, container, false);

        containerView = rootView;
        getLocationPermission();
        // R.id.map is a FrameLayout, not a Fragment
        //getChildFragmentManager().beginTransaction().replace(R.id.map_test, mapFragment).commit();
        return rootView;
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this.getActivity(), "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady");
        mMap = googleMap;
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        try
        {
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, true));
        }
        catch(Exception e){
            Toast.makeText(getActivity().getApplicationContext(), getActivity().getApplicationContext().getString(R.string.no_gps_found), Toast.LENGTH_SHORT).show();
        }
        mMap.setMyLocationEnabled(true);

        if (location != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(9)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }



    private void initMap() {
        Log.d(TAG, "initMap");
        if (mapFragment == null) {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_test);
        mapFragment.getMapAsync(this);
        }
    }
    private void addMarkers(GoogleMap googleMap, HashMap<String, LatLng> markersToAdd)
    {
        for (String name: markersToAdd.keySet()) {
            googleMap.addMarker(new MarkerOptions()
                    .position(markersToAdd.get(name))
                    .title(name));
        }
    }

    private LatLng getCoords(String address){
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
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

    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this.getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("LAF permission", "getLocationPermission: getting location permissions");
            PermissionsUtils.checkPermission(this, containerView, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    "La localisation est nécessaire pour voir les industries autour de vous", Constants.REQUEST_CODE_LOCATION);
        } else {
            initMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("LAF permission", "onRequestPermissionsResult LAF");
        Log.e("LAF permissions demands", String.valueOf(permissions.length));
        Log.e("LAF permission req code", String.valueOf(requestCode));
        if (requestCode == Constants.REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && permissions.length > 0) {
                Log.e("MA permission", Arrays.toString(grantResults));
                Log.e("MA permission", String.valueOf(grantResults[0]));
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMap();
                } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
                    PermissionsUtils.displayOptions(this.getActivity(), containerView, "La permission de géolocalisation est désactivée");
                } else {
                    PermissionsUtils.explain(this.getActivity(), containerView, permissions[0], requestCode, "Cette permission est nécessaire vous situer sur la carte");
                    Toast.makeText(this.getActivity(), "Localization permission was not granted", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}