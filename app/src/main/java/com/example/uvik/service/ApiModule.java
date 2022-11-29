package com.example.uvik.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiModule {

    private static ApiModule instance = null;
    private Service service;

    private ApiModule() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/civicinfo/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Service.class);
    }

    public static synchronized ApiModule getInstance() {
        if (instance == null) {
            instance = new ApiModule();
        }
        return instance;
    }

    public Service getService() {
        return service;
    }
}
