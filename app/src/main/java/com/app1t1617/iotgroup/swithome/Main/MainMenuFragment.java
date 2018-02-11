package com.app1t1617.iotgroup.swithome.Main;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.app1t1617.iotgroup.swithome.FastActions.FastActionsFragment;
import com.app1t1617.iotgroup.swithome.Profile.MyProfileFragment;
import com.app1t1617.iotgroup.swithome.R;
import com.app1t1617.iotgroup.swithome.data.model.Get;
import com.app1t1617.iotgroup.swithome.data.remote.APIService;
import com.app1t1617.iotgroup.swithome.data.remote.ApiUtils;
import com.app1t1617.iotgroup.swithome.data.remote.RaspberryAPIService;


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

    private RaspberryAPIService mRaspberryAPIService;

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

        mRaspberryAPIService = ApiUtils.getRaspberryAPIService();

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
                mRaspberryAPIService.switchLight().enqueue(new Callback<Get>() {
                    @Override
                    public void onResponse(Call<Get> call, Response<Get> response) {
                        Log.d("RASPBERRY", response.headers()+"");
                    }
                    @Override
                    public void onFailure(Call<Get> call, Throwable t) {
                        Log.d("RASPBERRY", t+"");
                    }
                });
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
