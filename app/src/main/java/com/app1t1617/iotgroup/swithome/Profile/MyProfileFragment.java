package com.app1t1617.iotgroup.swithome.Profile;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app1t1617.iotgroup.swithome.R;
import com.app1t1617.iotgroup.swithome.Utils.FilePath;
import com.app1t1617.iotgroup.swithome.Utils.FileStorage;
import com.app1t1617.iotgroup.swithome.Utils.ImagePicker;
import com.app1t1617.iotgroup.swithome.data.model.Post;
import com.app1t1617.iotgroup.swithome.data.remote.APIService;
import com.app1t1617.iotgroup.swithome.data.remote.ApiUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

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
    ImageView profileImage;

    //Permisos
    final int PICK_IMAGE_CODE = 1;

    //Declaración de preferencias
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    //Declaración del servicio
    private APIService mAPIService;

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
        //Obtener de preferencias el dato guardado
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();



        //Toast
        layout = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.toastlinear));
        textToast = (TextView) layout.findViewById(R.id.toastapp);
        toast = new Toast(context);

        username = (TextView) view.findViewById(R.id.profile_username);
        changePhoto = (ImageButton) view.findViewById(R.id.profile_change_photo);
        changeEmail = (ImageButton) view.findViewById(R.id.profile_change_email);
        changePass = (ImageButton) view.findViewById(R.id.profile_change_pass);
        profileImage = (ImageView) view.findViewById(R.id.profile_image);

        //Iniciacion del servicio
        mAPIService = ApiUtils.getAPIService();

        //Load profile image
        loadImage();

        String name = prefs.getString("nameLogged", "No name defined");

        username.setText(name);

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestImagesPermission();
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setContentView(R.layout.alert_change_email);
                final EditText email = (EditText) dialog.findViewById(R.id.profile_email_input);
                final EditText repeatEmail = (EditText) dialog.findViewById(R.id.profile_email_repeat);

                Button cancel = (Button) dialog.findViewById(R.id.cancel_profile_email);
                Button acept = (Button) dialog.findViewById(R.id.accept_change_email);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                acept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkEmail(email, repeatEmail)){
                            callApiUpdateUser(email.getText().toString(), null, null, null, dialog);
                        }
                    }
                });
                dialog.show();
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().setContentView(R.layout.alert_change_pass);
                final EditText oldPass = (EditText) dialog.findViewById(R.id.profile_old_pass_input);
                final EditText newPass = (EditText) dialog.findViewById(R.id.profile_new_pass_input);
                final EditText repeatPass = (EditText) dialog.findViewById(R.id.profile_new_pass_repeat);

                Button cancel = (Button) dialog.findViewById(R.id.cancel_profile_pass);
                Button acept = (Button) dialog.findViewById(R.id.accept_change_pass);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                acept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkRepeat(newPass.getText().toString(), repeatPass.getText().toString())){
                            callApiUpdateUser(null, newPass.getText().toString(), oldPass.getText().toString(), null, dialog);
                        }else{
                            newPass.setError("Las contraseñas deben coincidir");
                            repeatPass.setError("Las contraseñas deben coincidir");
                        }
                    }
                });
                dialog.show();

            }
        });






        return view;
    }

    public Boolean checkEmail(EditText email, EditText repeatEmail){

        if(!checkRepeat(email.getText().toString(), repeatEmail.getText().toString())){
            email.setError("Los email deben coincidir");
            repeatEmail.setError("Los email deben coincidir");
            return false;
        }
        if (!isValidEmail(email.getText().toString())){
            email.setError("El email debe ser válido");
            return false;
        }
        return true;
    }


    public Boolean checkRepeat(String param, String repeatParam){
        if (param.equals(repeatParam)) {
            return true;
        }else{
            return false;
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void callApiUpdateUser(String email, String pass, String passOld, File image, final Dialog dialog){
        MultipartBody.Part fileToUpload = null;
        RequestBody passToUpload = null;
        RequestBody passOldToUpload = null;
        RequestBody emailToUpload = null;
        if (image != null){
            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
            fileToUpload = MultipartBody.Part.createFormData("photo", image.getName(), requestBody);
        }
        if (pass != null){
            passToUpload = RequestBody.create(MediaType.parse("text/plain"), pass);
        }
        if (passOld != null){
            passOldToUpload = RequestBody.create(MediaType.parse("text/plain"), passOld);
        }
        if (email != null){
            emailToUpload = RequestBody.create(MediaType.parse("text/plain"), email);
        }



        mAPIService.updateUser(passToUpload, passOldToUpload, emailToUpload, fileToUpload, prefs.getString("token", "")).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.body().getCode() == 201){
                    editor.putString("token", response.body().getData().getToken());
                    editor.putString("urlPhoto", response.body().getData().getUser().getUrlPhoto());
                    Log.d("URL FOTO", response.body().getData().getUser().getUrlPhoto()+"");
                    editor.commit();
                    motherOfToast(response.body().getMessage());
                    if(dialog != null) {
                        dialog.dismiss();
                    }
                }else{
                    motherOfToast(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d("ERROR", t.getMessage()+"");
                motherOfToast("Fallo de conexión con el servidor");
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


    public void pickImage(){
        Intent intent = ImagePicker.getPickImageIntent(getActivity());
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, PICK_IMAGE_CODE );
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);

            storeImage(bitmap, false);

            profileImage.setImageBitmap(bitmap);

        }
    }

    private void requestImagesPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PICK_IMAGE_CODE);
        } else {
            pickImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PICK_IMAGE_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }

    public void loadImage(){

        String serverFilePath = prefs.getString("urlPhoto", "");
        String filePath = prefs.getString("profileImagePath", "");

        Log.d("CARGA RESULTADO", filePath+"     "+serverFilePath );
        if (filePath != ""){
            Picasso.with(getActivity()).load(Uri.fromFile(new File(filePath))).memoryPolicy(MemoryPolicy.NO_CACHE).into(profileImage);
            Log.d("STORAGE", filePath);
        }else if (serverFilePath != ""){
            Picasso.with(getActivity()).load(serverFilePath).memoryPolicy(MemoryPolicy.NO_STORE).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    storeImage(bitmap, true);
                    profileImage.setImageBitmap(bitmap);

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
            Log.d("URL", serverFilePath);
        }
    }

    public void storeImage(Bitmap image, Boolean fromUrl) {
        File pictureFile = FileStorage.getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("STORE IMAGE ERROR",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            Log.d("STORED", pictureFile.getAbsolutePath());
            if (!fromUrl) {
                callApiUpdateUser(null, null, null, pictureFile, null);
            }
            editor.putString("profileImagePath", pictureFile.getAbsolutePath());
            editor.commit();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("STORE IMAGE ERROR 2", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("STORE IMAGE ERROR 3", "Error accessing file: " + e.getMessage());
        }
    }


}