package com.example.motelroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motelroom.ui.home.HomeFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPost extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    private int position = 0, id = 0;
    private CircleImageView imgProfileDetail;
    private TextView txtNameDetail, txtJoinDateDetail, txtTitleDetail, txtPriceDetail, txtViewDetail, txtDateDetail
            ,txtAddressDetail, txtAreaDetail, txtUtilitiesDetail, txtDescDetail, txtPhoneDetail;
    private ImageView imgReportDetail, imgImageDetail;
    public int status;
    public String ip;
    private ProgressDialog dialog;
    public double lat, lng;
    private SharedPreferences userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        init();
        id = getIntent().getIntExtra("postId", 0);
        getContent(id);
    }

    private void init() {
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        imgProfileDetail = findViewById(R.id.imgProfileDetail);
        txtNameDetail = findViewById(R.id.txtNameDetail);
        txtJoinDateDetail = findViewById(R.id.txtJoinDateDetail);
        txtTitleDetail = findViewById(R.id.txtTitleDetail);
        txtPriceDetail = findViewById(R.id.txtPriceDetail);
        txtViewDetail = findViewById(R.id.txtViewDetail);
        txtDateDetail = findViewById(R.id.txtDateDetail);
        txtAddressDetail = findViewById(R.id.txtAddressDetail);
        txtAreaDetail = findViewById(R.id.txtAreaDetail);
        txtUtilitiesDetail = findViewById(R.id.txtUtilitiesDetail);
        txtDescDetail = findViewById(R.id.txtDescDetail);
        txtPhoneDetail = findViewById(R.id.txtPhoneDetail);
        imgReportDetail = findViewById(R.id.imgReportDetail);
        imgImageDetail = findViewById(R.id.imgImageDetail);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        ip = info.getMacAddress();
        System.out.println(ip);

        position = getIntent().getIntExtra("postion", 0);

        Toast.makeText(this, "ok "+id, Toast.LENGTH_SHORT).show();

        imgReportDetail.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(this, imgReportDetail);
            popupMenu.inflate(R.menu.menu_report_option);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.leased:
                            System.out.println("da cho thue");
                            status = 1;
                            report(status);
                            break;

                        case R.id.wrong:
                            status = 2;
                            report(status);
                            System.out.println("sai thong tin");
                            break;
                    }
                    return false;
                }
            });
            popupMenu.show();
        });
    }

    private void getContent(int id) {
        StringRequest request = new StringRequest(Request.Method.GET, Constant.DETAILROOM+id, response -> {
            //get response if connection success
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONObject roomObject = object.getJSONObject("room");
                    JSONObject userObject = roomObject.getJSONObject("user");

                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("lat", roomObject.getString("lat"));
                    editor.putString("lng", roomObject.getString("lng"));
                    editor.apply();

                    txtNameDetail.setText(userObject.getString("name"));
                    txtJoinDateDetail.setText(userObject.getString("timejoin"));
                    txtTitleDetail.setText(roomObject.getString("title"));
                    txtPriceDetail.setText(roomObject.getString("price"));
                    txtViewDetail.setText(roomObject.getString("count_view"));
                    txtDateDetail.setText(roomObject.getString("timepost"));
                    txtAddressDetail.setText(roomObject.getString("address"));
                    txtAreaDetail.setText(roomObject.getString("area"));
                    txtUtilitiesDetail.setText(roomObject.getString("utilities"));
                    txtDescDetail.setText(roomObject.getString("description"));
                    txtPhoneDetail.setText(roomObject.getString("phone"));
                    Picasso.get().load(Constant.URL+"uploads/images/"+roomObject.getString("images")).placeholder(R.drawable.user).into(imgImageDetail);
                    Picasso.get().load(Constant.URL+"uploads/avatars/"+userObject.getString("avatar")).placeholder(R.drawable.user).into(imgProfileDetail);
                    System.out.println("okey");
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

        };
        //add this request to requestqueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void report(int status) {
        StringRequest request = new StringRequest(Request.Method.POST, Constant.REPORT+status, response -> {
            //get response if connection success
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    //move
                    Toast.makeText(this, "Gửi báo cáo thành công, cảm ơn bạn", Toast.LENGTH_SHORT).show();
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
                map.put("id", String.valueOf(id));
                map.put("ipaddress", String.valueOf(ip));
                map.put("status", String.valueOf(status));
                return map;
            }
        };
        //add this request to requestqueue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        lat = Double.parseDouble(userPref.getString("lat","0"));
        lng = Double.parseDouble(userPref.getString("lng","0"));
        System.out.println(lat+"fadwadaw"+lng);
        map = googleMap;
        LatLng location = new LatLng(lat,lng);
        map.addMarker(new MarkerOptions().position(location).title("HOme"));
        map.moveCamera(CameraUpdateFactory.newLatLng((location)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }
}