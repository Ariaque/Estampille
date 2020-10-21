package istic.projet.estampille;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_IMAGE1_CAPTURE = 1;
    protected String mCurrentPhotoPath;
    private ProgressDialog mProgressDialog;
    private Context context;
    private Uri photoURI1;
    private Uri oldPhotoURI;
    private FloatingActionButton scanButton;
    private ViewGroup containerView;
    private boolean success;
    private ViewPager viewPager;
    private int OCRcounter = 0;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_history, container, false);
        context = rootView.getContext();
        scanButton = rootView.findViewById(R.id.scan_button);
        listView = rootView.findViewById(R.id.listView);
        scanButton.setOnClickListener(this);
        this.containerView = rootView;
        viewPager = getActivity().findViewById(R.id.pager);

        return rootView;
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
                is.close();

            } catch (Exception ex) {
                Log.i(getClass().getSimpleName(), ex.getMessage());
                Toast.makeText(context, R.string.conversion_fail_toast, Toast.LENGTH_SHORT).show();
            }
            //Start the stamp recognition
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
                Toast.makeText(context, R.string.file_creation_fail_toast, Toast.LENGTH_SHORT).show();
            }

        } else {
            photoURI1 = oldPhotoURI;
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
        if (this.getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            PermissionsUtils.checkPermission(this, containerView, new String[]{Manifest.permission.CAMERA}, "la caméra est nécessaire pour scanner les estmapilles", Constants.REQUEST_CODE_PERMISSION_CAMERA);
        } else {
            openCamera();
        }
    }

    /**
     * Open camera.
     */
    public void openCamera() {
        //Intent to open the camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(context, R.string.file_creation_fail_toast, Toast.LENGTH_SHORT).show();
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
     *
     * @param bitmap the stamp image
     */
    private void doOCR(final Bitmap bitmap) {
        //Open a waiting pop up during the treatment
        OCRcounter = 0;
        mProgressDialog = ProgressDialog.show(getActivity(), "Processing",
                "Doing OCR...", true);
        success = false;
        int rotationDegree = 90;
        TextRecognizer recognizer = TextRecognition.getClient();
        for (int i = 0; i < 4; i++) {
            InputImage image = InputImage.fromBitmap(bitmap, rotationDegree * i);
            final Task<Text> result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    List<Text.TextBlock> recognizedText = visionText.getTextBlocks();
                                    success = extractCode(recognizedText);
                                    mProgressDialog.cancel();
                                    imageResult(success);
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

    private void imageResult(boolean ocrSuccess) {
        if (ocrSuccess) {
            viewPager.setCurrentItem(1);
        } else {
            OCRcounter++;
            if (OCRcounter == 4) {
                Toast.makeText(context, R.string.recognition_fail_toast, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean extractCode(List<Text.TextBlock> recognizedText) {
        boolean found = false;
        Text.TextBlock t = null;
        Iterator it = recognizedText.iterator();
        String tempText = null;
        while (!found && it.hasNext()) {
            t = (Text.TextBlock) it.next();
            tempText = t.getText().replace("(", "");
            tempText = tempText.replace(")", "");
            if (tempText.matches("(?s).*[0-9][0-9].[0-9][0-9][0-9].[0-9][0-9][0-9].*") || tempText.matches("(?s).*[0-9][0-9][0-9].[0-9][0-9][0-9].[0-9][0-9][0-9].*") || tempText.matches("(?s).*[0-9](A|B).[0-9][0-9][0-9].[0-9][0-9][0-9].*")) {
                found = true;
            }
        }
        if (found) {
            tempText = tempText.replace("FR", "");
            tempText = tempText.replace("-", ".");
            tempText = tempText.replace("CE", "");
            tempText = tempText.replace("l", "1");
            tempText = tempText.replace("I", "1");
            tempText = tempText.replace(" ", "");
            tempText = tempText.replace("\n", "");
            TextInputEditText editText = getActivity().findViewById(R.id.tf_estampille);
            editText.setText(tempText);
            //Open the activity which permit to search the product origin with a stamp in the text field
            /*Intent otherActivity = new Intent(getActivity().getApplicationContext(), EcritureEstampille.class);
            otherActivity.putExtra("ocrText", tempText);
            startActivity(otherActivity);
            getActivity().finish();*/
        }
        return found;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
                PermissionsUtils.displayOptions(this.getActivity(), containerView, "La permission d'accès à la caméra est désactivée");
            } else {
                PermissionsUtils.explain(this.getActivity(), containerView, permissions[0], requestCode, "Cette permission est nécessaire pour scanner les estampilles");
                Toast.makeText(this.getActivity(), "Write external storage permission was not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Clear the file which contains the search history
     */
    private void clearFile() {
        List<String> list = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.list_item_layout, list);
        listView.setAdapter(adapter);

        File file = new File(getActivity().getFilesDir() + "/historyFile.txt");
        try {
            if (file.exists()) {
                PrintWriter writer = new PrintWriter(file);
                writer.print("");
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
