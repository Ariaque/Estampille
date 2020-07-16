package xyz.net7.textsave;



import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        setContentView(R.layout.activity_estampille);
        this.buttonRetour = findViewById(R.id.buttonRetour);
        this.button = findViewById(R.id.button);
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
        //createOnClickBtnAjout();
    }

    //Gestion du button pour la recherche de l'estampille
    private void createOnClickBtnAjout(){
        button.setOnClickListener(new View.OnClickListener() {
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
        String ocrText = "a";
        Boolean etat = false;
        Intent intent =getIntent();
        if(intent.hasExtra("ocrText"))
        ocrText = intent.getStringExtra("ocrText");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line = "";
        String leTexte = ZoneText.getText().toString();
        TextView view = (TextView) findViewById(R.id.textView);
        TextView view2 = (TextView) findViewById(R.id.textView2);
        TextView view3 = (TextView) findViewById(R.id.textView3);
        TextView view4 = (TextView) findViewById(R.id.textView4);
        TextView view5 = (TextView) findViewById(R.id.textView5);
        TextView view6 = (TextView) findViewById(R.id.textView6);
        TextView view7 = (TextView) findViewById(R.id.textView7);


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

                if (leTexte.equals(tab[1])) {
                    view.setText("Nom de l'entreprise : " + tab[3]);
                    view2.setText("Le département ou ce situe l'entreprise est  : " + tab[0]);
                    view3.setText("L'entreprise ce situe  à l'adresse suivante : " + tab[4]);
                    view4.setText("le Code postale est : " + tab[5]);
                    view5.setText("Nom de la ville : " + tab[6]);
                    view6.setText("Numero Siret : " + tab[2]);
                    view7.setText("Code Estampille: " + tab[1]);
                    etat = true;
                }
                Log.d("Estampille :" ,"Voici les informations de l'estampille" + estampille);
            }

        } catch (IOException e){
            Log.wtf("Erreur  dans la lecture du CSV " + line, e);
            e.printStackTrace();
        }
        if (etat == false) {
            Intent otherActivity = new Intent(getApplicationContext(), PageErreur.class);
            startActivity(otherActivity);
            finish();
        }

        Log.d("Test : ", "AFFICHE DE LA VARIABLE SRCTEST"+ ocrText);
    }
}
