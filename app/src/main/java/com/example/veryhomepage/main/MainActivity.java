package com.example.veryhomepage.main;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.veryhomepage.R;
import com.example.veryhomepage.member.LoginActivity;
import com.example.veryhomepage.order.InstantDeliveryOrderVO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {
    private final static int REQ_PERMISSIONS = 0;
    private static final int REQUEST_LOGIN = 1;
    private BottomNavigationView bottomNav;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onLogin();
        SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String deliveryingOrder_json = pref.getString("successOrder","");
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ssZ").create();
        InstantDeliveryOrderVO deliveryingOrder = gson.fromJson(deliveryingOrder_json, InstantDeliveryOrderVO.class);
        Util.D_ORDER = deliveryingOrder;


        bottomNav = findViewById(R.id.bottomNav);
        NavController controller = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(bottomNav, controller);
        askPermissions();
    }

    private void enableBottomBar(boolean enable){
        for (int i = 0; i < bottomNav.getMenu().size(); i++) {
            bottomNav.getMenu().getItem(i).setEnabled(enable);
        }
    }
    private void onLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, REQUEST_LOGIN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int result = ContextCompat.checkSelfPermission(this, permissions[0]);
        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    REQ_PERMISSIONS);
        }
    }
}
