package istic.projet.estampille.utils;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import istic.projet.estampille.models.APITransformateur;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(new LoggingInterceptor())
            .build();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.43.194:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

    @GET("transformateur")
    Call<APITransformateur> getTansformateur(@Query("estampille") String estampille);

    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Log.wtf("API : ", String.format("--> Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Log.wtf("API : request", String.valueOf(request));
            okhttp3.Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Log.wtf("API : ", String.format("<-- Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            Log.wtf("API : response : ", String.valueOf(response));
            assert response.body() != null;
            MediaType contentType = response.body().contentType();
            String content = response.body().string();
            ResponseBody wrappedBody = ResponseBody.create(contentType, content);
            return response.newBuilder().body(wrappedBody).build();
        }
    }
}
