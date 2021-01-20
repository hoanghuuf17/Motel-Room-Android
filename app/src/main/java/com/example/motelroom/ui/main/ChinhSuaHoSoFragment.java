package com.example.motelroom.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motelroom.Constant;
import com.example.motelroom.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChinhSuaHoSoFragment extends Fragment {

    private ChinhSuaHoSoViewModel mViewModel;
    private EditText edt_username, edt_password, edt_repassword;
    private Button btn_edit;
    private ImageView img_change_photo;
    private CircleImageView circle_Fr_Chinh_Sua;
    private View view;
    private SharedPreferences userPref;
    private static final int GALLERY_AND_PROFILE = 1;
    public int RESULT_OK = -1;
    private Bitmap bitmap = null;
    private ProgressDialog dialog;


    public static ChinhSuaHoSoFragment newInstance() {
        return new ChinhSuaHoSoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chinh_sua_ho_so_fragment, container, false);
        init();
        return view;
    }

    private void init() {
        edt_username = view.findViewById(R.id.edt_EditTenHienThi);
        edt_password = view.findViewById(R.id.edt_EditMatkhau);
        edt_repassword = view.findViewById(R.id.edt_NhapLaiMK);
        img_change_photo = view.findViewById(R.id.change_phto_chinhsua);
        btn_edit = view.findViewById(R.id.btn_ChinhSua);
        circle_Fr_Chinh_Sua = view.findViewById(R.id.circle_Fr_Chinh_Sua);
        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);
        userPref = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        edt_username.setText(userPref.getString("name", null));
        String avatar = userPref.getString("avatar", null);
        Picasso.get().load(Constant.URL+"uploads/avatars/"+avatar).placeholder(R.drawable.user).into(circle_Fr_Chinh_Sua);


        img_change_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ok");
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, GALLERY_AND_PROFILE);
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    updateUser();
                }

                edt_username.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (!edt_username.getText().toString().isEmpty()) {
                            edt_username.setError("Nhập tên người dùng");
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            private boolean validate() {
                if (edt_username.getText().toString().isEmpty()) {
                    edt_username.setError("Nhập tên người dùng");
                    return false;
                }
                return true;
            }
        });

    }

    private void updateUser() {
        dialog.setMessage("Đang lưu");
        dialog.show();
        StringRequest request= new StringRequest(Request.Method.PUT, Constant.UPDATEUSER, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    //code
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("avatar", object.getString("avatar"));
                    editor.putString("name", object.getString("name"));
                    editor.apply();

                    //move
                    Fragment ThongTinFragment = new ThongTinFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, ThongTinFragment).commit();
                   }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }, error -> {
            error.printStackTrace();
            dialog.dismiss();
        }){
            //add token to header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token",null);
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer "+token);
                System.out.println(token);
                return map;
            }
            //add params

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", edt_username.getText().toString().trim());
                map.put("password", edt_repassword.getText().toString().trim());
                map.put("avatar",bitmapToString(bitmap));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    private String bitmapToString(Bitmap bitmap){
        if (bitmap != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.DEFAULT);
        }
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_AND_PROFILE && resultCode == RESULT_OK){
            Uri imgUri = data.getData();
            circle_Fr_Chinh_Sua.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imgUri);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChinhSuaHoSoViewModel.class);
        // TODO: Use the ViewModel
    }

}