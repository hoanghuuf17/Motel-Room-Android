package com.example.motelroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class UserInfoActivity extends AppCompatActivity {

    private TextView txt_selectPhoto;
    private EditText txt_NameUserInfo;
    private Button btn_UserInfo;
    private CircleImageView circleImageView;
    private static final int GALLERY_AND_PROFILE = 1;
    private Bitmap bitmap = null;
    private SharedPreferences userPref;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        init();
    }

    public void init(){
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        txt_selectPhoto = findViewById(R.id.txtSelectPhoto);
        txt_NameUserInfo = findViewById(R.id.txt_NameUserInfo);
        btn_UserInfo = findViewById(R.id.btn_UserInfo);
        circleImageView = findViewById(R.id.imgUserInfo);

        txt_selectPhoto.setOnClickListener(v->{
            System.out.println("ok");
            Intent i =  new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i,GALLERY_AND_PROFILE);
        });
        btn_UserInfo.setOnClickListener(v->{
            if(validate()){
                //code
                saveUserInfo();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

    private boolean validate(){
        if (txt_NameUserInfo.getText().toString().isEmpty()) {
            txt_NameUserInfo.setError("Nhập Tên");
            return false;
        }
        return true;
    }

    private void saveUserInfo(){
        dialog.setMessage("Đang lưu");
        dialog.show();
        String name = txt_NameUserInfo.getText().toString().trim();
        StringRequest request= new StringRequest(Request.Method.POST, Constant.SAVE_USER_INFO, response -> {
            try {
                System.out.println(response);
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    //code
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("avatar", object.getString("avatar"));
                    editor.putString("name", object.getString("name"));
                    editor.apply();
                    startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
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
                map.put("name", name);
                map.put("avatar",bitmapToString(bitmap));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(UserInfoActivity.this);
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

}