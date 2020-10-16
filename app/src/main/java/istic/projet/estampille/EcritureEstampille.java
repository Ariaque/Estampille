package istic.projet.estampille;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
                Intent otherActivity = new Intent(getApplicationContext(), MainActivity.class);
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

        //If a scan is done, the scan result is put in the text field
        Intent intent = getIntent();
        if (intent.hasExtra("ocrText")) {
            String ocrText = intent.getStringExtra("ocrText");
            this.zoneText.setText(ocrText);
        }
    }

    /**
     * Display information about the origins of the product
     */
    private void readCsv() {
//        InputStream is = getResources().openRawResource(R.raw.bdd);
        InputStream is = null;
        try {
            is = new FileInputStream(new File(Environment
                    .getExternalStorageDirectory().toString()
                    + "/data/foodorigin_datagouv.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean found = false;
        String productEstampille = "";
        if (is != null) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            );

        //Recover the stamp in the text field
            productEstampille = this.zoneText.getText().toString();
        /*productEstampille.replace("FR", " ");
        productEstampille.replace("-", ".");
        productEstampille.replace("CE", " "); --> No consequence */

            //Recover all text view used to display the origins of the product
            TextView view = findViewById(R.id.textView);
            TextView view2 = findViewById(R.id.textView2);
            TextView view3 = findViewById(R.id.textView3);
            TextView view4 = findViewById(R.id.textView4);
            TextView view5 = findViewById(R.id.textView5);
            TextView view6 = findViewById(R.id.textView6);
            TextView view7 = findViewById(R.id.textView7);

            String line = "";
            int estampIndex = 0;
            try {
                while ((line = reader.readLine()) != null && !found) {
                    String[] tab = line.split(";");
                    if (productEstampille.equals(tab[estampIndex])) {
                        view.setText(getString(R.string.company_name, tab[estampIndex + 2]));
                        view2.setText(getString(R.string.company_dept, tab[estampIndex].substring(0, tab[estampIndex].indexOf("."))));
                        view3.setText(getString(R.string.company_address, tab[estampIndex + 3]));
                        view4.setText(getString(R.string.company_cp, tab[estampIndex + 4]));
                        view5.setText(getString(R.string.company_city, tab[estampIndex + 5]));
                        view6.setText(getString(R.string.company_siret, tab[estampIndex + 1]));
                        view7.setText(getString(R.string.company_estampille, tab[estampIndex]));
                        found = true;
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

            } catch (IOException e) {
                Log.wtf("Erreur dans la lecture du CSV " + line, e);
                e.printStackTrace();
            }
        }
        //If the stamp has no similarity in the CSV, the error page appears
        if (!found) {
            Intent otherActivity = new Intent(getApplicationContext(), PageErreur.class);
            startActivity(otherActivity);
            finish();
        }
    }
}
