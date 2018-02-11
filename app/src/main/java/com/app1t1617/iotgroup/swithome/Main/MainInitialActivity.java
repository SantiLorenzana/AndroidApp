package com.app1t1617.iotgroup.swithome.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app1t1617.iotgroup.swithome.FastActions.FastActionsFragment;
import com.app1t1617.iotgroup.swithome.Login.MainActivity;
import com.app1t1617.iotgroup.swithome.R;

public class MainInitialActivity extends AppCompatActivity {

    ListView menu;
    android.support.v4.app.FragmentManager fragmentManager;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    android.support.v4.app.Fragment fragment;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    View layout;
    Toast toast;
    TextView textToast;
    Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_initial);
        mainToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);

        setSupportActionBar(mainToolbar);

        //Toast
        layout = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastlinear));
        textToast = (TextView) layout.findViewById(R.id.toastapp);
        toast = new Toast(this);

        //Menú lateral configuración
        fragmentManager = getSupportFragmentManager();

        drawerLayout = (DrawerLayout) findViewById(R.id.menu_lateral);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mainToolbar, R.string.abrir_menu, R.string.cerrar_menu);
        drawerLayout.addDrawerListener(drawerToggle);

        //Iniciación de las preferencias
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        // Abrir pantalla de inicio
        abrirPantalla(0);

        // Selección de un elemento de menú
        menu = (ListView) findViewById(R.id.menu);
        ArrayAdapter adapter = new LateralMenuAdapter(this, R.layout.lateral_menu_adapter, getResources().getStringArray(R.array.pantallas));
        menu.setAdapter(adapter);

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                abrirPantalla(i);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


    }
    private void abrirPantalla(int i){

        fragmentTransaction = fragmentManager.beginTransaction();

        switch (i) {
            case 0:  // Abrir fragment Inicio
                clearBackStack();
                fragment = new MainMenuFragment();
                break;
            case 1:
                motherOfToast("Vista en construcción");
                break;
            case 2:
                motherOfToast("Vista en construcción");
                break;
            case 3:
                if (mainToolbar.getTitle() != "Funciones rápidas") {
                    clearBackStack();
                    fragment = new FastActionsFragment();
                    fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.enter_from_right, R.anim.exit_to_left);
                    fragmentTransaction.addToBackStack(null);
                }
                break;
            case 4:
                motherOfToast("Vista en construcción");
                break;
            case 5:
                backToLogin();
                break;
        }



        fragmentTransaction.replace(R.id.main_frame_layout, fragment, "Fragment");
        fragmentTransaction.commit();

    }

    public void clearBackStack(){
        fragmentManager.popBackStack();
        //fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_layout, new MainMenuFragment(), "Fragment");
        //fragmentTransaction.commit();
    }
    //Vuelta al login y se eliminan todas las credenciales y los datos del usuario
    public void backToLogin(){
        Intent intent = new Intent(this, MainActivity.class);
        editor.remove("token");
        editor.remove("nameLogged");
        editor.remove("firstLog");
        editor.commit();
        startActivity(intent);
        finish();
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
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
