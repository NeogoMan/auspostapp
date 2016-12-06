package com.conducthq.auspost.service;

import com.conducthq.auspost.helper.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by conduct19 on 21/10/2016.
 */

public class RestAdapter {

    static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(
                    new Interceptor() {
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {
                            Request request = chain.request().newBuilder()
                                    .addHeader("Accept", "application/json").build();
                            return chain.proceed(request);
                        }
                    }).build();

    static public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
