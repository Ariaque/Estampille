package istic.projet.estampille;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import istic.projet.estampille.models.APIInfosTransformateur;
import istic.projet.estampille.utils.Constants;

public class KnowMoreActivity extends AppCompatActivity implements View.OnClickListener {


    private Context context;
    private APIInfosTransformateur apiInfosTransformateur;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_more);
        Intent intent = getIntent();
        apiInfosTransformateur = (APIInfosTransformateur) intent.getSerializableExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR);
        if (apiInfosTransformateur != null) {
            description = apiInfosTransformateur.getDescription();
            TextView textViewTitle = findViewById(R.id.know_more_desc);
            textViewTitle.setText(Html.fromHtml(getResources().getString(R.string.name, description)));
        }

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.backButton) {
            Intent otherActivity = new Intent(getApplicationContext(), DisplayMapActivity.class);
            startActivity(otherActivity);
            finish();
        }
    }
}
