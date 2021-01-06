package com.example.motelroom.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motelroom.Adapter.PostsAdapter;
import com.example.motelroom.Model.Post;
import com.example.motelroom.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThongTinFragment extends Fragment {

    private ThongTinViewModel mViewModel;
    private View view;
    private CircleImageView circleImageViewAvatar;
    private CircleImageView circleImageViewEdit;
    private TextView txt_TenNguoiDung;
    private ArrayList<Post> arrayList;
    private RecyclerView recyclerView;
    private PostsAdapter postsAdapter;

    public static ThongTinFragment newInstance() {
        return new ThongTinFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.thong_tin_fragment, container, false);
        init();
        return view;
    }

    private void init() {
        circleImageViewAvatar = view.findViewById(R.id.circle_avatar_frThongTin);
        circleImageViewEdit = view.findViewById(R.id.circle_edit_frThongTin);
        txt_TenNguoiDung = view.findViewById(R.id.txt_nguoidung_frthongtin);
        recyclerView = view.findViewById(R.id.recyclerThongTin);

        circleImageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment ChinhSuaHoSo = new ChinhSuaHoSoFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, ChinhSuaHoSo).commit();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ThongTinViewModel.class);
        // TODO: Use the ViewModel
    }



}