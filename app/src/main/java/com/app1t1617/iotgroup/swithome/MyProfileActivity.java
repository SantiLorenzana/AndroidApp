package com.app1t1617.iotgroup.swithome;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class MyProfileActivity extends AppCompatActivity {

    TextView userName;
    //Declaración de preferencias
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Relacion de variables con id
        userName = (TextView) findViewById(R.id.userNameProfile);

        //Obtener de preferencias el dato guardado
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        String name = prefs.getString("nameLogged", "No name defined");

        //Usar ese dato en el sistema
        userName.setText(name);


    }
}
