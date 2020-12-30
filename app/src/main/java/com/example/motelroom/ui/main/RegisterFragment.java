package com.example.motelroom.ui.main;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motelroom.Constant;
import com.example.motelroom.R;
import com.example.motelroom.UserInfoActivity;
import com.example.motelroom.ui.home.HomeFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private RegisterViewModel mViewModel;
    private Button button_dangky;
    private TextView txtdangnhap;
    private EditText edtTaikhoan, edtMatkhau, edtRematkhau, edtEmail;
    private View view;
    private ProgressDialog dialog;

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        // TODO: Use the ViewModel
    }

    private void init() {
        edtEmail = view.findViewById(R.id.edt_email);
        edtTaikhoan = view.findViewById(R.id.edt_taikhoan);
        edtMatkhau = view.findViewById(R.id.edt_matkhau);
        edtRematkhau = view.findViewById(R.id.edt_rematkhau);
//        edtTenhienthi = view.findViewById(R.id.edt_tenhienthi);
        button_dangky = view.findViewById(R.id.btn_dangky);
        txtdangnhap = view.findViewById(R.id.txt_dangnhap);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        txtdangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment LoginFragment = new LoginFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, LoginFragment).commit();
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
//                        new LoginFragment()).commit();
            }
        });

        button_dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validate fields first
                if (validate()) {
                    //do something
                    register();
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

        return true;
    }

    private void register(){
        dialog.setMessage("Rergistering");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.REGISTER, response -> {
            //get response if connection success
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONObject user = object.getJSONObject("user");
                    //make shared Preference user
                    SharedPreferences userPref = getActivity().getApplicationContext().getSharedPreferences("user",getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token", object.getString("token"));
                    editor.putString("name", user.getString("name"));
                    editor.putString("email", user.getString("email"));
                    editor.putString("avatar", user.getString("avatar"));
                    editor.putBoolean("isLoggedIn", true);
                    editor.commit();
                    //if success
                    //move
                    startActivity(new Intent(getActivity(), UserInfoActivity.class));
                    Toast.makeText(getContext(), "register Success, next step", Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            dialog.dismiss();
        }, error -> {
            // error if connection fail
            error.printStackTrace();
            dialog.dismiss();
        }){
            // add parameters
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("txtmail", edtEmail.getText().toString().trim());
                map.put("txtuser", edtTaikhoan.getText().toString().trim());
                map.put("txtpass", edtMatkhau.getText().toString().trim());
                return map;
            }
        };
        //add this request to requestqueue
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }


}
