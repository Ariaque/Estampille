package istic.projet.estampille;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import istic.projet.estampille.models.APIInfosTransformateur;
import istic.projet.estampille.utils.Constants;

public class KnowMoreActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {


    private Context context;
    private APIInfosTransformateur apiInfosTransformateur;
    private String description;
    private String nom;
    private TextView textViewTitle;
    private ImageSlider imageSliderPictures;
    private GradientDrawable insGradientColor;
    private GradientDrawable greyHover;
    private ImageButton fbImageButton;
    private ImageButton insImageButton;
    private ImageButton twImageButton;
    private FragmentManager fm;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_more);
        Intent intent = getIntent();
        fm = this.getSupportFragmentManager();
        fbImageButton = findViewById(R.id.imageButtonFb);
        insImageButton = findViewById(R.id.imageButtonIns);
        twImageButton = findViewById(R.id.imageButtonTw);
        fbImageButton.setOnTouchListener(this);
        insImageButton.setOnTouchListener(this);
        twImageButton.setOnTouchListener(this);
        apiInfosTransformateur = (APIInfosTransformateur) intent.getSerializableExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR);
        fbImageButton.setBackgroundResource(R.color.FacebookBlue);
        insGradientColor = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(this, R.color.InstagramYellow),
                        ContextCompat.getColor(this, R.color.InstagramOrange),
                        ContextCompat.getColor(this, R.color.InstagramPink),
                        ContextCompat.getColor(this, R.color.InstagramPurple),
                        ContextCompat.getColor(this, R.color.InstagramBlue)});
        greyHover = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{ContextCompat.getColor(this, R.color.FoodOriginGrey2),
                        ContextCompat.getColor(this, R.color.FoodOriginGrey2)});

        insImageButton.setBackground(insGradientColor);
        twImageButton.setBackgroundResource(R.color.TwitterBlue);
        if (apiInfosTransformateur != null) {
            description = apiInfosTransformateur.getDescription();
            nom = apiInfosTransformateur.getTransformateur().getRaisonSociale();
            TextView textViewTitle = findViewById(R.id.textViewTitle);
            imageSliderPictures = findViewById(R.id.imageSlider);
            textViewTitle.setText(nom);
            //textViewTitle.setText(Html.fromHtml(getResources().getString(R.string.name, description)));
            List<SlideModel> slideModels = new ArrayList<>();
            imageSliderPictures.setImageList(slideModels, ScaleTypes.CENTER_INSIDE);
        }

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backButton) {
            /*Intent otherActivity = new Intent(getApplicationContext(), DisplayMapActivity.class);
            startActivity(otherActivity);*/
            fm.popBackStack();
            finish();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.imageButtonFb:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ColorDrawable[] colorDrawables = {new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginGrey2)), new ColorDrawable(ContextCompat.getColor(this, R.color.FacebookBlue))};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
                    fbImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(500);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ColorDrawable[] colorDrawables = {new ColorDrawable(ContextCompat.getColor(this, R.color.FacebookBlue)), new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginGrey2))};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
                    fbImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(500);
                }
                break;
            case R.id.imageButtonIns:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    GradientDrawable[] gradientDrawables = {greyHover, insGradientColor};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(gradientDrawables);
                    insImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(500);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    GradientDrawable[] gradientDrawables = {insGradientColor, greyHover};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(gradientDrawables);
                    insImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(500);
                }
                break;
            case R.id.imageButtonTw:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ColorDrawable[] colorDrawables = {new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginGrey2)), new ColorDrawable(ContextCompat.getColor(this, R.color.TwitterBlue))};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
                    twImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(500);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    ColorDrawable[] colorDrawables = {new ColorDrawable(ContextCompat.getColor(this, R.color.TwitterBlue)), new ColorDrawable(ContextCompat.getColor(this, R.color.FoodOriginGrey2))};
                    TransitionDrawable transitionDrawable = new TransitionDrawable(colorDrawables);
                    twImageButton.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(500);
                }
                break;
            default:
                break;
        }
        return false;
    }
}
