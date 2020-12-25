package com.example.motelroom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.motelroom.ui.main.MainLoginFragment;

public class MainLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainLoginFragment.newInstance())
                    .commitNow();
        }
    }
}