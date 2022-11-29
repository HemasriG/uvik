package com.example.uvik.service;

import com.example.uvik.model.GetResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @GET("representatives")
    Call<GetResponse> getRepresentativesResponse(@Query("key") String key, @Query("address") String zipcode);

}
