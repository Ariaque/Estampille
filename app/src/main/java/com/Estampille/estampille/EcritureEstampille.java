package com.Estampille.estampille;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EcritureEstampille extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecriture_estampille);
    }

    private List<estampilleCsv> estampilleCsv = new ArrayList<>();
    private void readCsv() {
     InputStream is = getResources().openRawResource(R.raw.bdd);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                //split ';'
                String[] tab = line.split(";");

                //lire les données

                estampilleCsv estampille = new estampilleCsv();
                estampille.setDep(Integer.parseInt(tab[0]));
                estampille.setCode(tab[1]);
                estampille.setSiret(Integer.parseInt(tab[2]));
                estampille.setNom(tab[3]);
                estampille.setAdresse(tab[4]);
                estampille.setCodePostal(Integer.parseInt(tab[5]));
                estampille.setCommune(tab[6]);
                estampille.setCategorie(tab[7]);
                estampille.setActivite(tab[8]);
                estampille.setEspece(tab[9]);
                estampilleCsv.add(estampille);

                Log.d("Estampille :" ,"Voici les informations de l'estampille" + estampille);
            }
        } catch (IOException e){
            Log.wtf("Erreur  dans la lecture du CSV " + line, e);
            e.printStackTrace();
        }
    }
}