package com.app1t1617.iotgroup.swithome.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app1t1617.iotgroup.swithome.Devices.DevicesFragment;
import com.app1t1617.iotgroup.swithome.FastActions.FastActionsFragment;
import com.app1t1617.iotgroup.swithome.Profile.MyProfileFragment;
import com.app1t1617.iotgroup.swithome.R;
import com.app1t1617.iotgroup.swithome.Utils.FileStorage;
import com.app1t1617.iotgroup.swithome.data.model.Get;
import com.app1t1617.iotgroup.swithome.data.remote.APIService;
import com.app1t1617.iotgroup.swithome.data.remote.ApiUtils;
import com.app1t1617.iotgroup.swithome.data.remote.RaspberryAPIService;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuFragment extends Fragment {
    public View view;
    public Context context;

    TextView userNameTitle;
    ImageButton myDevices;
    ImageButton myConfig;
    ImageButton fastActions;
    ImageButton myProfile;
    de.hdodenhof.circleimageview.CircleImageView profileImage;
    TextView welcome;

    FragmentTransaction fragmentTransaction;

    //Declaración de preferencias
    SharedPreferences prefs;
    SharedPreferences.Editor editor;



    View layout;
    Toast toast;
    TextView textToast;

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
        myDevices = (ImageButton) view.findViewById(R.id.MyDevices);
        myConfig = (ImageButton) view.findViewById(R.id.MyConfig);
        fastActions = (ImageButton) view.findViewById(R.id.FastActions);
        myProfile = (ImageButton) view.findViewById(R.id.MyProfile);
        welcome = (TextView) view.findViewById(R.id.main_welcome_text);
        profileImage = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.main_profile_image);

        getActivity().setTitle("Inicio");



        //Obtener de preferencias el dato guardado
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        String name = prefs.getString("nameLogged", "No name defined");

        checkFirstLog();

        //Usar ese dato en el sistema
        userNameTitle.setText(name);

        fragmentTransaction = getFragmentManager().beginTransaction();

        //Toast
        layout = getLayoutInflater().inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.toastlinear));
        textToast = (TextView) layout.findViewById(R.id.toastapp);
        toast = new Toast(context);

        myDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                fragmentTransaction.replace(R.id.main_frame_layout, new DevicesFragment()).addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        myConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motherOfToast("Vista en construcción");
            }
        });

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.main_frame_layout, new MyProfileFragment()).addToBackStack(null);
                fragmentTransaction.commit();
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

    public void checkFirstLog(){
        Boolean firstLog = prefs.getBoolean("firstLog", true);

        if (firstLog){
            profileImage.setVisibility(View.GONE);
            welcome.setVisibility(View.VISIBLE);
            editor.putBoolean("firstLog", false);
            editor.commit();
        }else{
            profileImage.setVisibility(View.VISIBLE);
            welcome.setVisibility(View.GONE);
            loadImage();
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
            Picasso.with(getActivity()).load(serverFilePath).memoryPolicy(MemoryPolicy.NO_CACHE).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    storeImage(bitmap);
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

    public void storeImage(Bitmap image) {
        File pictureFile = FileStorage.getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("STORE IMAGE ERROR",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            editor.putString("profileImagePath", pictureFile.getAbsolutePath());
            editor.commit();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("STORE IMAGE ERROR 2", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("STORE IMAGE ERROR 3", "Error accessing file: " + e.getMessage());
        }
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
