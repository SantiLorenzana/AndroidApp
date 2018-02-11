package com.app1t1617.iotgroup.swithome.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app1t1617.iotgroup.swithome.Main.MainInitialActivity;
import com.app1t1617.iotgroup.swithome.R;
import com.app1t1617.iotgroup.swithome.data.model.Get;
import com.app1t1617.iotgroup.swithome.data.remote.APIService;
import com.app1t1617.iotgroup.swithome.data.remote.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText user;
    EditText pass;
    ProgressBar chargeBar;
    Button btnLogIn;
    TextView btnRecoverPass;
    TextView btnRegister;
    View mainView;
    //Declaracion de servicio
    private APIService mAPIService;
    //Declaración de preferencias
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    //Toast
    View layout;
    Toast toast;
    TextView textToast;


    //Adaptación de la API
    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Declaración del servicio
        mAPIService = ApiUtils.getAPIService();
        //Iniciación de las preferencias
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        checkLogin();

        //Relacion de variables con id
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);
        chargeBar = (ProgressBar) findViewById(R.id.chargeBar);
        btnLogIn = (Button) findViewById(R.id.buttonLogIn);
        btnRecoverPass = (TextView) findViewById(R.id.buttonRecoverPass);
        btnRegister = (TextView) findViewById(R.id.buttonGoToRegister);
        mainView = (View) findViewById(R.id.mainView);
        //Toast
        layout = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastlinear));
        textToast = (TextView) layout.findViewById(R.id.toastapp);
        toast = new Toast(this);


    }


    //Funcion cambiar a la vista de registro
    public void changeToRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    //Funcion cambiar a la vista de cambiar contraseña
    public void changeToResetPass(View view){
        Intent intent = new Intent(this, ResetPassActivity.class);
        startActivity(intent);
    }

    //Funcion cambiar a la vista de menu principal
    public void changeToLogin(){
        Intent intent = new Intent(this, MainInitialActivity.class);
        startActivity(intent);
        finish();
    }

    //Funcion obtener textos e iniciar la funcion para comprobar la api
    public void loginUser(View view) {
        String userToLogin = user.getText().toString();
        String passToLogin = pass.getText().toString();
        loginToServer(userToLogin,passToLogin);

        chargeBar.setEnabled(true);
        btnLogIn.setEnabled(false);
        user.setEnabled(false);
        pass.setEnabled(false);
        btnRegister.setEnabled(false);
        btnRecoverPass.setEnabled(false);
        btnLogIn.setVisibility(View.GONE);
        chargeBar.setVisibility(View.VISIBLE);
    }

    //Funcion usada para llamar a una funcion dentro de la API la cual obtiene datos y devuelve respuesta
    private void loginToServer(final String name, final String pass){
        mAPIService.loginUser(name,pass).enqueue(new Callback<Get>() {
            //En caso de que la conexion con el servidor sea correcta
            @Override
            public void onResponse(Call<Get> call, Response<Get> response) {
                if (response.body().getCode() == 200){
                    editor.putString("token", response.body().getData().getToken());
                    editor.putString("nameLogged", response.body().getData().getName());
                    Log.d("LOGIN", response.body().getData()+"");
                    editor.commit();
                    changeToLogin();
                }else{
                    motherOfToast(response.body().getMessage());
                }
                enableButtons();
            }
            //En caso de que la conexión con el servidor haya fallado
            @Override
            public void onFailure(Call<Get> call, Throwable t) {
                motherOfToast("No se ha podido establecer conexion con el servidor. Intentelo mas tarde");
                enableButtons();
            }
        });
    }
    //toast function
    public void motherOfToast(String message){
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,50);
        textToast.setText(message);
        toast.setView(layout);
        toast.show();
    }
    //Activacion de botones tras las comprobaciones
    public void enableButtons () {

        chargeBar.setEnabled(false);
        btnLogIn.setEnabled(true);
        user.setEnabled(true);
        pass.setEnabled(true);
        btnRegister.setEnabled(true);
        btnRecoverPass.setEnabled(true);
        btnLogIn.setVisibility(View.VISIBLE);
        chargeBar.setVisibility(View.GONE);
    }
    public void checkLogin(){
        String token = prefs.getString("token", "");
        Log.d("TOKEN", token);
;        if (!token.isEmpty()) {
            mAPIService.defaultAuth(token).enqueue(new Callback<Get>() {
                @Override
                public void onResponse(Call<Get> call, Response<Get> response) {
                    Integer code = response.body().getCode();
                    if (code == 200){

                        changeToLogin();
                    }
                }
                @Override
                public void onFailure(Call<Get> call, Throwable t) {

                }
            });

        }
    }
}
