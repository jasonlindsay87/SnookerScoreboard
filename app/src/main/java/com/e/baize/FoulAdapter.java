package com.e.baize;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FoulAdapter extends ArrayAdapter<FoulItems> {
    public FoulAdapter(Context context, ArrayList<FoulItems> FoulList) {
        super(context, 0, FoulList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.foul_spinner_layout, parent, false);
        }
        ImageView imageViewFoulBall = convertView.findViewById(R.id.img_foul);


        FoulItems currentItem = getItem(position);

        if (currentItem != null) {
            imageViewFoulBall.setImageResource(currentItem.getFoulImage());
        }

        return convertView;
    }
}
