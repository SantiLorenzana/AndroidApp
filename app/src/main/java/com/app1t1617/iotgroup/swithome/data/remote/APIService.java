package com.app1t1617.iotgroup.swithome.data.remote;

import android.media.Image;

import com.app1t1617.iotgroup.swithome.data.model.Data;
import com.app1t1617.iotgroup.swithome.data.model.Get;
import com.app1t1617.iotgroup.swithome.data.model.Post;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("switchome/singleIp.json")
    Call<Get> device(@Header("Authorization") String token, @Query("id") String id);

    @Multipart
    @POST("users/updateData.json")
    Call<Post> updateUser(@Part("pass") RequestBody pass,
                          @Part("passOld") RequestBody passOld,
                          @Part("email") RequestBody email,
                          @Part MultipartBody.Part photo,
                          @Header("Authorization") String token);
}