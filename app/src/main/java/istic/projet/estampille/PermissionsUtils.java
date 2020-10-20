package istic.projet.estampille;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class PermissionsUtils {


    public static void checkPermission(Activity activity, String permission, String rational_text, int requestCode) {
        if (activity.shouldShowRequestPermissionRationale(permission)) {
            Toast.makeText(activity.getApplicationContext(), rational_text, Toast.LENGTH_SHORT).show();
        } else {
            activity.requestPermissions(new String[]{permission},
                    requestCode);
        }
    }

    public static void checkPermission(Fragment fragment, String permission, String rational_text, int requestCode) {
        if (fragment.getActivity().shouldShowRequestPermissionRationale(permission)) {
            Toast.makeText(fragment.getActivity().getApplicationContext(), rational_text, Toast.LENGTH_SHORT).show();
        } else {
            fragment.requestPermissions(new String[]{permission},
                    requestCode);
        }
    }

//    private void explain()
//    {
//        Snackbar.make(containerView, "Cette permission est nécessaire pour appeler", Snackbar.LENGTH_LONG).setAction("Activer", new OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                askForPermission();
//            }
//        }).show();
//    }


    public static boolean hasPermission(Activity activity, String permission) {
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(Activity activity, String[] permission, int requestCode) {
        activity.requestPermissions(permission, requestCode);
    }

    public static void requestPermissions(Fragment fragment, String[] permission, int requestCode) {
        fragment.requestPermissions(permission, requestCode);
    }

    public static boolean shouldShowRational(Activity activity, String permission) {
        return activity.shouldShowRequestPermissionRationale(permission);
    }

    public static boolean shouldAskForPermission(Activity activity, String permission) {
        return !hasPermission(activity, permission) &&
                (!hasAskedForPermission(activity, permission) ||
                        shouldShowRational(activity, permission));
    }

    public static void goToAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.getPackageName(), null));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static boolean hasAskedForPermission(Activity activity, String permission) {
        return PreferenceManager
                .getDefaultSharedPreferences(activity)
                .getBoolean(permission, false);
    }

    public static void markedPermissionAsAsked(Activity activity, String permission) {
        PreferenceManager
                .getDefaultSharedPreferences(activity)
                .edit()
                .putBoolean(permission, true)
                .apply();
    }

}