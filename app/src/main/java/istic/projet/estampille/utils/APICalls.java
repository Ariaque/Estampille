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
 * Class which calls the remote API.
 */
public class APICalls {

    private static final String TAG = APICalls.class.getName();

    /**
     * Call the API to know if a given estampille match with any transformator in the database
     *
     * @param activity       activity launching the API call
     * @param estampille     the estampille we are searching a match for
     * @param progressDialog ProgressDialog that mus be hidden when treatment is over
     */
    public static void searchStampInRemoteAPI(Activity activity, String estampille, ProgressDialog progressDialog) {
        APIService apiService = APIService.retrofit.create(APIService.class);
        Call<APITransformateur> call = apiService.getTansformateur(estampille);
        call.enqueue(new Callback<APITransformateur>() {

            @Override
            public void onResponse(Call<APITransformateur> call, Response<APITransformateur> response) {
                APITransformateur searchedTransformateur = response.body();
                if (searchedTransformateur != null) {
                    // showing the searchedTransformateur details
                    APICalls.searchUserStateAPI(activity, searchedTransformateur, progressDialog);

                } else {
                    Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.no_match_toast), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<APITransformateur> call, @NonNull Throwable t) {
                Log.wtf(TAG, "onResponseFailed : " + call.request().url());
                t.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.api_problem), Toast.LENGTH_SHORT).show();
                if (progressDialog != null) {
                    progressDialog.hide();
                }
            }
        });
    }

    /**
     * Search the details to show in "En savoir plus".
     *
     * @param activity activity launching the API call
     * @param idTransformateur id of the searched {@link APITransformateur}
     */
    public static void searchMoreInRemoteAPI(Activity activity, String idTransformateur, ProgressDialog progressDialog) {
        APIService apiService = APIService.retrofit.create(APIService.class);
        Call<APIInfosTransformateur> call = apiService.getInfosTansformateur(idTransformateur);
        call.enqueue(new Callback<APIInfosTransformateur>() {
            @Override
            public void onResponse(Call<APIInfosTransformateur> call, Response<APIInfosTransformateur> response) {
                APIInfosTransformateur infosTransformateur = response.body();
                if (infosTransformateur != null) {
                    Intent intent = new Intent(activity.getApplicationContext(), KnowMoreActivity.class);
                    intent.putExtra(Constants.KEY_INTENT_MORE_INFOS_TRANSFORMATEUR, infosTransformateur);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.api_problem), Toast.LENGTH_SHORT).show();
                }
                if (progressDialog != null) {
                    progressDialog.hide();
                }

            }

            @Override
            public void onFailure(@NonNull Call<APIInfosTransformateur> call, @NonNull Throwable t) {
                Log.wtf(TAG, "onResponseFailed : " + call.request().url());
                t.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.api_problem), Toast.LENGTH_SHORT).show();
                if (progressDialog != null) {
                    progressDialog.hide();
                }
            }
        });
    }


    /**
     * Checks if the user info are null or not. Disables the "En savoir plus" button if yes.
     * @param activity The activity launching the API call.
     * @param transformateur The transformer associated with its infos.
     * @param progressDialog Progress dialog of the API call.
     */
    public static void areUserInfosNull(Activity activity, APITransformateur transformateur, ProgressDialog progressDialog) {
        APIService apiService = APIService.retrofit.create(APIService.class);
        Call<APIInfosTransformateur> call = apiService.getInfosTansformateur(transformateur.getId().toString());
        call.enqueue(new Callback<APIInfosTransformateur>() {
            @Override
            public void onResponse(Call<APIInfosTransformateur> call, Response<APIInfosTransformateur> response) {
                APIInfosTransformateur infosTransformateur = response.body();
                if(infosTransformateur == null) {
                    transformateur.setKnowMoreActive(false);
                }
                HistoryFragment.writeSearchInHistory(activity, transformateur);
                Intent intent = new Intent(activity.getApplicationContext(), DisplayMapActivity.class);
                intent.putExtra(Constants.KEY_INTENT_SEARCHED_TRANSFORMATEUR, transformateur);
                activity.startActivity(intent);
                if (progressDialog != null) {
                    progressDialog.hide();
                }

            }

            @Override
            public void onFailure(Call<APIInfosTransformateur> call, Throwable t) {
                if (progressDialog != null) {
                    progressDialog.hide();
                }
            }

        });
    }


    /**
     * Call the API to know if a given user account is active or not to know if we have to display the know more button or not.
     *
     * @param activity
     * @param searchedTransformateur Transformator that have the SIRET number of the user we are looking for
     */
    public static void searchUserStateAPI(Activity activity, APITransformateur searchedTransformateur, ProgressDialog progressDialog) {
        APIService apiService = APIService.retrofit.create(APIService.class);
        Call<Boolean> call = apiService.getUserState(searchedTransformateur.getSiret());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean userState = response.body();
                if (userState != null) {
                    searchedTransformateur.setKnowMoreActive(userState);
                } else {
                    Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.api_problem), Toast.LENGTH_SHORT).show();
                }
                APICalls.areUserInfosNull(activity, searchedTransformateur, progressDialog);
            }


            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.wtf(TAG, "onResponseFailed : " + call.request().url());
                t.printStackTrace();
                Toast.makeText(activity.getApplicationContext(), activity.getApplicationContext().getString(R.string.api_problem), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
