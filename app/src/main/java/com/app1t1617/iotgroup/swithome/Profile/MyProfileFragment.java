package com.app1t1617.iotgroup.swithome.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app1t1617.iotgroup.swithome.R;

/**
 * Created by chema.dev on 11/2/18.
 */

public class MyProfileFragment extends Fragment {
    View view;
    Context context;

    TextView username;
    ImageButton changePhoto;
    ImageButton changeEmail;
    ImageButton changePass;

    //Declaración de preferencias
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    View layout;
    Toast toast;
    TextView textToast;

    public MyProfileFragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("Mi perfil");

        //Toast
        layout = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.toastlinear));
        textToast = (TextView) layout.findViewById(R.id.toastapp);
        toast = new Toast(context);

        username = (TextView) view.findViewById(R.id.profile_username);
        changePhoto = (ImageButton) view.findViewById(R.id.profile_change_photo);
        changeEmail = (ImageButton) view.findViewById(R.id.profile_change_email);
        changePass = (ImageButton) view.findViewById(R.id.profile_change_pass);

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motherOfToast("Funcionalidad en desarrollo");
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motherOfToast("Funcionalidad en desarrollo");
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motherOfToast("Funcionalidad en desarrollo");
            }
        });

        //Obtener de preferencias el dato guardado
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();

        String name = prefs.getString("nameLogged", "No name defined");

        username.setText(name);


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
