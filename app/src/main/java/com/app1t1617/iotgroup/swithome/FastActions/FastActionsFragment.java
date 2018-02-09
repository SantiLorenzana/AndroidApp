package com.app1t1617.iotgroup.swithome.FastActions;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app1t1617.iotgroup.swithome.Main.MainMenuFragment;
import com.app1t1617.iotgroup.swithome.R;

/**
 * Created by chema.dev on 9/2/18.
 */

public class FastActionsFragment extends Fragment {
    View view;
    Context context;
    AppCompatActivity mActivity;

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
        mActivity = (AppCompatActivity) getActivity();

        return view;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
