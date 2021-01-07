package com.example.motelroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailPost extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    private int position = 0, id = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        init();
    }

    private void init() {
        position = getIntent().getIntExtra("postion", 0);
        id = getIntent().getIntExtra("postId", 0);
        Toast.makeText(this, "ok "+id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng location = new LatLng(15.982493333333332,108.23668333333332);
        map.addMarker(new MarkerOptions().position(location).title("HOme"));
        map.moveCamera(CameraUpdateFactory.newLatLng((location)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }
}