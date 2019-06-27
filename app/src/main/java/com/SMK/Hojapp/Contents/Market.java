package com.SMK.Hojapp.Contents;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.SMK.Hojapp.R;

public class Market extends Fragment {

    public Market() {
        // Gerekli Boşluklar
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Düzen
        return inflater.inflate(R.layout.activity_time_line, container, false);
    }

}