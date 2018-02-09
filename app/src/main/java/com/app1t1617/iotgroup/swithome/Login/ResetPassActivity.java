package com.app1t1617.iotgroup.swithome.Login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app1t1617.iotgroup.swithome.R;
import com.app1t1617.iotgroup.swithome.data.model.Get;
import com.app1t1617.iotgroup.swithome.data.model.Post;
import com.app1t1617.iotgroup.swithome.data.remote.APIService;
import com.app1t1617.iotgroup.swithome.data.remote.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassActivity extends AppCompatActivity {

    EditText user;
    EditText email;
    EditText userInputPass;
    EditText userInputPass2;
    ProgressBar chargeBar;
    private String tokenTemp;
    private String passTemp;
    Button recoverbutton;
    Boolean validation;
    //pass popup
    EditText newPass;
    EditText passDoubleCheck;
    //Declaración del servicio
    private APIService mAPIService;
    //Toast
    View layout;
    Toast toast;
    TextView textToast;
    TextView textToastCosa;
    //TextInputs
    String firstPass;
    String checkPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        //Relacion de variables con id
        user = (EditText) findViewById(R.id.userRecover);
        email = (EditText) findViewById(R.id.emailRecover);
        chargeBar = (ProgressBar) findViewById(R.id.progressBarRecover);
        recoverbutton = (Button) findViewById(R.id.buttonRecover);
        //Inicialización de servicio
        mAPIService = ApiUtils.getAPIService();
        //Toast
        layout = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastlinear));
        textToastCosa = (TextView)layout.findViewById(R.id.toastapp);
        toast = new Toast(this);
        textToast = (TextView) findViewById(R.id.toastapp);
    }

    //Funcuon para finalizar la actividad y volver al login
    public void returnToIndex(View view) {
        finish();
    }

    //Funcion para guardar los datos e iniciar la funcion que encadena la API
    public void resetUser(View view) {
        String userToReset = user.getText().toString();
        String passToReset = email.getText().toString();
        resetToServer(userToReset, passToReset);

        chargeBar.setEnabled(true);
        recoverbutton.setEnabled(false);
        user.setEnabled(false);
        email.setEnabled(false);
        chargeBar.setVisibility(View.VISIBLE);
        recoverbutton.setVisibility(View.GONE);
    }

    //Funcion para guardar los datos e iniciar la funcion dentro de la api
    private void resetToServer(final String name, final String pass) {
        mAPIService.recoverUser(name, pass).enqueue(new Callback<Get>() {
            //En caso de que la conexion con el servidor sea correcta (Funcion recoger token y guardarlo)
            @Override
            public void onResponse(Call<Get> call, final Response<Get> response) {
                if (response.body().getData() == null) {
                    motherOfToast(response.body().getMessage());
                    enableButtons();
                } else {
                    View view = (LayoutInflater.from(ResetPassActivity.this)).inflate(R.layout.user_input, null);
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ResetPassActivity.this);
                    alertBuilder.setView(view);
                    userInputPass = (EditText) view.findViewById(R.id.userinput);
                    userInputPass2 = (EditText) view.findViewById(R.id.userinput2);
                    changeToken(response.body().getData().getToken(), String.valueOf(userInputPass.getText()));
                    Dialog dialog = alertBuilder.create();
                    dialog.show();
                }
            }
            //En caso de que la conexión con el servidor haya fallado
            @Override
            public void onFailure(Call<Get> call, Throwable t) {
                motherOfToast("No se ha podido establecer conexion con el servidor. Intentelo mas tarde");
            }
        });
    }

    //Funcion para actualizar los datos introducidos en el POPUP
    protected void changeToken(String token, String newPass){
        tokenTemp = token;
        passTemp = newPass;
    }



    //Funcion actualizar campos dentro de la API
    public void finalUpdate (View view) {
        checkInputs();
        if (validation == true) {
            mAPIService.updatePass(userInputPass.getText().toString(), tokenTemp).enqueue(new Callback<Post>() {
                //En caso de que la conexion con el servidor sea correcta (Funcion enviar el token y contraseña nueva)
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if (response.isSuccessful()) {
                        motherOfToast(response.body().toString());
                        if (response.body().toString().equals("contraseña actualizada")) {
                            finish();
                        }
                    } else {
                        motherOfToast("Fallo de conexión con el servidor");
                    }
                    enableButtons();
                }

                //En caso de que la conexion con el servidor haya fallado
                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    motherOfToast("No se ha podido establecer conexion con el servidor. Intentelo mas tarde");
                    enableButtons();
                }
            });
        }
    }
    //Checkea el popup
    public void checkInputs(){
        firstPass = userInputPass.getText().toString();
        checkPass = userInputPass2.getText().toString();

        if (firstPass.length() < 6){
            validation = false;
            userInputPass.setError("La nueva contraseña debe tener al menos 6 caracteres");
        } else{
            validation = false;
        }

        if (firstPass.equals(checkPass)){
            validation = true;
        } else{
            userInputPass2.setError("Las contraseñas deben coincidir");
            validation = false;
        }
    }

    public void motherOfToast(String message){
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,50);
        textToastCosa.setText(message);
        toast.setView(layout);
        toast.show();
    }
    public void enableButtons () {
        chargeBar.setEnabled(false);
        recoverbutton.setEnabled(true);
        user.setEnabled(true);
        email.setEnabled(true);
        chargeBar.setVisibility(View.GONE);
        recoverbutton.setVisibility(View.VISIBLE);
    }
}
