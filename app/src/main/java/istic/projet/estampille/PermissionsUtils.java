package istic.projet.estampille;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class PermissionsUtils {


    /**
     * Check if the permissions or granted and ask them if it is not.
     *
     * @param activity      activity
     * @param containerView containerView
     * @param permissions   permissions to verify/ask
     * @param rational_text explicative text about the permission
     * @param requestCode   requestCode
     */
    public static void checkPermission(Activity activity, View containerView, String[] permissions, String rational_text, int requestCode) {
        for (String permission : permissions) {
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                explain(activity, containerView, permission, requestCode, rational_text);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission},
                        requestCode);
            }
        }
    }

    /**
     * Check if the permissions or granted and ask them if it is not.
     *
     * @param fragment      fragment
     * @param containerView containerView
     * @param permissions   permissions to verify/ask
     * @param rational_text explicative text about the permission
     * @param requestCode   requestCode
     */
    public static void checkPermission(Fragment fragment, View containerView, String[] permissions, String rational_text, int requestCode) {
        Log.e("permission", "checkpermission f " + Arrays.toString(permissions));
        for (String permission : permissions) {
            if (fragment.getActivity().shouldShowRequestPermissionRationale(permission)) {
                explain(fragment.getActivity(), containerView, permission, requestCode, rational_text);
            } else {
                fragment.requestPermissions(new String[]{permission},
                        requestCode);
            }
        }
    }

    /**
     * Shows a SnackBar in order for the user to go understand why the permission is needed and to re-ask the
     * permission.
     *
     * @param activity      activity
     * @param containerView containerView
     * @param permission    permission
     * @param requestCode   requestCode
     * @param message       a message that explains why the permission is needed
     */
    public static void explain(Activity activity, View containerView, String permission, int requestCode, String message) {
        Snackbar.make(containerView, message, Snackbar.LENGTH_LONG).setAction("Activer", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.requestPermissions(new String[]{permission},
                        requestCode);
            }
        }).show();
    }

    /**
     * Shows a SnackBar in order for the user to go to the parameters
     * and check the permission.
     *
     * @param activity      activity
     * @param containerView containerView
     * @param message       explicative message
     */
    public static void displayOptions(Activity activity, View containerView, String message) {
        Snackbar.make(containerView, message, Snackbar.LENGTH_LONG).setAction("Paramètres", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                final Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        }).show();
    }

}