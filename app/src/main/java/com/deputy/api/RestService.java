package com.deputy.api;

import com.deputy.api.model.Business;
import com.deputy.api.model.Shift;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface RestService {

    @GET("/dmc/business")
    Call<Business> getBusiness();

    @POST("/dmc/shift/start")
    Call<String> startShift(@Body JsonObject jsonObject);

    @POST("/dmc/shift/end")
    Call<String> endShift(@Body JsonObject jsonObject);

    @GET("/dmc/shifts")
    Call<Shift.Collection> getShifts();

}
