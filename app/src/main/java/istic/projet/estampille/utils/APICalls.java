package istic.projet.estampille.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import istic.projet.estampille.DisplayMapActivity;
import istic.projet.estampille.HistoryFragment;
import istic.projet.estampille.R;
import istic.projet.estampille.models.APITransformateur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APICalls {

    public static void executeHttpRequestWithRetrofit(Activity activity, String estampille) {
        APIService apiService = APIService.retrofit.create(APIService.class);
        Call<APITransformateur> call = apiService.getTansformateur(estampille);
        call.enqueue(new Callback<APITransformateur>() {
            @Override
            public void onResponse(Call<APITransformateur> call, Response<APITransformateur> response) {
                APITransformateur searchedTransformateur = response.body();
                if (searchedTransformateur != null) {
                    HistoryFragment.writeSearchInCSV(activity, searchedTransformateur);
                    Log.wtf("API : result : act1 ", searchedTransformateur.toString());
                    Intent intent = new Intent(activity.getApplicationContext(), DisplayMapActivity.class);
                    intent.putExtra("searchedTransformateur", searchedTransformateur);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.no_match_toast), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<APITransformateur> call, @NonNull Throwable t) {
                Log.wtf("API : ", "onResponseFailed: " + call.request().url());
                t.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.api_problem), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
