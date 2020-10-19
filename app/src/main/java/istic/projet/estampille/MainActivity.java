package istic.projet.estampille;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String errorFileCreate = "Error file create!";
    private static final String errorConvert = "Error convert!";
    private static final int REQUEST_IMAGE1_CAPTURE = 1;
    protected String mCurrentPhotoPath;
    ImageView firstImage;
    TextView ocrText;
    int PERMISSION_ALL = 1;
    boolean flagPermissions = false;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
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


    /**
     * Do a recognition stamp in the bitmap in parameter
     *
     * @param bitmap the stamp image
     */
    /**
     * @param context the application context
     * @param permissions permissions asked by the application
     * @return true if the user has these permissions false otherwise
     */
    private static boolean hasPermissions(Context context, String... permissions) {
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

        //Add listener to button which allows to type a stamp
        /*this.ecrire = findViewById(R.id.action_write_code);
        ecrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(), EcritureEstampille.class);
                startActivity(otherActivity);
                finish();
            }
        });*/
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewPager = findViewById(R.id.pager);
        fragmentPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(fragmentPagerAdapter);


        //Detect everything that's potentially suspect and write it in log
        StrictMode.VmPolicy builder = new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(builder);

        //Check permission to create the OCR access
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                return true;
            case R.id.action_write_code:
                Intent otherActivity = new Intent(getApplicationContext(), EcritureEstampille.class);
                startActivity(otherActivity);
                finish();
            case R.id.action_look_around:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
