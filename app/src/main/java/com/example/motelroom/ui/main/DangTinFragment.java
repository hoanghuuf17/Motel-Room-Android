package com.example.motelroom.ui.main;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motelroom.Constant;
import com.example.motelroom.Model.Post;
import com.example.motelroom.Model.User;
import com.example.motelroom.R;
import com.example.motelroom.UserInfoActivity;
import com.example.motelroom.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DangTinFragment extends Fragment implements LocationListener {

    private DangTinViewModel mViewModel;
    private View view;
    private Button btnPost;
    private ImageView imgPost;
    private TextView txtPhoto;
    private ImageButton btnAddPostLocation;
    private EditText txtTitle, txtAddr, txtPrice, txtArea, txtPhone, txtDesc, txtUtilitiesAddPost;
    private static final int GALLERY_ADD_POST = 1;
    private Bitmap bitmap = null;
    int RESULT_OK = -1;
    private ProgressDialog dialog;
    private SharedPreferences userPref;
    LocationManager locationManager;
    public String txtlng,txtlat;
    public int category;
    Spinner spinner;


    public static DangTinFragment newInstance() {
        return new DangTinFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dang_tin_fragment, container, false);
        init();
        return view;
    }

    private void init() {
        userPref = getActivity().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btnPost = view.findViewById(R.id.btn_AddPost);
        imgPost = view.findViewById(R.id.imgAddPost);
        txtTitle = view.findViewById(R.id.txtTitleAddPost);
        txtAddr = view.findViewById(R.id.txtAddrAddPost);
        txtPrice = view.findViewById(R.id.txtPriceAddPost);
        txtArea = view.findViewById(R.id.txtAreaAddPost);
        txtPhone = view.findViewById(R.id.txtPhoneAddPost);
        txtDesc = view.findViewById(R.id.txtDescAddPost);
        txtPhoto = view.findViewById(R.id.txtPhotoAddPost);
        txtUtilitiesAddPost = view.findViewById(R.id.txtUtilitiesAddPost);
        btnAddPostLocation = view.findViewById(R.id.btnAddPostLocation);
        spinner = view.findViewById(R.id.spinnerAddPost);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.categories));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        category = 1;
                        break;
                    case 1:
                        category = 2;
                        break;
                    case 2:
                        category = 3;
                        break;
                    case 3:
                        category = 4;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialog = new ProgressDialog(getContext());
        dialog.setCancelable(false);

        btnAddPostLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        txtPhoto.setOnClickListener(v->{
            Intent i =  new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i,GALLERY_ADD_POST);
        });

        btnPost.setOnClickListener(v->{
            if(validate()){
                //code
                post();
            }
        });

        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtTitle.getText().toString().isEmpty()) {
                    txtTitle.setError("Nhập Tiêu Đề");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtAddr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtAddr.getText().toString().isEmpty()) {
                    txtAddr.setError("Nhập Địa Chỉ");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtPrice.getText().toString().isEmpty()) {
                    txtPrice.setError("Nhập Giá");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtArea.getText().toString().isEmpty()) {
                    txtArea.setError("Nhập Diện Tích");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtPhone.getText().toString().isEmpty()) {
                    txtPhone.setError("Nhập SDT");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtDesc.getText().toString().isEmpty()) {
                    txtDesc.setError("Nhập Mô Tả");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        txtUtilitiesAddPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!txtUtilitiesAddPost.getText().toString().isEmpty()) {
                    txtUtilitiesAddPost.setError("Nhập tiện ích");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5 , DangTinFragment.this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        txtlng = String.valueOf(location.getLongitude());
        txtlat = String.valueOf(location.getLatitude());
        Toast.makeText(getActivity(), ""+location.getLatitude()+ "," + location.getLongitude(), Toast.LENGTH_LONG).show();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses =  geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            txtAddr.setText(address);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void post() {
        dialog.setMessage("Đang đăng tin");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.MOTELROOMS, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONObject postObject = object.getJSONObject("motel");
                    JSONObject userObject = postObject.getJSONObject("user");

                    User user = new User();
                    user.setId(userObject.getInt("id"));
                    user.setName(userObject.getString("name"));
                    user.setPhoto(userObject.getString("avatar"));

                    Post post = new Post();
                    post.setUser(user);
                    post.setId(postObject.getInt("id"));
                    post.setTitle(postObject.getString("title"));
                    post.setPrice(postObject.getString("price"));
                    post.setPhoto(postObject.getString("images"));
                    post.setDate(postObject.getString("created_at"));
                    post.setAddr(postObject.getString("address"));

                    //if success
                    //move
                    Fragment homeFragment = new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commit();

                    Toast.makeText(getContext(), "Đăng tin thành công", Toast.LENGTH_SHORT).show();
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
                map.put("txttitle", txtTitle.getText().toString().trim());
                map.put("txtaddress", txtAddr.getText().toString().trim());
                map.put("txtlat", txtlat);
                map.put("txtlng", txtlng);
                map.put("idcategory", String.valueOf(category));
                System.out.println(txtlat+ "awdwadwd" + txtlng+"cate ne"+category);
                map.put("txtprice", txtPrice.getText().toString().trim());
                map.put("txtutilities", txtUtilitiesAddPost.getText().toString().trim());
                map.put("txtarea", txtArea.getText().toString().trim());
                map.put("txtphone", txtPhone.getText().toString().trim());
                map.put("txtdescription", txtDesc.getText().toString().trim());
                map.put("avatar",bitmapToString(bitmap));
                System.out.println(bitmapToString(bitmap));
                return map;
            }
        };
        //add this request to requestqueue
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

    private boolean validate() {
        if (txtTitle.getText().toString().isEmpty()) {
            txtTitle.setError("Nhập Tiêu đề");
            return false;
        }
        if (txtAddr.getText().toString().isEmpty()) {
            txtAddr.setError("Nhập Địa Chỉ");
            return false;
        }
        if (txtPrice.getText().toString().isEmpty()) {
            txtPrice.setError("Nhập Giá");
            return false;
        }
        if (txtArea.getText().toString().isEmpty()) {
            txtArea.setError("Nhập Diện Tích");
            return false;
        }
        if (txtPhone.getText().toString().isEmpty()) {
            txtPhone.setError("Nhập SDT");
            return false;
        }
        if (txtDesc.getText().toString().isEmpty()) {
            txtDesc.setError("Nhập Mô tả");
            return false;
        }
        if (txtUtilitiesAddPost.getText().toString().isEmpty()) {
            txtUtilitiesAddPost.setError("Nhập Tiêu Đề");
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_ADD_POST && resultCode == RESULT_OK){
            Uri imgUri = data.getData();
            imgPost.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imgUri);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DangTinViewModel.class);
        // TODO: Use the ViewModel
    }


}