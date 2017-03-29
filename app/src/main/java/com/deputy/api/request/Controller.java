package com.deputy.api.request;

import com.deputy.api.RestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {

    static final String BASE_URL = "https://apjoqdqpi3.execute-api.us-west-2.amazonaws.com/";
    private static RestService apiCall;


    public static RestService createApiCallInstance() {
        start();
        return apiCall;
    }


    private static void start() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request request = chain.request().newBuilder()
                                        .addHeader("Accept", "Application/JSON")
                                        .addHeader("Authorization","Deputy"+"f96dfb2acfd936fdead0bb6f3f7fd617f227dc51")
                                        .build();

                                return chain.proceed(request);
                            }
                        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(defaultHttpClient)
                .build();

        apiCall = retrofit.create(RestService.class);

    }

}
