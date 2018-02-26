package com.app1t1617.iotgroup.swithome.data.remote;

import com.app1t1617.iotgroup.swithome.data.model.Get;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by chema.dev on 9/2/18.
 */

public interface RaspberryAPIService {
    String HTTP = "http://";
    String DIRECTORY_BASE = "/serverSwitcHome/public/index.php/";
    String RASPBERRY_URL = "http://192.168.2.7/serverSwitcHome/public/index.php/";
    String POWER_ON_ENDPOINT = "welcome/powerOn.json";
    String POWER_OFF_ENDPOINT = "welcome/powerOff.json";

    @GET(RASPBERRY_URL+"welcome/executeScript.json")
    Call<Get> switchLight();

    @GET(HTTP+"{ip}"+DIRECTORY_BASE+POWER_OFF_ENDPOINT)
    Call<Get> turnOffLight(@Path(value = "ip", encoded = true) String ip);

    @GET(HTTP+"{ip}"+DIRECTORY_BASE+POWER_ON_ENDPOINT)
    Call<Get> turnOnLight(@Path(value = "ip", encoded = true) String ip);
}
