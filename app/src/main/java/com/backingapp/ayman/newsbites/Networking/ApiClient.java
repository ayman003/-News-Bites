package com.backingapp.ayman.newsbites.Networking;

import com.backingapp.ayman.newsbites.BuildConfig;
import com.backingapp.ayman.newsbites.Models.Result;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://newsapi.org/";

    private static ApiClient instance;
    private ApiService apiService;

    private ApiClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG)
            httpClient.addNetworkInterceptor(new StethoInterceptor());
        OkHttpClient client = httpClient.build();

        Gson gson = new GsonBuilder().create();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        apiService = retrofit.create(ApiService.class);

    }

    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public Single<Result> getNews(String apiKey, String country) {
        return apiService.getHeadLinesNews(country, apiKey);
    }

    public Single<Result> getNews(String apiKey, String country, String category) {
        return apiService.getHeadLinesNewsByCategory(category, country, apiKey);
    }

    public Single<Result> searchNews(String apiKey, String searchKeyword) {
        return apiService.searchNews(searchKeyword, apiKey);
    }

}
