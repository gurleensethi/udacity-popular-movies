package com.thetehnocafe.gurleensethi.popularmovies.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Singleton Class
public class NetworkService {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static NetworkService mInstance;
    private final Retrofit mRetrofit;
    private TMDBApi mTmdbApi;

    private NetworkService() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public TMDBApi getTMDBApi() {
        if (mTmdbApi == null) {
            mTmdbApi = mRetrofit.create(TMDBApi.class);
        }
        return mTmdbApi;
    }
}
