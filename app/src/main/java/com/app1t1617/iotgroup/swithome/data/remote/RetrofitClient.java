package com.app1t1617.iotgroup.swithome.data.remote;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//Constructor de cliente de api
public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}