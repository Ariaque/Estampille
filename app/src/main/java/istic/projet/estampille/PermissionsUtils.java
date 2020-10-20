package istic.projet.estampille;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class PermissionsUtils {


    public static void checkPermission(Activity activity, View containerView, String[] permissions, String rational_text, int requestCode) {
        for (int i = 0; i < permissions.length; i++) {
            if (activity.shouldShowRequestPermissionRationale(permissions[i])) {
                explain(activity, containerView, permissions[i], requestCode, "Cette permission est nécessaire pour avoir les données");
                Toast.makeText(activity.getApplicationContext(), rational_text, Toast.LENGTH_SHORT).show();
            } else {
                activity.requestPermissions(new String[]{permissions[i]},
                        requestCode);
            }
        }
    }

    public static void checkPermission(Fragment fragment, View containerView, String[] permissions, String rational_text, int requestCode) {
        for (String permission : permissions) {
            if (fragment.getActivity().shouldShowRequestPermissionRationale(permission)) {
                explain(fragment.getActivity(), containerView, permission, requestCode, "Cette permission est nécessaire pour avoir les données");
                Toast.makeText(fragment.getActivity().getApplicationContext(), rational_text, Toast.LENGTH_SHORT).show();
            } else {
                fragment.requestPermissions(new String[]{permission},
                        requestCode);
            }
        }
    }

    public static void explain(Activity activity, View containerView, String permission, int requestCode, String message) {
        Snackbar.make(containerView, message, Snackbar.LENGTH_LONG).setAction("Activer", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.requestPermissions(new String[]{permission},
                        requestCode);
            }
        }).show();
    }

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