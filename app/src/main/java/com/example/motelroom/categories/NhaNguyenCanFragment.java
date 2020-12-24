package com.example.motelroom.categories;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.motelroom.MainActivity;
import com.example.motelroom.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NhaNguyenCanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NhaNguyenCanFragment extends Fragment {

    public  NhaNguyenCanFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Nhà Nguyên Căn");
        return inflater.inflate(R.layout.fragment_nha_nguyen_can, container, false);
    }
}