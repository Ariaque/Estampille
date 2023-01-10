package istic.projet.estampille;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import istic.projet.estampille.utils.APICalls;
import istic.projet.estampille.utils.PermissionsUtils;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_IMAGE1_CAPTURE = 1;
    private static final Pattern normalStamp = Pattern.compile("[0-9][0-9][.][0-9][0-9][0-9][.][0-9][0-9][0-9]");
    private static final Pattern domTomStamp = Pattern.compile("[0-9][0-9][0-9][.][0-9][0-9][0-9][.][0-9][0-9][0-9]");
    private static final Pattern corsicaStamp = Pattern.compile("[0-9](A|B)[.][0-9][0-9][0-9][.][0-9][0-9][0-9]");
    protected String mCurrentPhotoPath;
    private ProgressDialog mProgressDialog;
    private Context context;
    private Uri photoURI1;
    private Uri oldPhotoURI;
    private ViewGroup containerView;
    private boolean success;
    private ViewPager viewPager;
    private int OCRcounter = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_home, container, false);
        context = rootView.getContext();
        this.containerView = rootView;
        viewPager = Objects.requireNonNull(getActivity()).findViewById(R.id.pager);
        ImageButton scanTile = rootView.findViewById(R.id.tile_scan);
        ImageButton searchTile = rootView.findViewById(R.id.tile_search);
        ImageButton historyTile = rootView.findViewById(R.id.tile_history);
        ImageButton lookAroundTile = rootView.findViewById(R.id.tile_look_around);
        scanTile.setOnClickListener(this);
        searchTile.setOnClickListener(this);
        historyTile.setOnClickListener(this);
        lookAroundTile.setOnClickListener(this);

        return rootView;
    }

    /**
     * Check is the phone is connected to internet, if it is, the application proceed as usual,
     * if not, a new page is loaded, telling the user that the phone can't access internet.
     *
     * @return true if the phone can access internet, false otherwise.
     */
    private boolean checkInternetConnection() {
        boolean result = true;
        /*ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            Intent intent = new Intent(getActivity(), NoInternetActivity.class);
            startActivity(intent);
            result = false;
        }
        return result;*/

        // Add by Ace
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork()).hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork()).hasTransport(NetworkCapabilities.TRANSPORT_WIFI))) {
            Intent intent = new Intent(getActivity(), NoInternetActivity.class);
            startActivity(intent);
            result = false;
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tile_scan) {
            if (checkInternetConnection()) {
                if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    PermissionsUtils.checkPermission(this, containerView, new String[]{Manifest.permission.CAMERA}, "La caméra est nécessaire pour scanner les estampilles", PermissionsUtils.REQUEST_CODE_PERMISSION_CAMERA);
                } else {
                    openCamera();
                }
            }
        } else if (view.getId() == R.id.tile_search) {
            viewPager.setCurrentItem(1);
        } else if (view.getId() == R.id.tile_history) {
            viewPager.setCurrentItem(2);
        } else if (view.getId() == R.id.tile_look_around) {
            viewPager.setCurrentItem(3);
        }
    }

    /**
     * Opens the camera to scan the stamp.
     */
    private void openCamera() {
        //Intent to open the camera
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Intent chooser = Intent.createChooser(takePictureIntent, "Select camera App:");
            //startActivity(chooser);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Toast.makeText(context, "Impossible de créer le fichier", Toast.LENGTH_SHORT).show();
                    Log.e("File error", Objects.requireNonNull(ex.getMessage()));
                }
                if (photoFile != null) {
                    // Delete the previous photo
                    deletePreviousPhoto();
                    // Store the new photo URI
                    photoURI1 = FileProvider.getUriForFile(context, "istic.projet.estampille.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI1);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE1_CAPTURE);
                }
            }
        }

    private void deletePreviousPhoto() {
            if (oldPhotoURI != null) {
                File file = new File(oldPhotoURI.getPath());
                if (file.exists()) {
                    file.delete();
                }
                oldPhotoURI = null;
            }
    }

    /**
     * Creates the image file.
     *
     * @return A file which represent the image of the stamp
     * @throws IOException Throws an {@link IOException} if something goes wrong in the file creation process.
     */
    public File createImageFile() throws IOException {
        ///////////////////////////////////////////////////////// Add by ace
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMdd_HHmmss");
        StringBuilder imageFileNameBuilder = new StringBuilder("JPEG_");
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = dateFormat.format(new Date());
        imageFileNameBuilder.append(timeStamp).append("_");
        String imageFileName = imageFileNameBuilder.toString();
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        String mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Calls after that user takes a photo
        if (requestCode == REQUEST_IMAGE1_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap bmp = null;
            try {
                //Creates a bitmap from the stamp image
                InputStream is = context.getContentResolver().openInputStream(photoURI1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                bmp = BitmapFactory.decodeStream(is, null, options);
                is.close();

            } catch (Exception ex) {
                Log.i(getClass().getSimpleName(), Objects.requireNonNull(ex.getMessage()));
                Toast.makeText(context, R.string.conversion_fail_toast, Toast.LENGTH_SHORT).show();
            }
            //Starts the stamp recognition
            doOCR(bmp);

            OutputStream os;
            try {
                // Ouvrez un flux d'écriture sur le fichier à l'aide du content resolver et du fileUri
                os = context.getContentResolver().openOutputStream(photoURI1);

                if (bmp != null) {
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
                }
                os.flush();
                os.close();
            } catch (Exception ex) {
                Log.e(getClass().getSimpleName(), Objects.requireNonNull(ex.getMessage()));
                Toast.makeText(context, R.string.file_creation_fail_toast, Toast.LENGTH_SHORT).show();
            }
            /*try {
                os = new FileOutputStream(photoURI1.getPath());
                if (bmp != null) {
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
                }
                os.flush();
                os.close();
            } catch (Exception ex) {
                Log.e(getClass().getSimpleName(), Objects.requireNonNull(ex.getMessage()));
                Toast.makeText(context, R.string.file_creation_fail_toast, Toast.LENGTH_SHORT).show();
            }*/
        }
        else {
            photoURI1 = oldPhotoURI;
        }
    }

    /**
     * Recognizes the text from the bitmap in parameter.
     *
     * @param bitmap the stamp image
     */
    private void doOCR(final Bitmap bitmap) {
        //Open a waiting pop up during the treatment
        OCRcounter = 0;
        mProgressDialog = new ProgressDialog(getActivity(), R.style.FoodOriginAlertDialog);
        mProgressDialog.setMessage(getString(R.string.loading_dialog_message));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
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

    /**
     * Sets the ocr in the text input inside {@link WritePackagingNumberFragment} if the recognition has been successful.
     *
     * @param ocrSuccess indicates if the recognition has been successful (true) or not (false).
     */
    private void imageResult(boolean ocrSuccess) {
        if (!ocrSuccess) {
            OCRcounter++;
            if (OCRcounter == 4) {
                viewPager.setCurrentItem(1);
                Toast.makeText(context, R.string.recognition_fail_toast, Toast.LENGTH_SHORT).show();
                mProgressDialog.hide();
            }
        }
    }

    /**
     * Extracts the packaging code from the text recognized on the picture.
     *
     * @param recognizedText recognized text
     * @return true if a character chain matching with the packaging number regex has been found, false otherwise
     */
    private boolean extractCode(List<Text.TextBlock> recognizedText) {
        boolean found = false;
        Text.TextBlock t;
        Iterator it = recognizedText.iterator();
        String tempText = null;
        //Finds the group of text containing the string
        while (!found && it.hasNext()) {
            t = (Text.TextBlock) it.next();
            tempText = t.getText().replace("(", "");
            tempText = tempText.replace(")", "");
            if (tempText.matches("(?s).*[0-9][0-9][.][0-9][0-9][0-9][.][0-9][0-9][0-9].*") || tempText.matches("(?s).*[0-9][0-9][0-9][.][0-9][0-9][0-9][.][0-9][0-9][0-9].*") || tempText.matches("(?s).*[0-9](A|B)[.][0-9][0-9][0-9][.][0-9][0-9][0-9].*")) {
                found = true;
            }
        }
        if (found) {
            //Removes spare characters from recognized string
            Matcher normalMatcher = normalStamp.matcher(tempText);
            Matcher domTomMatcher = domTomStamp.matcher(tempText);
            Matcher corsicaMatcher = corsicaStamp.matcher(tempText);
            if (normalMatcher.find()) {
                this.searchStampInDB(normalMatcher.group(0));
                //editText.setText(normalMatcher.group(0));
            } else if (domTomMatcher.find()) {
                this.searchStampInDB(domTomMatcher.group(0));
                //editText.setText(domTomMatcher.group(0));
            } else if (corsicaMatcher.find()) {
                this.searchStampInDB(corsicaMatcher.group(0));
                //editText.setText(corsicaMatcher.group(0));
            }
        }
        return found;
    }

    /**
     * Launch the search of the stamp in the remote database.
     *
     * @param estampilleSearched stamp's searched
     */
    private void searchStampInDB(String estampilleSearched) {
        mProgressDialog.show();
        APICalls.searchStampInRemoteAPI(this.getActivity(), estampilleSearched, mProgressDialog);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionsUtils.REQUEST_CODE_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && permissions.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else if (!shouldShowRequestPermissionRationale(permissions[0])) {
                    PermissionsUtils.displayOptions(this.getActivity(), containerView, PermissionsUtils.permission_camera_params);
                } else {
                    PermissionsUtils.explain(this.getActivity(), containerView, permissions[0], requestCode, PermissionsUtils.permission_camera_explain);
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
