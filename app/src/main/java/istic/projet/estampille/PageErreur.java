package istic.projet.estampille;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class PageErreur extends AppCompatActivity {

    private Button buttonRetourErreur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_erreur);

        this.buttonRetourErreur =findViewById(R.id.buttonRetourErreur);

        buttonRetourErreur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(),EcritureEstampille.class);
                startActivity(otherActivity);
                finish();
            }
        });
    }



}
