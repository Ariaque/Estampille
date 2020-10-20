//package istic.projet.estampille;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.os.Build;
//import android.os.Environment;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.core.app.NotificationCompat;
//import androidx.work.Data;
//import androidx.work.ForegroundInfo;
//import androidx.work.WorkManager;
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
//public class DownloadDataWorker2 extends Worker {
//    private static final String KEY_INPUT_URL = "KEY_INPUT_URL";
//    private static final String KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME";
//
//    private NotificationManager notificationManager;
//
//    public DownloadDataWorker2(
//            @NonNull Context context,
//            @NonNull WorkerParameters parameters) {
//        super(context, parameters);
//        notificationManager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);
//    }
//
//    @NonNull
//    @Override
//    public Result doWork() {
//        Data inputData = getInputData();
//        String inputUrl = inputData.getString(KEY_INPUT_URL);
//        String outputFile = inputData.getString(KEY_OUTPUT_FILE_NAME);
//        // Mark the Worker as important
//        String progress = "Starting Download";
//        setForegroundAsync(createForegroundInfo(progress));
//        download(inputUrl, outputFile);
//        return Result.success();
//    }
//
//    private Result download(String inputUrl, String outputFile) {
//        // Downloads a file and updates bytes read
//        // Calls setForegroundInfoAsync() periodically when it needs to update
//        // the ongoing Notification
//        Log.d(DownloadDataWorker.class.getName(), "Starting periodic backup job");
//        try {
//            FileWriter fstream = new FileWriter(Environment
//                    .getExternalStorageDirectory().toString()
//                    + "/data/foodorigin_datagouv.txt");
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
//                setForegroundInfoAsync()
//            }
//            bufferedWriter.close();
//            fstream.close();
//            return Result.success();
//        } catch (IOException e) {
//            Log.e(DownloadDataWorker.class.getName(), e.getMessage());
//            return Result.retry();
//
//        }
//    }
//
//    @NonNull
//    private ForegroundInfo createForegroundInfo(@NonNull String progress) {
//        // Build a notification using bytesRead and contentLength
//
//        Context context = getApplicationContext();
//        String id = "notification_channel_id";
//        String title = "notification_title";
//        String cancel = "cancel_download";
//        // This PendingIntent can be used to cancel the worker
//        PendingIntent intent = WorkManager.getInstance(context)
//                .createCancelPendingIntent(getId());
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createChannel();
//        }
//
//        Notification notification = new NotificationCompat.Builder(context, id)
//                .setContentTitle(title)
//                .setTicker(title)
//                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
//                .setOngoing(true)
//                .addAction(android.R.drawable.ic_delete, cancel, intent)
//                .build();
//
//        return new ForegroundInfo(1, notification);
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private void createChannel() {
//        // Create a Notification channel
//    }
//
//}
