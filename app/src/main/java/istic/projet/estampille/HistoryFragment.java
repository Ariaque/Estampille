package istic.projet.estampille;

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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    private static HistoryFragment instance;
    private Context context;
    private ViewGroup containerView;
    private ViewPager viewPager;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_history, container, false);
        context = rootView.getContext();
        listView = rootView.findViewById(R.id.listView);
        textEmptyHistory = rootView.findViewById(R.id.textEmptyHistory);
        buttonDeleteHistory = rootView.findViewById(R.id.buttonDeleteHistory);
        buttonDeleteHistory.setOnClickListener(this);
        buttonDeleteHistory.setVisibility(View.INVISIBLE);


        this.containerView = rootView;
        viewPager = getActivity().findViewById(R.id.pager);
        instance = this;

        return rootView;
    }

    /**
     * Clears the file which contains the search history
     */
    public void clearFile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_delete_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        List<String> list = new ArrayList<>();
                        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.list_item_layout, list);
                        listView.setAdapter(adapter);
                        File file = new File(getActivity().getFilesDir() + "/historyFile.txt");
                        try {
                            if (file.exists()) {
                                System.out.println("DELETED");
                                PrintWriter writer = new PrintWriter(file);
                                writer.print("");
                                writer.close();
                            }
                            dialog.dismiss();
                            //setTutoVisibility(true);
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
     * @param historyEmpty true if the history is empty, false otherwise
     */
    public void setHistoryFragmentComponentsVisibility(boolean historyEmpty) {
        if(historyEmpty) {
            textEmptyHistory.setVisibility(View.VISIBLE);
            buttonDeleteHistory.setVisibility(View.INVISIBLE);
        }
        else {
            textEmptyHistory.setVisibility(View.INVISIBLE);
            buttonDeleteHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.buttonDeleteHistory) {
            clearFile();
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
