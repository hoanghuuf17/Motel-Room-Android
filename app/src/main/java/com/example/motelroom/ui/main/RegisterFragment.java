package com.example.motelroom.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.motelroom.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private Button button_dangky;
//    private LinearLayout layoutTaikhoan, layoutMatkhau, layoutRematkhau, layoutEmail, layoutTenhienthi;
    private TextView txtdangnhap;
    private EditText edtTaikhoan, edtMatkhau, edtRematkhau, edtEmail, edtTenhienthi;
    private View view;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_fragment, container, false);
        init();
        return view;
    }

    private void init() {
//        layoutTaikhoan = view.findViewById(R.id.edt_taikhoan);
//        layoutMatkhau = view.findViewById(R.id.edt_matkhau);
//        layoutRematkhau = view.findViewById(R.id.edt_rematkhau);
//        layoutEmail = view.findViewById(R.id.edt_email);
//        layoutTenhienthi = view.findViewById(R.id.edt_tenhienthi);
        edtEmail = view.findViewById(R.id.edt_email);
        edtTaikhoan = view.findViewById(R.id.edt_taikhoan);
        edtMatkhau = view.findViewById(R.id.edt_matkhau);
        edtRematkhau = view.findViewById(R.id.edt_rematkhau);
        edtTenhienthi = view.findViewById(R.id.edt_tenhienthi);
        button_dangky = view.findViewById(R.id.btn_dangky);
        txtdangnhap = view.findViewById(R.id.txt_dangnhap);

        txtdangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new LoginFragment()).commit();
            }
        });

        button_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate fields first
                if (validate()) {
                    //do something
                }
            }

        });

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtEmail.getText().toString().isEmpty()) {
                    edtEmail.setError("Nhập Email");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtMatkhau.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtMatkhau.getText().toString().length() < 7) {
                    edtMatkhau.setError("Mật khẩu phải từ 8 kí tự");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtRematkhau.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtRematkhau.getText().toString().equals(edtMatkhau.getText().toString())) {
                    edtRematkhau.setError("Mật khẩu không khớp");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtTenhienthi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtTenhienthi.getText().toString().isEmpty()) {
                    edtTenhienthi.setError("Nhập tên hiển thị");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtTaikhoan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtTaikhoan.getText().toString().isEmpty()) {
                    edtTaikhoan.setError("Nhập tên tài khoản");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    private boolean validate() {
        if (edtEmail.getText().toString().isEmpty()) {
            edtEmail.setError("Nhập Email");
            return false;
        }
        if (edtTaikhoan.getText().toString().isEmpty()) {
            edtTaikhoan.setError("Nhập Tên tài khoản");
            return false;
        }
        if (edtMatkhau.getText().toString().length() < 8) {
            edtMatkhau.setError("Mật khẩu phải từ 8 kí tự");
            return false;
        }
        if (!edtRematkhau.getText().toString().equals(edtMatkhau.getText().toString())) {
            edtRematkhau.setError("Mật khẩu không khớp");
            return false;
        }
        if (edtTenhienthi.getText().toString().isEmpty()) {
            edtTenhienthi.setError("Nhập tên hiển thị");
            return false;
        }

        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        // TODO: Use the ViewModel
    }


}
