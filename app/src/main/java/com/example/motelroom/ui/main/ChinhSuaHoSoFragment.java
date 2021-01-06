package com.example.motelroom.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.motelroom.R;

public class ChinhSuaHoSoFragment extends Fragment {

    private ChinhSuaHoSoViewModel mViewModel;

    public static ChinhSuaHoSoFragment newInstance() {
        return new ChinhSuaHoSoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chinh_sua_ho_so_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChinhSuaHoSoViewModel.class);
        // TODO: Use the ViewModel
    }

}