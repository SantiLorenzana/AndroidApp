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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.app1t1617.iotgroup.swithome.Login.ResetPassActivity;
import com.app1t1617.iotgroup.swithome.Main.MainMenuFragment;
import com.app1t1617.iotgroup.swithome.R;

/**
 * Created by chema.dev on 9/2/18.
 */

public class FastActionsFragment extends Fragment {
    View view;
    Context context;

    ImageButton nightMode;
    ImageButton follow;

    Button reset;

    //Declaración de preferencias
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

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

        //Obtener de preferencias el dato guardado
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();

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

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                dialog.show();
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
