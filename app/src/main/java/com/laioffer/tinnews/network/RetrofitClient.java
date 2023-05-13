package com.laioffer.tinnews.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//This class is responsible for providing a configured Retrofit instance,
// that can then instantiate a NewsApi implementation.
public class RetrofitClient {

    // TODO: Assign your API_KEY here
    private static final String API_KEY = "e5d413e1e7004d4184d80ba57be1f692";
    private static final String BASE_URL = "https://newsapi.org/v2/";

    ////Gson adapter: JSON response can be deserialized into model classes.
    public static Retrofit newInstance() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
    //A header interceptor.
    // You can attach custom or standard header information to all requests.
    private static class HeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original
                    .newBuilder()
                    .header("X-Api-Key", API_KEY)
                    .build();
            return chain.proceed(request);
        }
    }
}
