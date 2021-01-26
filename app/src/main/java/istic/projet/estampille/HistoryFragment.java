package istic.projet.estampille;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import istic.projet.estampille.models.APITransformateur;
import istic.projet.estampille.utils.AppendableObjectOutputStream;
import istic.projet.estampille.utils.Constants;

/**
 * History page.
 */
public class HistoryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = HistoryFragment.class.getName();
    private static HistoryFragment instance;
    private ListView listView;
    private TextView textEmptyHistory;
    private Button buttonDeleteHistory;
    private ProgressDialog mProgressDialog;

    /**
     * Gets the current instance of {@link HistoryFragment}.
     *
     * @return the current instance of {@link HistoryFragment}.
     */
    public static HistoryFragment getInstance() {
        return instance;
    }

    /**
     * Serialize the searched {@link APITransformateur} in a file.
     *
     * @param activity
     * @param transformateur the {@link APITransformateur} to serialize
     */
    public static void writeSearchInHistory(Activity activity, APITransformateur transformateur) {
        String fileName = Constants.HISTORY_FILE_NAME;
        File file = new File(activity.getFilesDir(), "" + File.separator + fileName);
        boolean append = file.exists();
        FileOutputStream fout;
        ObjectOutputStream oout;
        try {
            fout = activity.getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE | Context.MODE_APPEND);
            // if file exists then append the object, otherwise create new and write headers (see AppendableObjectOutputStream)
            oout = new AppendableObjectOutputStream(fout, append);
            oout.writeObject(transformateur);
            oout.close();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_history, container, false);
        listView = rootView.findViewById(R.id.listView);
        textEmptyHistory = rootView.findViewById(R.id.textEmptyHistory);
        buttonDeleteHistory = rootView.findViewById(R.id.buttonDeleteHistory);
        buttonDeleteHistory.setOnClickListener(this);
        buttonDeleteHistory.setVisibility(View.INVISIBLE);
        instance = this;
        mProgressDialog = new ProgressDialog(getContext(), R.style.FoodOriginAlertDialog);
        mProgressDialog.setMessage(getString(R.string.loading_dialog_message));
        mProgressDialog.setIndeterminate(true);
        return rootView;
    }

    /**
     * Clears the file which contains the search history
     */
    public void clearHistoryFile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setMessage(R.string.dialog_delete_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList<APITransformateur> list = new ArrayList<>();
                        HistoryAdapter adapter = new HistoryAdapter(HistoryFragment.super.getActivity(), HistoryFragment.super.getContext(), list, mProgressDialog);
                        listView.setAdapter(adapter);
                        File file = new File(HistoryFragment.super.getContext().getFilesDir(), "" + File.separator + Constants.HISTORY_FILE_NAME);
                        try {
                            if (file.exists()) {
                                boolean deleted = file.delete();
                                Log.d(TAG, "history file deleted : " + deleted);
                                list.clear();
                            }
                            dialog.dismiss();
                            setHistoryFragmentComponentsVisibility(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Hides or reveals components from history view depending if the history is empty or not.
     *
     * @param historyEmpty true if the history is empty, false otherwise
     */
    public void setHistoryFragmentComponentsVisibility(boolean historyEmpty) {
        if (historyEmpty) {
            textEmptyHistory.setVisibility(View.VISIBLE);
            buttonDeleteHistory.setVisibility(View.INVISIBLE);
        } else {
            textEmptyHistory.setVisibility(View.INVISIBLE);
            buttonDeleteHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonDeleteHistory) {
            clearHistoryFile();
        }
    }
}
