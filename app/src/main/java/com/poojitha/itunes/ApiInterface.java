package com.poojitha.itunes;

import com.poojitha.itunes.model.ItuneResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("search?")
    Call<ItuneResponse> getItunes(@Query("term") String term);

}