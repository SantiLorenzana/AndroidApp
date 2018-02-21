package com.app1t1617.iotgroup.swithome.data.model;


import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Serializable
{

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("url_photo")
    @Expose
    private String urlPhoto;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("pass")
    @Expose
    private String pass;

    @SerializedName("id")
    @Expose
    private String id;

    private final static long serialVersionUID = -6918871039318354207L;

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
    }

    /**
     *
     * @param id
     * @param email
     * @param urlPhoto
     * @param name
     * @param pass
     */
    public User(String name, String urlPhoto, String email, String pass, String id) {
        super();
        this.name = name;
        this.urlPhoto = urlPhoto;
        this.email = email;
        this.pass = pass;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User withName(String name) {
        this.name = name;
        return this;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public User withUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public User withPass(String pass) {
        this.pass = pass;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User withId(String id) {
        this.id = id;
        return this;
    }

    public User fromJson(String json){

        Gson gson = new Gson();

        return gson.fromJson(json, User.class);

    }

}