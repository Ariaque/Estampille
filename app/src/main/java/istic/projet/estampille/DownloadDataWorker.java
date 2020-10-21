package istic.projet.estampille;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class DownloadDataWorker extends Worker {

    public DownloadDataWorker(@NonNull Context context,
                              @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
//        check
//        if (this.getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                Toast.makeText(this.getApplicationContext(), "Write extenral storage permission needed", Toast.LENGTH_SHORT).show();
//            } else {
//                ActivityCompat.requestPermissions(MainActivity.class, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
//            }
//        }

        Log.d(DownloadDataWorker.class.getName(), "Starting periodic backup job");
        try {
            FileWriter fstream = new FileWriter(Environment
                    .getExternalStorageDirectory().toString()
                    + "/data/foodorigin_datagouv.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fstream);
            String[] dataGouvUrlsArray = getInputData().getStringArray(Constants.KEY_DATA_GOUV_URLS);
            for (int i = 0; i < Objects.requireNonNull(dataGouvUrlsArray).length; i++) {
                URL url = new URL(dataGouvUrlsArray[i]);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String aCompanyLine;
                String header = bufferedReader.readLine();// skipping the header :"Numero de département","Numéro agrément"...
                while ((aCompanyLine = bufferedReader.readLine()) != null) {
                    aCompanyLine = aCompanyLine.replace("\",\"", ";");
                    aCompanyLine = aCompanyLine.replace("\"", "");
                    if (header.contains("département")) {
                        String aCompanyLineWODept = aCompanyLine.substring(aCompanyLine.indexOf(";") + 1);
                        bufferedWriter.write(aCompanyLineWODept);
                    } else {
                        bufferedWriter.write(aCompanyLine);
                    }
                    bufferedWriter.newLine();
                }
                bufferedReader.close();
                i++;
            }
            bufferedWriter.close();
            fstream.close();
            return Result.success();
        } catch (IOException e) {
            Log.e(DownloadDataWorker.class.getName(), e.getMessage());
            return Result.retry();

        }
    }

}
