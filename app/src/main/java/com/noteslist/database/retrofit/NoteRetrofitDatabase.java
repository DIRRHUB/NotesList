package com.noteslist.database.retrofit;

import com.noteslist.database.retrofit.result.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoteRetrofitDatabase {
    private static NoteRetrofitDatabase instance = null;
    private final Api myApi;

    private NoteRetrofitDatabase() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        myApi = retrofit.create(Api.class);
    }

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    public static synchronized NoteRetrofitDatabase getInstance() {
        if (instance == null) {
            instance = new NoteRetrofitDatabase();
        }
        return instance;
    }

    public Api getApi() {
        return myApi;
    }
}
