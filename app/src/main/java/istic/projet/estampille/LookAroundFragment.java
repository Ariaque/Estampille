package istic.projet.estampille;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.List;
import java.util.Objects;

public class LookAroundFragment extends Fragment {
    private SupportMapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_look_around, container, false);
        Log.e("NearMeTest", "Début");
        if (mapFragment == null) {
            Log.e("NearMeTest", "FragmentExist");
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_test);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Log.e("NearMeTest", "DébutOnMapReady");
                    if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    Criteria criteria = new Criteria();
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                    googleMap.setMyLocationEnabled(true);

                    if (location != null) {
                        Log.e("NearMeTest", "LocationExist");
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                                .zoom(9)                   // Sets the zoom
                                .build();                   // Creates a CameraPosition from the builder
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }


                    Log.e("NearMeTest", "AvantFindNearMe");
                    //Find near me
                    InputStream is = null;
                    try {
                        Log.e("NearMeTest", "tryis");
                        is = new FileInputStream(new File(Environment
                                .getExternalStorageDirectory().toString()
                                + "/data/foodorigin_datagouv.txt"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.e("NearMeTest", "catchis : " + e.toString());
                    }
                    if (is != null) {
                        Log.e("NearMeTest", "isExist");
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(is, StandardCharsets.UTF_8)
                        );

                        Log.e("NearMeTest", "avantwhile");
                        String aCompanyLine = "";
                        try {
                            while ((aCompanyLine = reader.readLine()) != null) {
                                Log.e("NearMeTest", "while");
                                String[] tab = aCompanyLine.split(";");
                                String cedex = tab[4].substring(0,2);
                                int cedexInt = Integer.parseInt(cedex);
                                if(cedexInt == 35)
                                {
                                    String address = tab[3] + ", " + tab[4] + " " + tab[5];
                                    LatLng latLng = getCoords(address);
                                    double lonProd = latLng.longitude;
                                    double latProd = latLng.latitude;

                                    double lonMe = location.getLongitude();
                                    double latMe = location.getLatitude();

                                    double latDist = latMe - latProd;
                                    double lonDist = lonMe - lonProd;

                                    if(Math.abs(latDist) < 0.4 && Math.abs(lonDist) < 0.4){
                                        LatLng coords = new LatLng(latProd, lonProd);
                                        googleMap.addMarker(new MarkerOptions()
                                                .position(coords)
                                                .title(tab[2]));
                                    }
                                }

                            }

                        } catch (IOException e) {
                            Log.wtf("Erreur dans la lecture du CSV " + aCompanyLine, e);
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        // R.id.map is a FrameLayout, not a Fragment
        //getChildFragmentManager().beginTransaction().replace(R.id.map_test, mapFragment).commit();
        return rootView;
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
}