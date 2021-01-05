package com.example.motelroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChinhSuaHoSoActivity extends Activity {

    private static final int GALLERY_AND_PROFILE = 1;
    private TextView txt_SelectPhoto_Edit;
    private EditText edt_tennguoidung, edt_matkhau, edt_nhaplaimatkhau;
    private Button btnChinhSua;
    private Bitmap bitmap = null;
    private SharedPreferences userEdit;
    private ProgressDialog dialog;
    private CircleImageView circleImageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinh_sua_ho_so_fragment);
        init();
    }

    private void init() {
        txt_SelectPhoto_Edit = findViewById(R.id.txtSelectPhoto_Edit);
        edt_tennguoidung = findViewById(R.id.edt_EditTenHienThi);
        edt_matkhau = findViewById(R.id.edt_EditMatkhau);
        edt_nhaplaimatkhau = findViewById(R.id.edt_EditMatkhau);


        txt_SelectPhoto_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("ok");
                Intent i =  new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,GALLERY_AND_PROFILE);
            }
        });
        btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    //code
                    saveUserEdit();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_AND_PROFILE && resultCode == RESULT_OK){
            Uri imgUri = data.getData();
            circleImageView.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void saveUserEdit() {
        dialog.setMessage("Đang lưu");
        dialog.show();
        String name = edt_tennguoidung.getText().toString().trim();
        String matkhau = edt_matkhau.getText().toString().trim();
        String nhaplaimk = edt_nhaplaimatkhau.getText().toString().trim();
        StringRequest request= new StringRequest(Request.Method.POST, Constant.SAVE_USER_INFO, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    //code
                    SharedPreferences.Editor editor = userEdit.edit();
                    editor.putString("avatar", object.getString("avatar"));
                    editor.putString("name", object.getString("name"));
                    editor.putString("password", object.getString("password"));
                    editor.putString("repassword", object.getString("repassword"));

                    editor.apply();

                    //move
                    startActivity(new Intent(ChinhSuaHoSoActivity.this, MainActivity.class));
                    finish();
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
                String token = userEdit.getString("token",null);
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer "+token);
                System.out.println(token);
                return map;
            }
            //add params

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("avatar",bitmapToString(bitmap));
                return map;
            }

            private String bitmapToString(Bitmap bitmap) {
                if (bitmap != null){
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100,byteArrayOutputStream);
                    byte[] array = byteArrayOutputStream.toByteArray();
                    return Base64.encodeToString(array, Base64.DEFAULT);
                }
                return "";
            }
        };
        RequestQueue queue = Volley.newRequestQueue(ChinhSuaHoSoActivity.this);
        queue.add(request);
    }

    private boolean validate() {
        if (edt_tennguoidung.getText().toString().isEmpty()) {
            edt_tennguoidung.setError("Nhập Tên Người Dùng");
            return false;
        }
        if (edt_matkhau.getText().toString().length() < 8) {
            edt_matkhau.setError("Mật khẩu phải từ 8 kí tự");
            return false;
        }
        if (!edt_nhaplaimatkhau.getText().toString().equals(edt_matkhau.getText().toString())) {
            edt_nhaplaimatkhau.setError("Mật khẩu không khớp");
            return false;
        }
        return true;
    }
}
