package com.example.veryhomepage.order;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.veryhomepage.R;
import com.example.veryhomepage.main.Util;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AddressActivity extends AppCompatActivity {
    private EditText tilet_address;
    private Button btn_geoCoder, btnUse;
    private SharedPreferences pref;
//-----定位-----
    private static final int MY_REQUEST_CODE = 0;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final String TAG = "AddressActivity";

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private LatLng latLng;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        address = pref.getString("member_address","");
        findViews();
        setData();
//-----定位-----
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                // 10秒要一次位置資料 (但不一定, 有可能不到10秒, 也有可能超過10秒才要一次)
                .setInterval(10000)
                .setSmallestDisplacement(10);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                lastLocation = locationResult.getLastLocation();
                latLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            }
        };

    }

    private void findViews() {
        tilet_address = findViewById(R.id.tilet_address);
        btn_geoCoder = findViewById(R.id.btn_geoCoder);
        btn_geoCoder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latLng ==null){
                    Util.showToast(AddressActivity.this,"請在試一次");
                    return;
                }
                address = getAddress(latLng);
                tilet_address.setText(address);
            }
        });

        btnUse = findViewById(R.id.btnUse);
        btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("member_address", address);
                intent.putExtras(bundle);
                setResult(Util.RESULT_ADDRESS, intent);
                finish();
            }
        });
    }

    private void setData() {
        String member_address = pref.getString("member_address", "");
        tilet_address.setText(member_address);

    }

    private String getAddress(LatLng myCoordinates) {
//        String myCity = "";
        String address = "";
        Geocoder geocoder = new Geocoder(AddressActivity.this, Locale.getDefault());
        try {
            if (geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1) != null) {
                List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);

//            if(addresses == null) {
//                Toast.makeText(AddressActivity.this, "外送地址: " + address, Toast.LENGTH_SHORT).show();
//                return "";}

                address = addresses.get(0).getAddressLine(0);
//            myCity = addresses.get(0).getLocality();
                Log.d("mylog", "Complete Address: " + addresses.toString());
                Log.d("mylog", "Address: " + address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
    @Override
    protected void onStart() {
        super.onStart();
        // 請求user同意定位
//        askPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocationSettings();
    }

    // 檢查裝置是否開啟Location設定
    private void checkLocationSettings() {
        // 必須將LocationRequest設定加入檢查
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (ActivityCompat.checkSelfPermission(AddressActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // 取得並顯示最新位置
                    showLastLocation();
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ResolvableApiException) {
                    Log.e(TAG, e.getMessage());
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        // 跳出Location設定的對話視窗
                        resolvable.startResolutionForResult(AddressActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
    }
    private void showLastLocation() {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            //取得最新的位置資訊(一次性)
            fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(Task<Location> task) {
                    if (task.isSuccessful()) {
                        lastLocation = task.getResult();
//                        updateLocationInfo(lastLocation);
                    }
                }
            });

            // 持續取得最新位置。looper設為null代表以現行執行緒呼叫callback方法，而非使用其他執行緒
            fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback, null);
        }
    }

//    private void updateLocationInfo(Location lastLocation) {
//        TextView tvLastLocation = findViewById(R.id.tvLastLocation);
//        StringBuilder msg = new StringBuilder();
//        msg.append("自己位置相關資訊 \n");
//
//        if (lastLocation == null) {
//            Toast.makeText(this, getString(R.string.msg_LastLocationNotAvailable), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // 取得定位時間
//        Date date = new Date(lastLocation.getTime());
//        DateFormat dateFormat = DateFormat.getDateTimeInstance();
//        String time = dateFormat.format(date);
//        msg.append("定位時間: " + time + "\n");
//
//        // 取得自己位置的緯經度、精準度、高度、方向與速度，不提供的資訊會回傳0.0
//        msg.append("緯度: " + lastLocation.getLatitude() + "\n");
//        msg.append("經度: " + lastLocation.getLongitude() + "\n");
//        msg.append("精準度(公尺): " + lastLocation.getAccuracy() + "\n");
//        msg.append("高度(公尺): " + lastLocation.getAltitude() + "\n");
//        msg.append("方向(角度): " + lastLocation.getBearing() + "\n");
//        msg.append("速度(公尺/秒): " + lastLocation.getSpeed() + "\n");
//
//        tvLastLocation.setText(msg);
//    }

    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    MY_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        String text = getString(R.string.text_ShouldGrant);
                        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                break;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

}
