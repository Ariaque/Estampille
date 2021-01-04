//package istic.projet.estampille.utils;
//
//import androidx.annotation.Nullable;
//
//import java.lang.ref.WeakReference;
//
//import istic.projet.estampille.models.APITransformateur;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class APICalls {
//
//    // 1 - Creating a callback
//    public interface Callbacks {
//        void onResponse(@Nullable APITransformateur transformateur);
//
//        void onFailure();
//    }
//
//    // 2 - Public method to start fetching transformateur
//    public static void fetchTransformateurSearched(Callbacks callbacks, String estampille) {
//
//        // 2.1 - Create a weak reference to callback (avoid memory leaks)
//        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);
//
//        // 2.2 - Get a Retrofit instance and the related endpoints
//        APIService apiService = APIService.retrofit.create(APIService.class);
//
//        // 2.3 - Create the call on API
//        Call<APITransformateur> call = apiService.getTansformateur(estampille);
//        // 2.4 - Start the call
//        call.enqueue(new Callback<APITransformateur>() {
//
//            @Override
//            public void onResponse(Call<APITransformateur> call, Response<APITransformateur> response) {
//                // 2.5 - Call the proper callback used in controller (MainFragment)
//                if (callbacksWeakReference.get() != null)
//                    callbacksWeakReference.get().onResponse(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<APITransformateur> call, Throwable t) {
//                // 2.5 - Call the proper callback used in controller (MainFragment)
//                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
//            }
//        });
//    }
//
//
//
//}
