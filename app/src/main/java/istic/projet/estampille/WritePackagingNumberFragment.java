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

import java.util.Objects;

import istic.projet.estampille.models.APITransformateur;
import istic.projet.estampille.utils.APICalls;
import istic.projet.estampille.utils.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WritePackagingNumberFragment extends Fragment implements View.OnTouchListener, View.OnClickListener, View.OnFocusChangeListener, TextWatcher {

    private TextInputEditText textFieldEstampille1;
    private TextInputEditText textFieldEstampille2;
    private TextInputEditText textFieldEstampille3;
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
        Button searchButton = rootView.findViewById(R.id.button_pckg_nb);
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
        String txt = "";
        //Recover the stamp in the text field
        txt = Objects.requireNonNull(this.textFieldEstampille1.getText()).toString() + "." + Objects.requireNonNull(this.textFieldEstampille2.getText()).toString() + "." + Objects.requireNonNull(this.textFieldEstampille3.getText()).toString();
        // calling the remote API
        APICalls.executeHttpRequestWithRetrofit(this.getActivity(), txt);
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
            if (Objects.requireNonNull(textFieldEstampille2.getText()).length() == 2 && Objects.requireNonNull(textFieldEstampille1.getText()).length() == 3) {
                textFieldEstampille3.requestFocus();
            } else if (textFieldEstampille2.getText().length() == 3 && Objects.requireNonNull(textFieldEstampille1.getText()).length() == 2) {
                textFieldEstampille3.requestFocus();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

