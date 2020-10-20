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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LookAroundFragment extends Fragment implements OnMapReadyCallback {
    private SupportMapFragment mapFragment;
    private ViewGroup containerView;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_look_around, container, false);
        containerView = rootView;
        getLocationPermission();
        return rootView;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this.getActivity(), "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d("LookAroundFragment", "onMapReady: map is ready");
        mMap = googleMap;
        Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        if (provider == null) {
            Log.e("LookAroundFragment", "No location provider found!");
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        mMap.setMyLocationEnabled(true);

        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(9)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        //Find near me
                    /*InputStream is = null;
                    try {
                        is = new FileInputStream(new File(Environment
                                .getExternalStorageDirectory().toString()
                                + "/data/foodorigin_datagouv.txt"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (is != null) {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(is, StandardCharsets.UTF_8)
                        );

                        String aCompanyLine = "";
                        try {
                            while ((aCompanyLine = reader.readLine()) != null) {
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
                    */
    }


    private void initMap() {
        Log.d("LookAroundFragment", "initMap: initializing map");
        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_test);
            //mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
        }
    }

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
            LatLng latLng = new LatLng(latitude, longitude);
            return latLng;
        } else {
            LatLng latLng = new LatLng(0, 0);
            return latLng;
        }
    }

    private void getLocationPermission() {
        Log.d("LookAroundFragment", "getLocationPermission: getting location permissions");
        if (this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionsUtils.checkPermission(this, containerView, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    "La localisation est nécessaire pour voir les industries autour de vous", Constants.REQUEST_CODE_LOCATION);
        } else {
            initMap();
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        boolean mLocationPermissionsGranted = true;
//        if (requestCode == Constants.REQUEST_CODE_LOCATION) {
//            if (grantResults.length > 0) {
//                for (int grantResult : grantResults) {
//                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
//                        mLocationPermissionsGranted = false;
//                        Log.d("LookAroundFragment", "onRequestPermissionsResult: permission failed");
//                        return;
//                    }
//                }
//            } else {
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            }
//            if (mLocationPermissionsGranted) {
//                initMap();
//            }
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Log.e("grant results !!!", Arrays.toString(grantResults));
//        Log.e("grant results !!!", String.valueOf(grantResults[0]));
        if (requestCode == Constants.REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && permissions.length > 0) {
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