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
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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

public class HistoryFragment extends Fragment implements View.OnClickListener {

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





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_history, container, false);
        context = rootView.getContext();
        scanButton = rootView.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(this);
        firstImage = rootView.findViewById(R.id.ocr_image);
        ocrText = rootView.findViewById(R.id.ocr_text);
        checkPermissions();

        return rootView;
    }

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }


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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Call after that user takes a photo
        if (resultCode == Activity.RESULT_OK) {
            Bitmap bmp = null;
            try {
                //Create a bitmap from the stamp image
                InputStream is = context.getContentResolver().openInputStream(photoURI1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                bmp = BitmapFactory.decodeStream(is, null, options);

            } catch (Exception ex) {
                Log.i(getClass().getSimpleName(), ex.getMessage());
                Toast.makeText(context, errorConvert, Toast.LENGTH_SHORT).show();
            }

            //Start the stamp recognition
            firstImage.setImageBitmap(bmp);
            doOCR(bmp);

            OutputStream os;
            try {
                os = new FileOutputStream(photoURI1.getPath());
                if (bmp != null) {
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
                }
                os.flush();
                os.close();
            } catch (Exception ex) {
                Log.e(getClass().getSimpleName(), ex.getMessage());
                Toast.makeText(context, errorFileCreate, Toast.LENGTH_SHORT).show();
            }

        } else {
            photoURI1 = oldPhotoURI;
            firstImage.setImageURI(photoURI1);
        }
    }

    /**
     * @return A file which represent the image of the stamp
     * @throws IOException
     */
    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("MMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onClick(View view) {
        // Check permissions
        if (!flagPermissions) {
            return;
        }

        //Intent to open the camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(context, errorFileCreate, Toast.LENGTH_SHORT).show();
                Log.i("File error", ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                oldPhotoURI = photoURI1;
                photoURI1 = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI1);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE1_CAPTURE);
            }
        }
    }

    /**
     * Do a recognition stamp in the bitmap in parameter
     * @param bitmap the stamp image
     */
    private void doOCR(final Bitmap bitmap) {
        //Open a waiting pop up during the treatment
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(getActivity(), "Processing",
                    "Doing OCR...", true);
        }
        new Thread(new Runnable() {
            public void run() {

                int rotationDegree = 90;
                TextRecognizer recognizer = TextRecognition.getClient();

                for(int i = 0; i < 4; i++){
                    InputImage image = InputImage.fromBitmap(bitmap, rotationDegree * i);

                    final Task<Text> result =
                            recognizer.process(image)
                                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                                        @Override
                                        public void onSuccess(Text visionText) {
                                            List<Text.TextBlock> recognizedText = visionText.getTextBlocks();
                                            extractCode(recognizedText);
                                        }
                                    })
                                    .addOnFailureListener(
                                            new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });
                }

            }
        }).start();
    }

    private void extractCode(List<Text.TextBlock> recognizedText) {
        boolean found = false;
        Text.TextBlock t = null;
        Iterator it = recognizedText.iterator();
        String tempText = null;
        while (!found && it.hasNext()) {
            t = (Text.TextBlock) it.next();
            tempText = t.getText().replace("(", "");
            tempText = tempText.replace(")", "");
            System.out.println(tempText);
            if (tempText.matches("(?s).*[0-9][0-9].[0-9][0-9][0-9].[0-9][0-9][0-9].*") || tempText.matches("(?s).*[0-9][0-9][0-9].[0-9][0-9][0-9].[0-9][0-9][0-9].*") || tempText.matches("(?s).*[0-9](A|B).[0-9][0-9][0-9].[0-9][0-9][0-9].*")) {
                found = true;
            }
        }

        if(!found){
            return;
        }

        tempText = tempText.replace("FR", "");
        tempText = tempText.replace("-", ".");
        tempText = tempText.replace("CE", "");
        tempText = tempText.replace("l", "1");
        tempText = tempText.replace("I", "1");
        tempText = tempText.replace(" ", "");
        tempText = tempText.replace("\n", "");
        ocrText.setText(tempText);
        mProgressDialog.dismiss();
        //Open the activity which permit to search the product origin with a stamp in the text field
        Intent otherActivity = new Intent(getActivity().getApplicationContext(), EcritureEstampille.class);
        otherActivity.putExtra("ocrText", ocrText.getText());
        startActivity(otherActivity);
        getActivity().finish();
    }

    /**
     * Check if the user has all permissions. If the user has all permissions flagPermissions = true
     * otherwise flagPermissions = false and a pop up appears to ask permission
     */
    void checkPermissions() {
        if (!hasPermissions(context, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
            flagPermissions = false;
        }
        flagPermissions = true;

    }
}
