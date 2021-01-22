package istic.projet.estampille;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * LookAround page.
 */
public class LookAroundFragment extends Fragment implements OnMapReadyCallback {
    private final HashMap<String, LatLng> markersToAdd = new HashMap<>();
    private SupportMapFragment mapFragment;

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        mapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_look_around, container, false);
        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_test);
            mapFragment.getMapAsync(this);
        }
        return rootView;
    }

    /**
     * Add a marker to the map at the given coordinates.
     *
     * @param googleMap    the map we want to add a marker on
     * @param markersToAdd the coordinate where to put the marker
     */
    private void addMarkers(GoogleMap googleMap, HashMap<String, LatLng> markersToAdd) {
        for (String name : markersToAdd.keySet()) {
            googleMap.addMarker(new MarkerOptions()
                    .position(markersToAdd.get(name))
                    .title(name));
        }
    }

    /**
     * Given an address, return the latitude and longitude of the address.
     *
     * @param address the address we want to know the coordinates of
     * @return a LatLng object containing the coordinate of the given address
     */
    private LatLng getCoords(String address) {
        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
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
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()).getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(Objects.requireNonNull(locationManager.getBestProvider(criteria, true)));
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), getActivity().getApplicationContext().getString(R.string.no_gps_found), Toast.LENGTH_SHORT).show();
        }
        googleMap.setMyLocationEnabled(true);
        if (location != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(9)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}