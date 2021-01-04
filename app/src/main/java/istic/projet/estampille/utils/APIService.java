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
    //            .baseUrl("http://localhost:8080/")
    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("http://10.188.77.125:8080/")
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

            Buffer requestBuffer = new Buffer();
//            request.body().writeTo(requestBuffer);
            Log.wtf("API : ", requestBuffer.readUtf8());

            okhttp3.Response response = chain.proceed(request);
            System.out.println("RESPONSE : " + response);
            long t2 = System.nanoTime();
            Log.wtf("API : ", String.format("<-- Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            MediaType contentType = response.body().contentType();
            String content = response.body().string();
            Log.wtf("API : ", content);

            ResponseBody wrappedBody = ResponseBody.create(contentType, content);
            return response.newBuilder().body(wrappedBody).build();
        }
    }
}
