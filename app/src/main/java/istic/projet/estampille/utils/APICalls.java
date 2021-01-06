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

/**
 * Class which calls the remote API
 */
public class APICalls {

    private static final String TAG = APICalls.class.getName();

    public static void executeHttpRequestWithRetrofit(Activity activity, String estampille) {
        APIService apiService = APIService.retrofit.create(APIService.class);
        Call<APITransformateur> call = apiService.getTansformateur(estampille);
        call.enqueue(new Callback<APITransformateur>() {
            /**
             * If a response is given by the API, we reify the received object.
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<APITransformateur> call, Response<APITransformateur> response) {
                APITransformateur searchedTransformateur = response.body();
                if (searchedTransformateur != null) {
                    HistoryFragment.writeSearchInHistory(activity, searchedTransformateur);
                    // showing the searchedTransformateur details
                    Intent intent = new Intent(activity.getApplicationContext(), DisplayMapActivity.class);
                    intent.putExtra("searchedTransformateur", searchedTransformateur);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.no_match_toast), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<APITransformateur> call, @NonNull Throwable t) {
                Log.wtf(TAG, "onResponseFailed : " + call.request().url());
                t.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.api_problem), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
