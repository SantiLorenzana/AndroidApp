package com.app1t1617.iotgroup.swithome.data.remote;

import com.app1t1617.iotgroup.swithome.data.model.Get;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chema.dev on 9/2/18.
 */

public interface RaspberryAPIService {

    String RASPBERRY_URL = "http://192.168.6.239/serverSwitcHome/public/index.php/";

    @GET(RASPBERRY_URL+"welcome/executeScript.json")
    Call<Get> switchLight();
}
