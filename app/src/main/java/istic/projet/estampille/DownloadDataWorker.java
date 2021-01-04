//package istic.projet.estampille;
//
//import android.content.Context;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.util.Objects;
//
//import istic.projet.estampille.utils.Constants;
//
///**
// * Worker that download the list of CE-approved establishments.
// */
//public class DownloadDataWorker extends Worker {
//
//    public DownloadDataWorker(@NonNull Context context,
//                              @NonNull WorkerParameters params) {
//        super(context, params);
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//        Log.d(DownloadDataWorker.class.getName(), "Starting periodic backup job");
//        try {
//            FileWriter fstream = new FileWriter(getApplicationContext().getApplicationContext().getFilesDir().toString()
//                    + "/foodorigin_datagouv.txt");
//            BufferedWriter bufferedWriter = new BufferedWriter(fstream);
//            String[] dataGouvUrlsArray = getInputData().getStringArray(Constants.KEY_DATA_GOUV_URLS);
//            for (int i = 0; i < Objects.requireNonNull(dataGouvUrlsArray).length; i++) {
//                URL url = new URL(dataGouvUrlsArray[i]);
//                BufferedReader bufferedReader = new BufferedReader(
//                        new InputStreamReader(url.openStream()));
//                String aCompanyLine;
//                String header = bufferedReader.readLine();// skipping the header :"Numero de département","Numéro agrément"...
//                while ((aCompanyLine = bufferedReader.readLine()) != null) {
//                    aCompanyLine = aCompanyLine.replace("\",\"", ";");
//                    aCompanyLine = aCompanyLine.replace("\"", "");
//                    if (header.contains("département")) {
//                        String aCompanyLineWODept = aCompanyLine.substring(aCompanyLine.indexOf(";") + 1);
//                        bufferedWriter.write(aCompanyLineWODept);
//                    } else {
//                        bufferedWriter.write(aCompanyLine);
//                    }
//                    bufferedWriter.newLine();
//                }
//                bufferedReader.close();
//                i++;
//            }
//            fstream.close();
//            bufferedWriter.close();
//            return Result.success();
//        } catch (IOException e) {
//            Log.e(DownloadDataWorker.class.getName(), Objects.requireNonNull(e.getMessage()));
//            return Result.retry();
//
//        }
//    }
//
//}
