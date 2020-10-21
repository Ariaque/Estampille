package istic.projet.estampille;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String errorFileCreate = "Error file create!";
    private static final String errorConvert = "Error convert!";
    private static final int REQUEST_IMAGE1_CAPTURE = 1;
    private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 2;

    protected String mCurrentPhotoPath;
    ImageView firstImage;
    TextView ocrText;
    int PERMISSION_ALL = 1;
    boolean flagPermissions = false;
    ListView list;

    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private ProgressDialog mProgressDialog;
    private Context context;
    private Uri photoURI1;
    private Uri oldPhotoURI;
    private Button ecrire;
    private FloatingActionButton scanButton;
    private Toolbar mToolBar;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager viewPager;
    private MenuItem historyMenuItem;
    private MenuItem searchMenuItem;
    private MenuItem lookAroundMenuItem;
    private int foodOriginDarkBlue;
    private int foodOriginWhite;
    private ConstraintLayout containerView;


    /**
     * Do a recognition stamp in the bitmap in parameter
     *
     * @param bitmap the stamp image
     */

    /**
     * @param context     the application context
     * @param permissions permissions asked by the application
     * @return true if the user has these permissions false otherwise
     */
    private boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        setContentView(R.layout.activity_main);
        this.containerView = findViewById(R.id.main_container);

        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewPager = findViewById(R.id.pager);
        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        foodOriginDarkBlue = ResourcesCompat.getColor(getResources(), R.color.FoodOriginDarkBlue, null);
        foodOriginWhite = ResourcesCompat.getColor(getResources(), R.color.FoodOriginWhite, null);

        //Detect everything that's potentially suspect and write it in log
        StrictMode.VmPolicy builder = new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(builder);

        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            PermissionsUtils.checkPermission(this, containerView, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, "L'écriture en mémoire est requise pour le chargment des données", Constants.REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE);
        } else {
            launchDownloadWorker();
        }
    }


    private void launchDownloadWorker() {
        if (this.getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Downloading of the lists of CE-approved establishments every 7 days
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();
            final long repeatInterval = 15;
            PeriodicWorkRequest downloadDataGouv =
                    // TODO : change interval to 7 days
                    new PeriodicWorkRequest.Builder(DownloadDataWorker.class, repeatInterval, TimeUnit.MINUTES)
                            .setConstraints(constraints)
                            .setInputData(createInputDataForDownloadWorker())
                            .build();
            WorkManager.getInstance(getApplicationContext())
                    .enqueue(downloadDataGouv);
        }
    }

    private Data createInputDataForDownloadWorker() {
        Data.Builder builder = new Data.Builder();
        builder.putStringArray(Constants.KEY_DATA_GOUV_URLS, Constants.urls_data_gouv_array_2);
        return builder.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        historyMenuItem = menu.findItem(R.id.action_history);
        searchMenuItem = menu.findItem(R.id.action_write_code);
        lookAroundMenuItem = menu.findItem(R.id.action_look_around);
        setFocusOnHistoryItem();
        viewPager.addOnPageChangeListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                setFocusOnHistoryItem();
                viewPager.setCurrentItem(0);

                return true;
            case R.id.action_write_code:
                setFocusOnSearchItem();
                viewPager.setCurrentItem(1);

                return true;
            case R.id.action_look_around:
                setFocusOnLookAroundItem();
                viewPager.setCurrentItem(2);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Ask permissions if it's not already done.
     */
    void askPermissions() {
        if (!hasPermissions(context, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            setFocusOnHistoryItem();
        } else if (position == 1) {
            setFocusOnSearchItem();
        } else if (position == 2) {
            setFocusOnLookAroundItem();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setFocusOnHistoryItem() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            historyMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginWhite, BlendMode.SRC_ATOP));
            searchMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkBlue, BlendMode.SRC_ATOP));
            lookAroundMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkBlue, BlendMode.SRC_ATOP));
        } else {
            historyMenuItem.getIcon().setColorFilter(foodOriginWhite, PorterDuff.Mode.SRC_ATOP);
            searchMenuItem.getIcon().setColorFilter(foodOriginDarkBlue, PorterDuff.Mode.SRC_ATOP);
            lookAroundMenuItem.getIcon().setColorFilter(foodOriginDarkBlue, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void setFocusOnSearchItem() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            historyMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkBlue, BlendMode.SRC_ATOP));
            searchMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginWhite, BlendMode.SRC_ATOP));
            lookAroundMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkBlue, BlendMode.SRC_ATOP));
        } else {
            historyMenuItem.getIcon().setColorFilter(foodOriginDarkBlue, PorterDuff.Mode.SRC_ATOP);
            searchMenuItem.getIcon().setColorFilter(foodOriginWhite, PorterDuff.Mode.SRC_ATOP);
            lookAroundMenuItem.getIcon().setColorFilter(foodOriginDarkBlue, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void setFocusOnLookAroundItem() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            historyMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkBlue, BlendMode.SRC_ATOP));
            searchMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginDarkBlue, BlendMode.SRC_ATOP));
            lookAroundMenuItem.getIcon().setColorFilter(new BlendModeColorFilter(foodOriginWhite, BlendMode.SRC_ATOP));
        } else {
            historyMenuItem.getIcon().setColorFilter(foodOriginDarkBlue, PorterDuff.Mode.SRC_ATOP);
            searchMenuItem.getIcon().setColorFilter(foodOriginDarkBlue, PorterDuff.Mode.SRC_ATOP);
            lookAroundMenuItem.getIcon().setColorFilter(foodOriginWhite, PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchDownloadWorker();
            } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
                PermissionsUtils.displayOptions(this, containerView, "La permission d'accès au stockage est désactivée");
            } else {
                PermissionsUtils.explain(this, containerView, permissions[0], requestCode, "Cette permission est nécessaire pour charger les données");
                Toast.makeText(this, "Write external storage permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
