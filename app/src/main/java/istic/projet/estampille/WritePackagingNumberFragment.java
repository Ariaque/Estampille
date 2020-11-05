package istic.projet.estampille;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class WritePackagingNumberFragment extends Fragment implements View.OnTouchListener, View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    private TextInputEditText textFieldEstampille1;
    private TextInputEditText textFieldEstampille2;
    private TextInputEditText textFieldEstampille3;
    private Button searchButton;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_write_packaging_number, container, false);
        context = rootView.getContext();
        textFieldEstampille1 = rootView.findViewById(R.id.tf_estampille_1);
        textFieldEstampille1.setOnFocusChangeListener(this::onFocusChange);
        textFieldEstampille1.addTextChangedListener(this);
        textFieldEstampille2 = rootView.findViewById(R.id.tf_estampille_2);
        textFieldEstampille2.addTextChangedListener(this);
        textFieldEstampille2.setOnFocusChangeListener(this::onFocusChange);
        textFieldEstampille3 = rootView.findViewById(R.id.tf_estampille_3);
        textFieldEstampille3.setOnFocusChangeListener(this::onFocusChange);
        searchButton = rootView.findViewById(R.id.button_pckg_nb);
        searchButton.setOnClickListener(this);
        rootView.setOnTouchListener(this);
        return rootView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        textFieldEstampille1.clearFocus();
        textFieldEstampille2.clearFocus();
        textFieldEstampille3.clearFocus();

        return true;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!textFieldEstampille1.hasFocus() && !textFieldEstampille2.hasFocus() && !textFieldEstampille3.hasFocus()) {
            hideKeyboard(view);
        }
    }

    @Override
    public void onClick(View view) {
        readCsv();
    }

    /**
     * Displays information about the origins of the product.
     */
    private void readCsv() {
        InputStream is = getResources().openRawResource(R.raw.bdd_test);
        boolean find = false;
        String txt = "";

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );

        //Recover the stamp in the text field
        txt = this.textFieldEstampille1.getText().toString() + "." + this.textFieldEstampille2.getText().toString() + "." + this.textFieldEstampille3.getText().toString();

        String line = "";

        try {
            while ((line = reader.readLine()) != null) {
                String[] tab = line.split(";");
                if (txt.equals(tab[1])) {
                    String fileName = "historyFile.txt";
                    try {
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(getActivity().openFileOutput(fileName, Context.MODE_APPEND)));
                        bw.write(tab[0] + ";" + tab[1] + ";" + tab[2] + ";" + tab[3] + ";" + tab[4] + ";" + tab[5] + ";" + tab[6] + "\n");
                        bw.close();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    Intent intent = new Intent(context, DisplayMap.class);
                    Bundle mapBundle = new Bundle();
                    mapBundle.putStringArray("Infos", tab);
                    intent.putExtras(mapBundle);
                    startActivity(intent);
                    find = true;
                }
            }

        } catch (IOException e) {
            Log.wtf("Erreur dans la lecture du CSV " + line, e);
            e.printStackTrace();
        }

        //If the stamp has no similarity in the CSV, a message error appears
        if (!find) {
            Toast.makeText(context, context.getString(R.string.no_match_toast), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Hides the keyboard when focus on the {@link TextInputEditText} is lost.
     *
     * @param view the current {@link View view}
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (textFieldEstampille2.hasFocus()) {
            if (textFieldEstampille2.getText().length() == 2 && textFieldEstampille1.getText().length() == 3) {
                textFieldEstampille3.requestFocus();
            } else if (textFieldEstampille2.getText().length() == 3 && textFieldEstampille1.getText().length() == 2) {
                textFieldEstampille3.requestFocus();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}