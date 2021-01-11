package istic.projet.estampille.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import istic.projet.estampille.DisplayMapActivity;
import istic.projet.estampille.HistoryFragment;
import istic.projet.estampille.KnowMoreActivity;
import istic.projet.estampille.R;
import istic.projet.estampille.models.APIInfosTransformateur;
import istic.projet.estampille.models.APITransformateur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class which calls the remote API
 */
public class APICalls {

    private static final String TAG = APICalls.class.getName();

    public static void searchStampInRemoteAPI(Activity activity, String estampille, ProgressDialog progressDialog) {
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
                    intent.putExtra(Constants.KEY_INTENT_SEARCHED_TRANSFORMATEUR, searchedTransformateur);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.no_match_toast), Toast.LENGTH_SHORT).show();
                }
                if(progressDialog != null){
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(@NonNull Call<APITransformateur> call, @NonNull Throwable t) {
                Log.wtf(TAG, "onResponseFailed : " + call.request().url());
                t.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.api_problem), Toast.LENGTH_SHORT).show();
                if(progressDialog != null){
                    progressDialog.hide();
                }
            }
        });
    }

    /**
     * Search the details to show in "En savoir plus".
     *
     * @param activity
     * @param idTransformateur
     */
    public static void searchMoreInRemoteAPI(Activity activity, String idTransformateur) {
        APIService apiService = APIService.retrofit.create(APIService.class);
        Call<APIInfosTransformateur> call = apiService.getInfosTansformateur(idTransformateur);
        call.enqueue(new Callback<APIInfosTransformateur>() {
            @Override
            public void onResponse(Call<APIInfosTransformateur> call, Response<APIInfosTransformateur> response) {
                APIInfosTransformateur infosTransformateur = response.body();
                if (infosTransformateur != null) {
                    Intent intent = new Intent(activity.getApplicationContext(), KnowMoreActivity.class);
                    intent.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, infosTransformateur);
                    activity.startActivity(intent);}

            }

            @Override
            public void onFailure(@NonNull Call<APIInfosTransformateur> call, @NonNull Throwable t) {
                Log.wtf(TAG, "onResponseFailed : " + call.request().url());
                t.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.api_problem), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
