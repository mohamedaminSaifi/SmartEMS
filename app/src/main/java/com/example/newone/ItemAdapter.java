package com.example.newone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<item> {public ItemAdapter(Context context, ArrayList<item> items) {
    super(context, 0, items);
}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        item currentItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        //here add the rest of the info
        TextView textNumber = convertView.findViewById(R.id.text_number);
        TextView textStatus = convertView.findViewById(R.id.text_status);

        textNumber.setText(String.valueOf(currentItem.getNumber()));
        textStatus.setText(currentItem.getStatus());

        return convertView;
    }
}