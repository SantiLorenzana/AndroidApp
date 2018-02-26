package com.app1t1617.iotgroup.swithome.Devices;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app1t1617.iotgroup.swithome.R;
import com.app1t1617.iotgroup.swithome.data.model.Get;
import com.app1t1617.iotgroup.swithome.data.remote.ApiUtils;
import com.app1t1617.iotgroup.swithome.data.remote.RaspberryAPIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chema.dev on 21/2/18.
 */

public class DevicesFragment extends Fragment {
    public View view;
    public Context context;

    LinearLayout lightButton;
    LinearLayout soundButton;
    LinearLayout searchButton;

    FragmentTransaction fragmentTransaction;

    //Toast
    View layout;
    Toast toast;
    TextView textToast;

    private RaspberryAPIService mRaspberryAPIService;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DevicesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_devices, container, false);

        getActivity().setTitle("Mis dispositivos");

        soundButton = (LinearLayout) view.findViewById(R.id.sound_button);
        lightButton = (LinearLayout) view.findViewById(R.id.light_button);
        searchButton = (LinearLayout) view.findViewById(R.id.search_button);

        fragmentTransaction = getFragmentManager().beginTransaction();

        mRaspberryAPIService = ApiUtils.getRaspberryAPIService();

        //Toast
        layout = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.toastlinear));
        textToast = (TextView) layout.findViewById(R.id.toastapp);
        toast = new Toast(getActivity());

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motherOfToast("Funcionalidad en construcción");
            }
        });

        lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                fragmentTransaction.replace(R.id.main_frame_layout, new LightDevicesListFragment()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motherOfToast("Funcionalidad en construcción");
            }
        });

        return view;
    }

    //toast function
    public void motherOfToast(String message){
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,50);
        textToast.setText(message);
        toast.setView(layout);
        toast.show();
    }
}
