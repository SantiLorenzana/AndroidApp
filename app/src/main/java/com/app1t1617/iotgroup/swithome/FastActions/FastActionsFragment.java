package com.app1t1617.iotgroup.swithome.FastActions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app1t1617.iotgroup.swithome.Login.ResetPassActivity;
import com.app1t1617.iotgroup.swithome.Main.MainMenuFragment;
import com.app1t1617.iotgroup.swithome.R;
import com.app1t1617.iotgroup.swithome.data.model.Get;
import com.app1t1617.iotgroup.swithome.data.remote.ApiUtils;
import com.app1t1617.iotgroup.swithome.data.remote.RaspberryAPIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chema.dev on 9/2/18.
 */

public class FastActionsFragment extends Fragment {
    View view;
    Context context;

    ImageButton nightMode;
    ImageButton follow;
    ImageButton turnOff;

    Button reset;

    //Toast
    View layout;
    Toast toast;
    TextView textToast;

    //Declaración de preferencias
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private RaspberryAPIService mRaspberryAPIService;

    public FastActionsFragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_fast_actions, container, false);
        getActivity().setTitle("Funciones rápidas");

        nightMode = (ImageButton) view.findViewById(R.id.night_mode_button);
        follow = (ImageButton) view.findViewById(R.id.follow_button);
        reset = (Button) view.findViewById(R.id.reset_switchome);
        turnOff = (ImageButton) view.findViewById(R.id.switch_off_button);

        //Toast
        layout = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.toastlinear));
        textToast = (TextView) layout.findViewById(R.id.toastapp);
        toast = new Toast(getActivity());


        //Obtener de preferencias el dato guardado
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();

        //Preferencias para mostrar la info
        Boolean showInfo = prefs.getBoolean("showFastActionsInfo", true);
        showFastInfo(showInfo);

        mRaspberryAPIService = ApiUtils.getRaspberryAPIService();

        checkNightMode();
        checkFollowMode();


        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNightMode();
            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFollowMode();
            }
        });

        turnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTurnOffAlert();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetAlert();
            }
        });


        return view;
    }


    public void changeNightMode(){
        Boolean isNightMode = prefs.getBoolean("nightMode", true);
        Log.d("NIGHTMODE", isNightMode+"");
        if (!isNightMode) {
            nightMode.setImageResource(R.drawable.iconos_app_switchome);
            editor.putBoolean("nightMode", true);
        }else{
            nightMode.setImageResource(R.drawable.iconos_app_switchome_off);
            editor.putBoolean("nightMode", false);
        }
        editor.commit();
    }

    public void changeFollowMode(){
        Boolean isFollowMode = prefs.getBoolean("followMode", false);
        Log.d("FOLLOWMODE", isFollowMode+"");
        if (!isFollowMode) {
            follow.setImageResource(R.drawable.sensor_icon);
            editor.putBoolean("followMode", true);
        }else{
            follow.setImageResource(R.drawable.sensor_icon_off);
            editor.putBoolean("followMode", false);
        }
        editor.commit();
    }

    public void checkNightMode(){
        Boolean isNightMode = prefs.getBoolean("nightMode", true);
        if (!isNightMode){
            nightMode.setImageResource(R.drawable.iconos_app_switchome_off);
        }else{
            nightMode.setImageResource(R.drawable.iconos_app_switchome);
        }
    }

    public void checkFollowMode(){
        Boolean isFollowMode = prefs.getBoolean("followMode", false);
        if (!isFollowMode){
            follow.setImageResource(R.drawable.sensor_icon_off);
        }else{
            follow.setImageResource(R.drawable.sensor_icon);
        }
    }

    public void showFastInfo(Boolean showInfo){
        if (showInfo){
            final Dialog infoDialog = new Dialog(context);
            infoDialog.getWindow().setContentView(R.layout.alert_fast_actions_info);

            Button ok = (Button) infoDialog.findViewById(R.id.ok_fast_actions_button);
            final CheckBox check = (CheckBox) infoDialog.findViewById(R.id.checkbox_fast_actions);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(check.isChecked()){
                        editor.putBoolean("showFastActionsInfo", false);
                        editor.commit();
                    }
                    infoDialog.dismiss();
                }
            });
            infoDialog.show();
        }
    }

    public void showTurnOffAlert(){
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setContentView(R.layout.alert_turn_off);

        Button cancel = (Button) dialog.findViewById(R.id.cancel_off);
        Button acept = (Button) dialog.findViewById(R.id.accept_off);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRaspberryAPIService.turnOffLight().enqueue(new Callback<Get>() {
                    @Override
                    public void onResponse(Call<Get> call, Response<Get> response) {
                        if (response.body().getCode() == 200){
                            motherOfToast(response.body().getMessage());
                            dialog.dismiss();
                        }else{
                            motherOfToast(response.body().getMessage());
                        }
                        Log.d("RASPBERRY", response.headers()+"");
                    }
                    @Override
                    public void onFailure(Call<Get> call, Throwable t) {
                        Log.d("RASPBERRY", t+"");
                    }
                });
            }
        });

        dialog.show();
    }

    public void showResetAlert(){
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setContentView(R.layout.alert_reset_switchome);

        Button cancel = (Button) dialog.findViewById(R.id.cancel_reset);
        Button acept = (Button) dialog.findViewById(R.id.accept_reset);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motherOfToast("Funcionalidad en construcción");
            }
        });
        dialog.show();
    }

    //toast function
    public void motherOfToast(String message){
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,50);
        textToast.setText(message);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
