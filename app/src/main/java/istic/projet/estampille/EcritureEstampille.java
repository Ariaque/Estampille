package istic.projet.estampille;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * This activity matches with the screen which permit to know the origins of the product
 */
public class EcritureEstampille extends AppCompatActivity {

    private EditText zoneText;
    private Button buttonValider;
    private Button buttonRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Display the screen which permit to type the stamp
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estampille);

        //Retrieve design elements
        this.buttonRetour = findViewById(R.id.buttonRetour);
        this.buttonValider = findViewById(R.id.button);
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
        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    readCsv();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
    private void readCsv() throws FileNotFoundException {
        InputStream is = getResources().openRawResource(R.raw.bdd_test);
        ArrayList<String> List = new ArrayList<>();
        //    InputStream is = null;
       /* try {
            is = new FileInputStream(new File(Environment
                    .getExternalStorageDirectory().toString()
                    + "/data/foodorigin_datagouv.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        boolean found = false;
        String productEstamp = "";

      //  if (is != null) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8)
            );
            //Recover the stamp in the text field
            productEstamp = this.zoneText.getText().toString();
            String aCompanyLine = "";
            try {
                while ((aCompanyLine = reader.readLine()) != null) {
                    String[] tab = aCompanyLine.split(";");
                    String fileName = "historyFile.txt";
                    if (productEstamp.equals(tab[1])) {
                       try {
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(fileName, Context.MODE_APPEND)));
                            bw.write(tab[3]+"\n");
                            bw.close();
                        }
                        catch (Exception e){
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(getApplicationContext(), DisplayMap.class);
                        Bundle mapBundle = new Bundle();
                        mapBundle.putStringArray("Infos", tab);
                        intent.putExtras(mapBundle);
                        startActivity(intent);
                        found = true;

                    }
                }

            } catch (IOException e) {
                Log.wtf("Erreur dans la lecture du CSV " + aCompanyLine, e);
                e.printStackTrace();
            }
            //If the stamp has no similarity in the CSV, the error page appears
            if (!found) {
                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.no_match_toast), Toast.LENGTH_SHORT).show();
            }


        //     }
    }
}
