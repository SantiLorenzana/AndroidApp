package com.app1t1617.iotgroup.swithome.data.remote;

import com.app1t1617.iotgroup.swithome.data.model.Get;
import com.app1t1617.iotgroup.swithome.data.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

//Aqui se añaden las terminaciones de cada endpoint y los datos que se van a subir
public interface APIService {
    @POST("users/create.json")
    @FormUrlEncoded
    Call<Post> registerUser(@Field("name") String name,
                        @Field("pass") String pass,
                        @Field("email") String email);

    @POST("users/update_pass.json")
    @FormUrlEncoded
    Call<Post> updatePass(@Field("pass") String pass,
                          @Header("Authorization") String token);

    @GET("users/login.json")
    Call<Get> loginUser(@Query("name") String name, @Query("pass") String pass);

    @GET("users/recover_pass.json")
    Call<Get> recoverUser(@Query("name") String name, @Query("email") String email);

    @GET("base/default_auth.json")
    Call<Get> defaultAuth(@Header("Authorization") String token);
}