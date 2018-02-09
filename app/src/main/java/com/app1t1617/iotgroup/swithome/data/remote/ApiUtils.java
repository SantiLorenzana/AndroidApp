package com.app1t1617.iotgroup.swithome.data.remote;
//Enlace padre de la api y creacion del cliente de retrofit
public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://h2744356.stratoserver.net/domotics/serverIoTApi/public/index.php/";


    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static RaspberryAPIService getRaspberryAPIService() {
        return RetrofitClient.getClient("").create(RaspberryAPIService.class);
    }
}