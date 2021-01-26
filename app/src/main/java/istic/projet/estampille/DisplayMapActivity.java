package istic.projet.estampille;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
import istic.projet.estampille.utils.APICalls;
import istic.projet.estampille.utils.Constants;

public class DisplayMapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private double lon = 0;
    private double lat = 0;
    private String address = "";
    private String siret = "";
    private String name = "";
    private String street = "";
    private FragmentManager fm;
    private APITransformateur transformateur;
    private ProgressDialog mProgressDialog;
    private boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_map);
        Intent intent = getIntent();
        fm = this.getSupportFragmentManager();
        //APITransformateur transformateur = (APITransformateur) intent.getSerializableExtra("searchedTransformateur");
        transformateur = (APITransformateur) intent.getSerializableExtra(Constants.KEY_INTENT_SEARCHED_TRANSFORMATEUR);
        if (transformateur != null) {
            System.out.println("transfo:" + transformateur);
            isActive = transformateur.getKnowMoreActive();
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


            if (!isActive) {
                View button = findViewById(R.id.button_know_more);
                button.setVisibility(View.GONE);
            } else {
                View text = findViewById(R.id.textView_no_more_info);
                text.setVisibility((View.GONE));
            }
        }

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    /**
     * Check is the phone is connected to internet, if it is, the application proceed as usual,
     * if not, a new page is loaded, telling the user that the phone can't access internet
     *
     * @return true if the phone can access internet, false otherwise
     */
    private boolean checkInternetConnexion() {
        boolean result = true;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            Intent intent = new Intent(this, NoInternetActivity.class);
            startActivity(intent);
            result = false;
        }
        return result;
    }

    /**
     * Called when the google map is ready to be displayed on screen.
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng coords = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions()
                .position(coords)
                .title(name));
        CameraPosition camPos = new CameraPosition.Builder().target(coords).zoom(8).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPos));
    }

    /**
     * Given an address, return the latitude and longitude of the address
     *
     * @param address the address we want to know the coordinates of
     * @return a LatLng object containing the coordinate of the given address
     */
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

    /**
     * backButton will bring the user back to the last page,
     * knowMoreButton will reach the API and ask for more informations about the transformator.
     *
     * @param view the button pressed
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backButton) {
            Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
            /*startActivity(otherActivity);*/
            fm.popBackStack();
            finish();
        }
        if (view.getId() == R.id.button_know_more) {
            if (checkInternetConnexion()) {
                mProgressDialog = new ProgressDialog(this, R.style.FoodOriginAlertDialog);
                mProgressDialog.setMessage(getString(R.string.loading_dialog_message));
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.show();
                APICalls.searchMoreInRemoteAPI(this, String.valueOf(transformateur.getId()), mProgressDialog);
            }
        }
    }
}