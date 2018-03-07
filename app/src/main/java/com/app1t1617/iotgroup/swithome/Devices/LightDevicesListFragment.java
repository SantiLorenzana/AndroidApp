package com.app1t1617.iotgroup.swithome.Devices;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.app1t1617.iotgroup.swithome.Objets.Device;
import com.app1t1617.iotgroup.swithome.Objets.DevicesIDPreferences;
import com.app1t1617.iotgroup.swithome.R;
import com.app1t1617.iotgroup.swithome.data.model.Get;
import com.app1t1617.iotgroup.swithome.data.remote.APIService;
import com.app1t1617.iotgroup.swithome.data.remote.ApiUtils;
import com.app1t1617.iotgroup.swithome.data.remote.RaspberryAPIService;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by chema.dev on 21/2/18.
 */

public class LightDevicesListFragment extends Fragment {
    final String LOG_TAG = "LightDevicesListFragmen";
    public View view;
    public Context context;

    ImageButton addButton;
    ListView devicesList;


    //Toast
    View layout;
    Toast toast;
    TextView textToast;

    //Declaración de preferencias
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    ArrayList<Device> lightDevices;
    ArrayAdapter adapter;


    //qr code scanner objects

    public EditText id;


    private RaspberryAPIService mRaspberryAPIService;
    private APIService mAPIService;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public LightDevicesListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_devices_fragment, container, false);

        getActivity().setTitle("Iluminación");


        //Obtener de preferencias el dato guardado
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();

        addButton = (ImageButton) view.findViewById(R.id.add_device_button);
        devicesList = (ListView) view.findViewById(R.id.list_devices);

        //Toast
        layout = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.toastlinear));
        textToast = (TextView) layout.findViewById(R.id.toastapp);
        toast = new Toast(getActivity());

        mAPIService = ApiUtils.getAPIService();


        lightDevices = new ArrayList<Device>();

        checkDevices();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setContentView(R.layout.alert_add_device);
                id = (EditText) dialog.findViewById(R.id.devices_add_input);
                // if (!resultId.isEmpty()){
                //   id.setText(resultId);
                //}

                //Boton e inicio del scan con el clicklistener
                Button scan = (Button) dialog.findViewById(R.id.buttonScan);

                scan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startScan();
                    }
                });

                Button cancel = (Button) dialog.findViewById(R.id.cancel_add_device);
                final Button acept = (Button) dialog.findViewById(R.id.accept_add_device);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                acept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addDeviceToPrefs(id.getText().toString());
                        getDeviceByID(id.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        return view;
    }

    public void getDeviceByID(final String id) {
        mAPIService.device(prefs.getString("token", ""), id).enqueue(new Callback<Get>() {
            @Override
            public void onResponse(Call<Get> call, Response<Get> response) {
                if (response.body().getCode() == 200) {

                    lightDevices.add(new Device("PROTOTIPO", "light", "sample description", id, response.body().getData().getIpDevice(), false, false, 100));
                    adapter = new LightListAdapter(getActivity(), R.layout.my_devices_list_adapter, lightDevices);
                    Log.d("IP", response.body().getData().getIpDevice() + "");
                    devicesList.setAdapter(adapter);
                } else {
                    Log.d("ERROR CON LA ID", response.headers() + "");
                }
            }

            @Override
            public void onFailure(Call<Get> call, Throwable t) {

            }
        });
    }

    public void addDeviceToPrefs(String id) {
        DevicesIDPreferences idDevices = new DevicesIDPreferences();
        idDevices = idDevices.fromJson(prefs.getString("idDevices", ""));
        if (idDevices == null) {
            idDevices = new DevicesIDPreferences();
        }
        idDevices.ids.add(id);
        String idsJson = idDevices.toJson();

        Log.d("AGREGANDO", idDevices + "");
        editor.putString("idDevices", idsJson);
        editor.commit();
    }

    public void checkDevices() {
        DevicesIDPreferences idDevices = new DevicesIDPreferences();
        idDevices = idDevices.fromJson(prefs.getString("idDevices", ""));

        Log.d("IDS", idDevices + "");
        if (!(idDevices == null)) {
            Log.d("IDS", idDevices.ids + "");
            for (String id : idDevices.ids) {
                getDeviceByID(id);
            }
        }
    }

    //inicio del intent para el scanner
    public void startScan() {

        new IntentIntegrator(getActivity()).initiateScan();
    }

    //toast function
    public void motherOfToast(String message) {
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 50);
        textToast.setText(message);
        toast.setView(layout);
        toast.show();
    }

}
