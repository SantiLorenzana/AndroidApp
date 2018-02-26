package com.app1t1617.iotgroup.swithome.Objets;

/**
 * Created by chema.dev on 22/2/18.
 */

public class Device {

    public String name;
    public String type;
    public String description;
    public String id;
    public String ip;
    public Boolean state;
    public Boolean following;
    public Integer intensity;

    public Device(String name, String type, String description, String id, String ip, Boolean state, Boolean following, Integer intensity){
        this.name = name;
        this.type = type;
        this.description = description;
        this.id = id;
        this.ip = ip;
        this.state = state;
        this.following = following;
        this.intensity = intensity;
    }
}
