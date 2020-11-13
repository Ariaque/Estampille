package istic.projet.estampille;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    private static HistoryFragment instance;
    private ListView listView;
    private TextView textEmptyHistory;
    private Button buttonDeleteHistory;

    /**
     * Gets the current instance of {@link HistoryFragment}.
     *
     * @return the current instance of {@link HistoryFragment}.
     */
    public static HistoryFragment getInstance() {
        return instance;
    }

    static void writeSearchInCSV(Activity activity, String[] tab) {
        String fileName = "historyFile.txt";
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(activity.openFileOutput(fileName, Context.MODE_APPEND)));
            bw.write(tab[0] + ";" + tab[1] + ";" + tab[2] + ";" + tab[3] + ";" + tab[4] + ";" + tab[5] + ";" + tab[6] + "\n");
            bw.close();
        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                        List<String> list = new ArrayList<>();
                        ArrayAdapter adapter = new ArrayAdapter(Objects.requireNonNull(getContext()), R.layout.list_item_layout, list);
                        listView.setAdapter(adapter);
                        File file = new File(Objects.requireNonNull(getActivity()).getFilesDir() + "/historyFile.txt");
                        try {
                            if (file.exists()) {
                                System.out.println("DELETED");
                                PrintWriter writer = new PrintWriter(file);
                                writer.print("");
                                writer.close();
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

    /**
     * Sets the visibility of the tutorial image on the HistoryFragment
     * @param isTutoVisible true if the image must be visible, false otherwise
     */
    /**public void setTutoVisibility(boolean isTutoVisible) {
     ImageView imageView = getView().findViewById(R.id.tuto_image);
     if (isTutoVisible) {
     imageView.setVisibility(View.VISIBLE);
     final ConstraintLayout.LayoutParams layoutparams = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
     layoutparams.setMargins(0, 0, 0, 0);
     imageView.setLayoutParams(layoutparams);
     } else {
     imageView.setVisibility(View.INVISIBLE);
     }
     }*/
}
