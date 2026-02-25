package com.example.phanumas_vanichayavarodomkulyelp;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YelpApiClient {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://api.yelp.com/v3/";

    public static Retrofit getClient() {
        if (retrofit == null) {

            // Create a logging interceptor for HTTP requests
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);  // Log the body of the request

            // Build OkHttpClient with the logging interceptor and authentication header
            // Add Yelp Authorization header using API key stored in local PC gradle.properties (~/.gradle/gradle.properties)
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + BuildConfig.YELP_API_KEY)
                                .build();
                        return chain.proceed(request);
                    })
                    .addInterceptor(loggingInterceptor)
                    .build();

            // Initialize Retrofit with the OkHttpClient and Gson converter
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
