package istic.projet.estampille;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import istic.projet.estampille.models.APITransformateur;
import istic.projet.estampille.utils.Constants;
import istic.projet.estampille.utils.PermissionsUtils;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getName();
    private Context context;
    private Toolbar mToolBar;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ImageButton helpButton;
    private ViewPager viewPager;
    private MenuItem homeMenuItem;
    private MenuItem historyMenuItem;
    private MenuItem searchMenuItem;
    private MenuItem lookAroundMenuItem;
    private int foodOriginDarkOrange;
    private int foodOriginWhite;
    private ConstraintLayout containerView;
    private ArrayList<APITransformateur> listHistoryItems;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        this.containerView = findViewById(R.id.main_container);

        //Configures design elements
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        viewPager = findViewById(R.id.pager);
        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        foodOriginDarkOrange = ResourcesCompat.getColor(getResources(), R.color.FoodOriginDarkOrange, null);
        foodOriginWhite = ResourcesCompat.getColor(getResources(), R.color.FoodOriginWhite, null);
        mProgressDialog = new ProgressDialog(context, R.style.FoodOriginAlertDialog);
        mProgressDialog.setMessage(getString(R.string.loading_dialog_message));
        mProgressDialog.setIndeterminate(true);
        //Detects everything that's potentially suspect and write it in log
        StrictMode.VmPolicy builder = new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(builder);

    }

    /**
     * Check is the phone is connected to internet, if it is, the application proceed as usual,
     * if not, a new page is loaded, telling the user that the phone can't access internet.
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.helpButton) {
            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        homeMenuItem = menu.findItem(R.id.action_home);
        historyMenuItem = menu.findItem(R.id.action_history);
        searchMenuItem = menu.findItem(R.id.action_write_code);
        lookAroundMenuItem = menu.findItem(R.id.action_look_around);
        helpButton = findViewById(R.id.helpButton);
        helpButton.setOnClickListener(this);
        try {
            setFocusOnHomeItem();
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewPager.addOnPageChangeListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                try {
                    setFocusOnHomeItem();
                    viewPager.setCurrentItem(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.action_write_code:
                if (checkInternetConnexion()) {
                    setFocusOnSearchItem();
                    viewPager.setCurrentItem(1);
                }
                return true;
            case R.id.action_history:
                try {
                    setFocusOnHistoryItem();
                    viewPager.setCurrentItem(2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.action_look_around:
                if (checkInternetConnexion()) {
                    setFocusOnLookAroundItem();
                    viewPager.setCurrentItem(3);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            try {
                setFocusOnHomeItem();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (position == 1) {
            if (checkInternetConnexion()) {
                setFocusOnSearchItem();
            }
        } else if (position == 2) {
            try {
                setFocusOnHistoryItem();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (position == 3) {
            if (checkInternetConnexion()) {
                setFocusOnLookAroundItem();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Sets the focus on the "home" (first from left) menu item. Displays the button to delete the history.
     *
     * @throws IOException throws an {@link IOException} if something goes wrong
     */
    private void setFocusOnHomeItem() throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            homeMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginWhite, BlendMode.SRC_ATOP));
            historyMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkOrange, BlendMode.SRC_ATOP));
            searchMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkOrange, BlendMode.SRC_ATOP));
            lookAroundMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkOrange, BlendMode.SRC_ATOP));
        } else {
            homeMenuItem.getIcon().setColorFilter(foodOriginWhite, PorterDuff.Mode.SRC_ATOP);
            historyMenuItem.getIcon().setColorFilter(foodOriginDarkOrange, PorterDuff.Mode.SRC_ATOP);
            searchMenuItem.getIcon().setColorFilter(foodOriginDarkOrange, PorterDuff.Mode.SRC_ATOP);
            lookAroundMenuItem.getIcon().setColorFilter(foodOriginDarkOrange, PorterDuff.Mode.SRC_ATOP);
        }
        viewPager.clearFocus();
    }

    /**
     * Sets the focus on the "search" (third from left) menu item. Displays the button to delete the history.
     */
    private void setFocusOnSearchItem() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            homeMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkOrange, BlendMode.SRC_ATOP));
            historyMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkOrange, BlendMode.SRC_ATOP));
            searchMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginWhite, BlendMode.SRC_ATOP));
            lookAroundMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkOrange, BlendMode.SRC_ATOP));
        } else {
            homeMenuItem.getIcon().setColorFilter(foodOriginDarkOrange, PorterDuff.Mode.SRC_ATOP);
            historyMenuItem.getIcon().setColorFilter(foodOriginDarkOrange, PorterDuff.Mode.SRC_ATOP);
            searchMenuItem.getIcon().setColorFilter(foodOriginWhite, PorterDuff.Mode.SRC_ATOP);
            lookAroundMenuItem.getIcon().setColorFilter(foodOriginDarkOrange, PorterDuff.Mode.SRC_ATOP);
        }
    }

    /**
     * Sets the focus on the "history" (second from left) menu item. Displays the button to delete the history.
     *
     * @throws IOException throws an {@link IOException} if something goes wrong
     */
    private void setFocusOnHistoryItem() throws IOException {
        this.loadHistoryFragment();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            homeMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkOrange, BlendMode.SRC_ATOP));
            historyMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginWhite, BlendMode.SRC_ATOP));
            searchMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkOrange, BlendMode.SRC_ATOP));
            lookAroundMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkOrange, BlendMode.SRC_ATOP));
        } else {
            homeMenuItem.getIcon().setColorFilter(foodOriginDarkOrange, PorterDuff.Mode.SRC_ATOP);
            historyMenuItem.getIcon().setColorFilter(foodOriginWhite, PorterDuff.Mode.SRC_ATOP);
            searchMenuItem.getIcon().setColorFilter(foodOriginDarkOrange, PorterDuff.Mode.SRC_ATOP);
            lookAroundMenuItem.getIcon().setColorFilter(foodOriginDarkOrange, PorterDuff.Mode.SRC_ATOP);
        }
        viewPager.clearFocus();
    }

    /**
     * Sets the focus on the "look around" (fourth from left) menu item. Displays the button to delete the history.
     */
    private void setFocusOnLookAroundItem() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionsUtils.checkPermission(this, containerView, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PermissionsUtils.permission_geoloc_explain, PermissionsUtils.REQUEST_CODE_LOCATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            homeMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkOrange, BlendMode.SRC_ATOP));
            historyMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkOrange, BlendMode.SRC_ATOP));
            searchMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkOrange, BlendMode.SRC_ATOP));
            lookAroundMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginWhite, BlendMode.SRC_ATOP));
        } else {
            homeMenuItem.getIcon().setColorFilter(foodOriginDarkOrange, PorterDuff.Mode.SRC_ATOP);
            historyMenuItem.getIcon().setColorFilter(foodOriginDarkOrange, PorterDuff.Mode.SRC_ATOP);
            searchMenuItem.getIcon().setColorFilter(foodOriginDarkOrange, PorterDuff.Mode.SRC_ATOP);
            lookAroundMenuItem.getIcon().setColorFilter(foodOriginWhite, PorterDuff.Mode.SRC_ATOP);
        }
        viewPager.clearFocus();
    }

    /**
     * Reads the history file content and displays the history in the main page
     */
    public void loadHistoryFragment() {
        String fileName = Constants.HISTORY_FILE_NAME;
        ListView listView = findViewById(R.id.listView);
        listHistoryItems = new ArrayList<APITransformateur>();
        Object objectRead = null;
        File file = new File(this.context.getFilesDir(), "" + File.separator + fileName);
        if (file.exists()) {
            try (FileInputStream fileInputStream = context.openFileInput(fileName); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                while ((objectRead = objectInputStream.readObject()) != null) {
                    if (objectRead instanceof APITransformateur) {
                        boolean alreadyPresent = false;
                        // delete the duplicate lines
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            APITransformateur finalObjectRead = (APITransformateur) objectRead;
                            alreadyPresent = listHistoryItems.stream().anyMatch(o -> o.getNumAgrement().equals((finalObjectRead).getNumAgrement()));
                        } else {
                            Iterator<APITransformateur> it = listHistoryItems.iterator();
                            while (!alreadyPresent && it.hasNext()) {
                                Object finalObjectRead = it.next();
                                if (finalObjectRead != null && finalObjectRead.equals(objectRead)) {
                                    alreadyPresent = true;
                                }
                            }
                        }
                        if (!alreadyPresent) {
                            listHistoryItems.add((APITransformateur) objectRead);
                        }
                    }
                }
            } catch (IOException e) {
                Log.wtf(TAG, "fin du fichier d'historique");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            //Displays the tutorial image if there are no history
            HistoryFragment.getInstance().setHistoryFragmentComponentsVisibility(listHistoryItems.size() == 0);

            //Changes the adapter list
            HistoryAdapter adapter = new HistoryAdapter(this,this, listHistoryItems, mProgressDialog);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionsUtils.REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && permissions.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Fragment lookAroundFragment = (LookAroundFragment) fragmentPagerAdapter.instantiateItem(viewPager, 3);
                    lookAroundFragment.onResume();
                } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
                    PermissionsUtils.displayOptions(this, containerView, PermissionsUtils.permission_geoloc_params);
                } else {
                    PermissionsUtils.explain(this, containerView, permissions[0], requestCode, PermissionsUtils.permission_geoloc_explain);
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * prevent the application to going back to the phone's main menu screen if
     * the back button is pressed when in another fragment than the home page
     */
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
            try {
                setFocusOnHomeItem();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            super.onBackPressed();
        }
    }
}