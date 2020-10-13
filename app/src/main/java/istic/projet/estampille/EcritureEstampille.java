package istic.projet.estampille;

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

/**
 * This activity matches with the screen which permit to know the origins of the product
 */
public class EcritureEstampille extends AppCompatActivity {

    private EditText zoneText;
    private Button button;
    private Button buttonRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Display the screen which permit to type the stamp
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estampille);

        //Retrieve design elements
        this.buttonRetour = findViewById(R.id.buttonRetour);
        this.button = findViewById(R.id.button);
        this.zoneText = findViewById(R.id.ZoneText);

        //Add listener that allows to return to the home page
        buttonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent otherActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(otherActivity);
                finish();
            }
        });

        //Add listener that allows to do the search of a stamp
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readCsv();
            }
        });

        //
        Intent intent = getIntent();
        if(intent.hasExtra("ocrText")) {
            String ocrText = intent.getStringExtra("ocrText");
            this.zoneText.setText(ocrText);
        }
    }

    /**
     * Display information about the origins of the product
     */
    private void readCsv() {
        InputStream is = getResources().openRawResource(R.raw.bdd);
        boolean etat = false;
        String txt ="";

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        //Recover the stamp in the text field
        txt = this.zoneText.getText().toString();
        txt.replace("FR", "");
        txt.replace("-", ".");
        txt.replace("CE", "");

        //Recover all text view used to display the origins of the product
        TextView view = (TextView) findViewById(R.id.textView);
        TextView view2 = (TextView) findViewById(R.id.textView2);
        TextView view3 = (TextView) findViewById(R.id.textView3);
        TextView view4 = (TextView) findViewById(R.id.textView4);
        TextView view5 = (TextView) findViewById(R.id.textView5);
        TextView view6 = (TextView) findViewById(R.id.textView6);
        TextView view7 = (TextView) findViewById(R.id.textView7);

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                String[] tab = line.split(";");

                if (txt.equals(tab[1])) {
                    view.setText("Nom de l'entreprise : " + tab[3]);
                    view2.setText("Le département où se situe l'entreprise est  : " + tab[0]);
                    view3.setText("L'adresse entreprise: " + tab[4]);
                    view4.setText("le Code postale est : " + tab[5]);
                    view5.setText("Nom de la ville : " + tab[6]);
                    view6.setText("Numero Siret : " + tab[2]);
                    view7.setText("Code Estampille: " + tab[1]);
                    etat = true;
                }

                // Create an object for each csv file
               /* EstampilleCsv estampille = new EstampilleCsv();
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
                    estampille.setEspece("0");*/
            }

        } catch (IOException e){
            Log.wtf("Erreur dans la lecture du CSV " + line, e);
            e.printStackTrace();
        }

        //If the stamp has no similarity in the CSV, the error page appears
        if (etat == false) {
            Intent otherActivity = new Intent(getApplicationContext(), PageErreur.class);
            startActivity(otherActivity);
            finish();
        }
    }
}
