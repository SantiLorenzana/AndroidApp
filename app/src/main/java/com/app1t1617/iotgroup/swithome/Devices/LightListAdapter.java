package com.app1t1617.iotgroup.swithome.Devices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import com.app1t1617.iotgroup.swithome.Objets.Device;
import com.app1t1617.iotgroup.swithome.R;
import com.app1t1617.iotgroup.swithome.data.model.Get;
import com.app1t1617.iotgroup.swithome.data.remote.ApiUtils;
import com.app1t1617.iotgroup.swithome.data.remote.RaspberryAPIService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chema.dev on 21/2/18.
 */

public class LightListAdapter extends ArrayAdapter{

    ArrayList<Device> itemList;
    private RaspberryAPIService mRaspberryAPIService;

    public LightListAdapter(@NonNull Context context, int resource, ArrayList<Device> itemList) {
        super(context, resource, itemList);
        mRaspberryAPIService = ApiUtils.getRaspberryAPIService();
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.my_devices_list_adapter, parent, false);


        final Device listItem = itemList.get(position);



        TextView itemText = (TextView) view.findViewById(R.id.my_devices_row_text);
        final Switch itemSwitch = (Switch) view.findViewById(R.id.my_devices_row_switch);
        final CheckBox itemCheckBox = (CheckBox) view.findViewById(R.id.my_devices_row_checkbox);


        itemSwitch.setChecked(listItem.state);
        itemCheckBox.setChecked(listItem.following);

        itemSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemSwitch.isChecked()){
                    turnOn(listItem);
                }else{
                    turnOff(listItem);
                }
            }
        });

        itemCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemCheckBox.isChecked()){
                    listItem.following = true;
                    //El objeto le sigue
                }else{
                    listItem.following = false;
                    //El objeto no le sigue
                }
            }
        });



        itemText.setText(listItem.name);

        return view;
    }

    public void turnOff(final Device item){
        mRaspberryAPIService.turnOffLight(item.ip).enqueue(new Callback<Get>() {
            @Override
            public void onResponse(Call<Get> call, Response<Get> response) {
                Log.d("RASPBERRY", response.headers()+"");
                Log.d("RESPUESTA", response.body().getMessage()+"");
                item.state = false;
            }
            @Override
            public void onFailure(Call<Get> call, Throwable t) {
                Log.d("RASPBERRY", t+"");
                Log.d("CALL", call.request()+"");
            }
        });

    }

    public void turnOn(final Device item){

        mRaspberryAPIService.turnOnLight(item.ip).enqueue(new Callback<Get>() {
            @Override
            public void onResponse(Call<Get> call, Response<Get> response) {
                Log.d("RASPBERRY", response.headers()+"");
                Log.d("RESPUESTA", response.body().getMessage()+"");
                item.state = true;
            }
            @Override
            public void onFailure(Call<Get> call, Throwable t) {
                Log.d("RASPBERRY", t+"");
                Log.d("CALL", call.request()+"");
            }
        });

    }

}
