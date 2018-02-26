package com.app1t1617.iotgroup.swithome.Objets;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by chema.dev on 22/2/18.
 */

public class DevicesIDPreferences {

    public ArrayList<String> ids;

    public DevicesIDPreferences(){
        this.ids = new ArrayList<>();
    }

    public DevicesIDPreferences(ArrayList<String> ids){
        this.ids = new ArrayList<>(ids);
    }


    public String toJson(){

        Gson gson = new Gson();

        return gson.toJson(this);

    }

    public DevicesIDPreferences fromJson(String json){

        Gson gson = new Gson();

        return gson.fromJson(json, DevicesIDPreferences.class);

    }
}
