package com.app1t1617.iotgroup.swithome.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app1t1617.iotgroup.swithome.R;

/**
 * Created by chema.dev on 11/2/18.
 */

public class MyProfileFragment extends Fragment {
    View view;
    Context context;

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
        view = inflater.inflate(R.layout.fragment_provisional_profile, container, false);

        return view;
    }
}
