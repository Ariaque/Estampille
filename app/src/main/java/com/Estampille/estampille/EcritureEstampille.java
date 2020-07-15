package com.Estampille.estampille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EcritureEstampille extends AppCompatActivity {

    private EditText ZoneText;
    private Button button;
    private Button buttonRetour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecriture_estampille);
        this.buttonRetour = findViewById(R.id.buttonRetour);
        buttonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(otherActivity);
                finish();
            }
        });

        initActivity();
        createOnClickBtnAjout();
    }
    //Initialisation de l'activity
    private void initActivity(){
        ZoneText = (EditText)findViewById(R.id.ZoneText);
        button = (Button)findViewById(R.id.button);
        createOnClickBtnAjout();
    }

    //Gestion du button pour la recherche de l'estampille
    private void createOnClickBtnAjout(){
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Faire la recherche de l'estampille
                readCsv();
            }
        });
    }
    private List<estampilleCsv> estampilleCsv = new ArrayList<>();
    private void readCsv() {
     InputStream is = getResources().openRawResource(R.raw.bdd);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line = "";
        String leTexte = ZoneText.getText().toString();
        TextView view = (TextView) findViewById(R.id.textView);
        try {
            while ((line = reader.readLine()) != null) {
                //split ';'
                String[] tab = line.split(";");

                //lire les données

                estampilleCsv estampille = new estampilleCsv();
                if(tab[0].length() >0 )
                    estampille.setDep(tab[0]);
                else
                    estampille.setDep("0");
                if(tab[1].length() >0 )
                    estampille.setCode(tab[1]);
                else
                    estampille.setCode("0");
                if(tab[2].length() >0 )
                    estampille.setSiret(Double.parseDouble(tab[2]));
                else
                    estampille.setSiret(Double.parseDouble("0"));
                estampille.setNom(tab[3]);
                estampille.setAdresse(tab[4]);
                estampille.setCodePostal(Integer.parseInt(tab[5]));
                if(tab.length >= 7 &&  tab[6].length() >0 )
                    estampille.setCommune(tab[6]);
                else
                    estampille.setCommune("0");
                if(tab.length >= 8 &&  tab[7].length() >0 )
                    estampille.setCategorie(tab[7]);
                else
                    estampille.setCategorie("0");
                if(tab.length >= 9 &&  tab[8].length() >0 )
                    estampille.setActivite(tab[8]);
                else
                    estampille.setActivite("0");
                if(tab.length >= 10 &&  tab[9].length() >0 )
                    estampille.setEspece(tab[9]);
                else
                    estampille.setEspece("0");

                estampilleCsv.add(estampille);

                if (leTexte.equals(tab[1]))
                   view.setText("L'estampille proviens du département : "+tab[0]+" le code estampille est : "+tab[1]+" le code Siret est le :"+tab[2]+" Le nom de l'entreprise est : "+tab[3]+" l'entreprise ce situe à l'adresse suivante : "+tab[4]+"le code postal"+tab[5]);


                Log.d("Estampille :" ,"Voici les informations de l'estampille" + estampille);
            }
        } catch (IOException e){
            Log.wtf("Erreur  dans la lecture du CSV " + line, e);
            e.printStackTrace();
        }

    }
}