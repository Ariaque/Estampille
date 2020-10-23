package istic.projet.estampille;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private Context context;
    private Toolbar mToolBar;
    private Fragment historyFragment;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ImageButton deleteButton;
    private ViewPager viewPager;
    private MenuItem historyMenuItem;
    private MenuItem searchMenuItem;
    private MenuItem lookAroundMenuItem;
    private int foodOriginDarkBlue;
    private int foodOriginWhite;
    private ConstraintLayout containerView;
    private ArrayList<Map<String, String>> list;
    private Set<String> setH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        this.containerView = findViewById(R.id.main_container);

        //Checks permission
        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            PermissionsUtils.checkPermission(this, containerView, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionsUtils.permission_storage_explain, PermissionsUtils.REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE);
        } else {
            launchDownloadWorker();
        }

        //Configures design elements
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewPager = findViewById(R.id.pager);
        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        foodOriginDarkBlue = ResourcesCompat.getColor(getResources(), R.color.FoodOriginDarkOrange, null);
        foodOriginWhite = ResourcesCompat.getColor(getResources(), R.color.FoodOriginWhite, null);

        //Detects everything that's potentially suspect and write it in log
        StrictMode.VmPolicy builder = new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(builder);

    }

    /**
     * Downloading of the lists of CE-approved establishments every 7 days.
     */
    private void launchDownloadWorker() {
        if (this.getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();
            final long repeatInterval = 7;
            PeriodicWorkRequest downloadDataGouv =
                    new PeriodicWorkRequest.Builder(DownloadDataWorker.class, repeatInterval, TimeUnit.DAYS)
                            .setConstraints(constraints)
                            .setInputData(createInputDataForDownloadWorker())
                            .build();
            WorkManager.getInstance(getApplicationContext())
                    .enqueue(downloadDataGouv);
        }
    }

    /**
     * Gives the urls that allows to access the lists of CE-approved establishments
     *
     * @return the input
     */
    private Data createInputDataForDownloadWorker() {
        Data.Builder builder = new Data.Builder();
        builder.putStringArray(Constants.KEY_DATA_GOUV_URLS, Constants.urls_data_gouv_array_2);
        return builder.build();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.deleteButton) {
            HistoryFragment.getInstance().clearFile();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        historyMenuItem = menu.findItem(R.id.action_history);
        searchMenuItem = menu.findItem(R.id.action_write_code);
        lookAroundMenuItem = menu.findItem(R.id.action_look_around);
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);
        try {
            setFocusOnHistoryItem();
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewPager.addOnPageChangeListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                try {
                    setFocusOnHistoryItem();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            try {
                setFocusOnHistoryItem();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (position == 1) {
            setFocusOnSearchItem();
        } else if (position == 2) {
            setFocusOnLookAroundItem();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Sets the focus on the "history" (first from left) menu item. Displays the button to delete the history.
     *
     * @throws IOException throws an {@link IOException} if something goes wrong
     */
    private void setFocusOnHistoryItem() throws IOException {
        this.readFile();
        deleteButton.setVisibility(View.VISIBLE);
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

    /**
     * Sets the focus on the "search" (second from left) menu item. Displays the button to delete the history.
     */
    private void setFocusOnSearchItem() {
        deleteButton.setVisibility(View.INVISIBLE);
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

    /**
     * Sets the focus on the "look around" (third from left) menu item. Displays the button to delete the history.
     */
    private void setFocusOnLookAroundItem() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionsUtils.checkPermission(this, containerView, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PermissionsUtils.permission_geoloc_explain, PermissionsUtils.REQUEST_CODE_LOCATION);
        }
        deleteButton.setVisibility(View.INVISIBLE);
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

    /**
     * Reads the history file content and displays the history in the main page
     */
    public void readFile() {
        String fileName = "historyFile.txt";
        ListView listView = findViewById(R.id.listView);
        list = new ArrayList<>();
        setH = new LinkedHashSet<>();

        //Reads the file
        try {
            InputStreamReader inputReader = new InputStreamReader(openFileInput(fileName));
            BufferedReader br = new BufferedReader(inputReader);
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = br.readLine()) != null) {
                Map<String, String> data = new HashMap<>();
                buffer.append(line).append("\n");
                String[] infos = line.split(";");
                data.put("estampille",infos[1]);
                data.put("transformateur", infos[3]);
                setH.add(line);
                list.add(data);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            HistoryFragment.getInstance().setTutoVisibility(true);
        }

        //Displays the tutorial image if there are no history
        ImageView imageView = findViewById(R.id.tuto_image);
        if(list.size() == 0)
        {
            HistoryFragment.getInstance().setTutoVisibility(true);
        }
        else{
            HistoryFragment.getInstance().setTutoVisibility(false);
        }

        //Deletes duplicates line
        Set<Map<String, String>> mySet = new LinkedHashSet<>(list);
        list = new ArrayList<>(mySet);

        //Changes the adapter list
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.list_item_layout, new String[]{"transformateur", "estampille"}, new int[]{R.id.item1, R.id.item2});
        listView.setAdapter(adapter);

        //Adds listener for each list item : go to the information page of a transformer
        Object[] tab = setH.toArray();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String infos = (String)tab[i];
                Intent intent = new Intent(context, DisplayMap.class);
                Bundle mapBundle = new Bundle();
                mapBundle.putStringArray("Infos", infos.split(";"));
                intent.putExtras(mapBundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionsUtils.REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchDownloadWorker();
            } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
                PermissionsUtils.displayOptions(this, containerView, PermissionsUtils.permission_storage_params);
            } else {
                PermissionsUtils.explain(this, containerView, permissions[0], requestCode, PermissionsUtils.permission_storage_explain);
            }
        } else if (requestCode == PermissionsUtils.REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && permissions.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Fragment lookAroundFragment = (LookAroundFragment) fragmentPagerAdapter.instantiateItem(viewPager, 2);
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

}