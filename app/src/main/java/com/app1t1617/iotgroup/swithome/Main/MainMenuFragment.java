package com.app1t1617.iotgroup.swithome.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app1t1617.iotgroup.swithome.FastActions.FastActionsFragment;
import com.app1t1617.iotgroup.swithome.R;
import com.app1t1617.iotgroup.swithome.data.model.Get;
import com.app1t1617.iotgroup.swithome.data.remote.APIService;
import com.app1t1617.iotgroup.swithome.data.remote.ApiUtils;
import com.app1t1617.iotgroup.swithome.data.remote.RaspberryAPIService;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuFragment extends Fragment {
    public View view;
    public Context context;

    TextView userNameTitle;
    ImageView myDevices;
    ImageView myConfig;
    ImageView fastActions;
    ImageView myProfile;

    FragmentTransaction fragmentTransaction;

    //Declaración de preferencias
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private RaspberryAPIService mRaspberryAPIService;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public MainMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        userNameTitle = (TextView) view.findViewById(R.id.userNameTitle);
        myDevices = (ImageView) view.findViewById(R.id.MyDevices);
        myConfig = (ImageView) view.findViewById(R.id.MyConfig);
        fastActions = (ImageView) view.findViewById(R.id.FastActions);
        myProfile = (ImageView) view.findViewById(R.id.MyProfile);

        mRaspberryAPIService = ApiUtils.getRaspberryAPIService();

        //Obtener de preferencias el dato guardado
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        String name = prefs.getString("nameLogged", "No name defined");

        //Usar ese dato en el sistema
        userNameTitle.setText(name);

        fragmentTransaction = getFragmentManager().beginTransaction();



        myDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRaspberryAPIService.switchLight().enqueue(new Callback<Get>() {
                    @Override
                    public void onResponse(Call<Get> call, Response<Get> response) {

                        Log.d("RASPBERRY", response.headers()+"");
                    }

                    @Override
                    public void onFailure(Call<Get> call, Throwable t) {
                        Log.d("RASPBERRY", t+"");
                    }
                });
            }
        });

        fastActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                fragmentTransaction.replace(R.id.main_frame_layout, new FastActionsFragment()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
