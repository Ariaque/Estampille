package istic.projet.estampille.utils;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;

import istic.projet.estampille.models.APITransformateur;
import retrofit2.Call;
import retrofit2.Response;

public class AsyncCalls extends AsyncTask<Call, Void, String> {
    @Override
    protected String doInBackground(Call... params) {
        try {
            Call<APITransformateur> call = params[0];
            Response<APITransformateur> response = call.execute();
            return response.body().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println(result);
    }
}
