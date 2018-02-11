package com.app1t1617.iotgroup.swithome.Main;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app1t1617.iotgroup.swithome.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chema.dev on 8/2/18.
 */

public class LateralMenuAdapter extends ArrayAdapter {

    String [] itemList;

    public LateralMenuAdapter(@NonNull Context context, int resource, String[] itemList) {
        super(context, resource, itemList);
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.lateral_menu_adapter, parent, false);

        String listItem = itemList[position];


        TextView item = (TextView) view.findViewById(R.id.lateral_menu_item);

        if (position == itemList.length-1){
            item.setTextColor(Color.parseColor("#DA041E"));
        }


        item.setText(listItem);

        return view;
    }
}
