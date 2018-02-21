package com.app1t1617.iotgroup.swithome.data.model;


import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data implements Serializable
{

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("urlPhoto")
    @Expose
    private String urlPhoto;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user")
    @Expose
    private User user;
    private final static long serialVersionUID = -7107292181391806667L;



    /**
     * No args constructor for use in serialization
     *
     */
    public Data() {
    }

    /**
     *
     * @param token
     * @param user
     *
     */
    public Data(String token, User user) {
        super();
        this.token = token;
        this.user = user;
    }

    public Data(String token) {
        super();
        this.token = token;
    }

    /**
     *
     * @param token
     * @param name
     *
     */
    public Data(String token, String name) {
        super();
        this.token = token;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Data withToken(String token) {
        this.token = token;
        return this;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Data withUser(User user) {
        this.user = user;
        return this;
    }
    public String getName() {
        return name;
    }

    public void setUser(String name) {
        this.name = name;
    }

    public Data withUser(String name) {
        this.name = name;
        return this;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }
}