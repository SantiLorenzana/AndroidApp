package com.app1t1617.iotgroup.swithome.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Constructor de datos devueltos de la Api en en cualquier peticion de data
public class Data {
    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("user")
    @Expose
    private User user;

    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user = user;
    }
}