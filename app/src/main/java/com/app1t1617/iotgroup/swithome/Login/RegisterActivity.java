package com.app1t1617.iotgroup.swithome.Login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app1t1617.iotgroup.swithome.R;
import com.app1t1617.iotgroup.swithome.data.model.Post;
import com.app1t1617.iotgroup.swithome.data.remote.APIService;
import com.app1t1617.iotgroup.swithome.data.remote.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextView btnReturnIndex;
    EditText userR;
    EditText passR;
    EditText passRDoubleCheck;
    EditText emailR;
    Button btnRegister;
    ProgressBar chargeBar;

    //Declaración del servicio
    private APIService mAPIService;
    //Toast
    View layout;
    Toast toast;
    TextView textToast;
    TextView textToastCosa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Relacion de variables con id
        userR = (EditText) findViewById(R.id.userRegister);
        passR = (EditText) findViewById(R.id.passRegister);
        emailR = (EditText) findViewById(R.id.emailRegister);
        passRDoubleCheck = (EditText) findViewById(R.id.passRegisterDC);
        chargeBar = (ProgressBar) findViewById(R.id.progressBarRegister);
        btnReturnIndex = (TextView) findViewById(R.id.returnToLogin);
        btnRegister = (Button) findViewById(R.id.buttonRegist);
        //Iniciacion del servicio
        mAPIService = ApiUtils.getAPIService();
        //Toast
        layout = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastlinear));
        textToastCosa = (TextView)layout.findViewById(R.id.toastapp);
        toast = new Toast(this);
        textToast = (TextView) findViewById(R.id.toastapp);
    }

    //Funcion a la que se llama para comprobar la intergridad de los datos que se han puesto en el formulario
    //Como numero de caracteres minimos, contraseñas coinciden...
    public void checkData(View view){
        Boolean validation;
        chargeBar.setEnabled(true);
        btnRegister.setEnabled(false);
        userR.setEnabled(false);
        passR.setEnabled(false);
        passRDoubleCheck.setEnabled(false);
        btnReturnIndex.setEnabled(false);
        btnRegister.setVisibility(View.GONE);
        chargeBar.setVisibility(View.VISIBLE);

        String passOriginal = passR.getText().toString();
        String passChecked = passRDoubleCheck.getText().toString();
        String name = userR.getText().toString();
        String email = emailR.getText().toString();

        validation = validateParams(name, email, passOriginal, passChecked);

        if (validation == false) {
           enableButtons();
        }

        //En caso de que to do funcione correctamente llamará a la funcion de relacion con el servidor
        if (validation == true) {
            registerNew(name, passOriginal, email);
        }
    }

    //Funcion finalizar actividad y volver a la anterior (login)
    public void returnToIndex(View view){
        finish();
    }

    //Funcion que recive los datos para ejercutar funcion dentro de la API
    protected void registerNew(String name, String pass, String email) {



        mAPIService.registerUser(name, pass, email).enqueue(new Callback<Post>() {
            //En caso de que la conexion al servidor haya sido buena
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.isSuccessful()) {
                    motherOfToast(response.body().toString());
                    //Si la respuesta es que se ha creado un usuario, se finaliza la actividad y se vuelve al login
                    if (response.body().getCode() == 201) {
                        thread.start();
                    }
                    enableButtons();
                }
            }
            //En caso de que la conexion al servidor haya fallado
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                motherOfToast("No se ha podido establecer conexion con el servidor. Intentelo mas tarde");
                enableButtons();
            }
        });
    }

    public void motherOfToast(String message){
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,50);
        textToastCosa.setText(message);
        toast.setView(layout);
        toast.show();
    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(3000); // As I am using LENGTH_LONG in Toast
                RegisterActivity.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    public void enableButtons () {

        chargeBar.setEnabled(false);
        btnRegister.setEnabled(true);
        userR.setEnabled(true);
        passR.setEnabled(true);
        passRDoubleCheck.setEnabled(true);
        btnReturnIndex.setEnabled(true);
        btnRegister.setVisibility(View.VISIBLE);
        chargeBar.setVisibility(View.GONE);


    }

    public boolean validateParams(String name, String email, String passOriginal, String passChecked){
        Boolean nameValidation;
        Boolean emailValidation;
        Boolean passValidation;
        Boolean passCheckedValidation;

        if (name.length() < 1){
            userR.setError("No puede estar vacío");
            nameValidation = false;
        } else {
            nameValidation = true;
        }

        if (email.length() < 1){
            emailR.setError("No puede estar vacio");
            emailValidation = false;
        } else {
            emailValidation = true;
        }

        if (passOriginal.length() < 6){
            passR.setError("Tiene que tener al menos 6 caracteres");
            passValidation = false;
        } else {
            passValidation = true;
        }

        if  (passOriginal.equals(passChecked)) {
            passCheckedValidation = true;
        } else {
            passR.setError("Las contraseñas deben coincidir");
            passRDoubleCheck.setError("Las contraseñas deben coincidir");
            passCheckedValidation = false;
        }
        if (nameValidation && emailValidation && passValidation && passCheckedValidation){
            return true;
        }else{
            return  false;
        }
    }
}
