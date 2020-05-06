package com.example.veryhomepage.order;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.veryhomepage.R;
import com.example.veryhomepage.main.MainActivity;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.model.ChatMessage;
import com.example.veryhomepage.order.directionhelpers.FetchURL;
import com.example.veryhomepage.order.directionhelpers.TaskLoadedCallback;
import com.example.veryhomepage.qrcode.Contents;
import com.example.veryhomepage.qrcode.QRCodeEncoder;
import com.example.veryhomepage.ws.Utils;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.example.veryhomepage.ws.Utils.showToast;

public class DeliveryActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private static final int MY_REQUEST_CODE = 0;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final String LOG_TAG = "QRCodeGenerator";
    private static final String TAG = "BasicMapActivity";
    // QRcode
    private ImageView ivCode;
    //  估狗圖
    private GoogleMap map;
    //  定位區
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private LatLng latLng;
    private Integer count = 0;
    //  導航區
    private Button btnDirect;
    private MarkerOptions place2;
    private Polyline currentPolyline;
    //  websocket區
    private LocalBroadcastManager broadcastManager;

    private void findViews() {
        btnDirect = findViewById(R.id.btnDirect);
        btnDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(DeliveryActivity.this).execute(getUrl(latLng, place2.getPosition(), "driving"), "driving");
            }
        });
        place2 = new MarkerOptions().position(new LatLng(25.091075, 121.559834)).title("Location 2");
        ivCode = findViewById(R.id.ivCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        if(Util.D_ORDER == null){
            Intent intent =new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        findViews();
        //  定位區
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                // 10秒要一次位置資料 (但不一定, 有可能不到10秒, 也有可能超過10秒才要一次)
                .setInterval(10000);
//                .setSmallestDisplacement(1000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                lastLocation = locationResult.getLastLocation();
                latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

                //確認位子有取到 所以在位子生成後在去用target()

//                Log.d(TAG, "onLocationResult:緯度"+lastLocation.getLatitude()+"經度+"+lastLocation.getLongitude());
                updateLocationInfo(lastLocation);

                if (count == 0) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            // 鏡頭焦點在自己位子上
                            .target(latLng)
                            //地圖縮放層級定為7
                            .zoom(15)
                            .build();
                    // 改變鏡頭焦點到指定的新地點
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    map.animateCamera(cameraUpdate);
                    count++;
                }
            }
        };
        //  估狗圖
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //  導航區
//        place1 = new MarkerOptions().position(new LatLng(24.151287, 121.625537));
//        place2 = new MarkerOptions().position(new LatLng(23.791952, 120.861379));
//
//        String url = getUrl(place1.getPosition(),place2.getPosition(),"driving");


        //Qrcode區
//        EditText etQRCodeText = findViewById(R.id.etQRCodeText);
//        String qrCodeText = etQRCodeText.getText().toString();//
        String order_id = Util.D_ORDER.getIdo_no();

        String qrCodeText = order_id;
        int smallerDimension = getDimension();
//        switch (v.getId()) {
//            case R.id.btnQRCode:
        Log.e(LOG_TAG, qrCodeText);
        // Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrCodeText, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ivCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
//                break;
//        }
        // websocket區
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(this);
        registerChatReceiver();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String member_id = preferences.getString("member_id", "");
        Log.e(TAG, "onCreate: Delivery_member_id:" + member_id);
        Utils.connectServer(this, member_id);
    }

    // 攔截user連線或斷線的Broadcast
    private void registerChatReceiver() {
        //設定上線的過濾器
        IntentFilter openFilter = new IntentFilter("open");
        IntentFilter locationFilter = new IntentFilter("location");
        IntentFilter closeFilter = new IntentFilter("close");
        StaffSLocationReceiver staffSLocationReceiver = new StaffSLocationReceiver();
        //會員上線時觸發Receiver
        broadcastManager.registerReceiver(staffSLocationReceiver, openFilter);
        broadcastManager.registerReceiver(staffSLocationReceiver, locationFilter);
        broadcastManager.registerReceiver(staffSLocationReceiver, closeFilter);
    }

    // 攔截user連線或斷線的Broadcast，並在RecyclerView呈現
    private class StaffSLocationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage chatMessage = new Gson().fromJson(message, ChatMessage.class);
            String type = chatMessage.getType();
            String user_Id = chatMessage.getSender();
            switch (type) {
                // 有user連線
                case "open":
//                    // 如果是自己連線
//                    if (friend.equals(user)) {
//                        // 取得server上的所有user
//                        friendList = new LinkedList<>(stateMessage.getUsers());
//                        // 將自己從聊天清單中移除，否則會看到自己在聊天清單上
//                        friendList.remove(user);
//                    } else {
//                        // 如果其他user連線且尚未加入聊天清單，就加上
//                        if (!friendList.contains(friend)) {
//                            friendList.add(friend);
//                        }
                    showToast(DeliveryActivity.this, user_Id + " is online");
//                    }
//                    // 重刷聊天清單
//                    rvFriends.getAdapter().notifyDataSetChanged();
                    break;
                case "location":
//                    map.clear();
                    Double lat_staff = chatMessage.getLat();
                    Double lng_staff = chatMessage.getLng();
                    LatLng latLng_staff = new LatLng(lat_staff, lng_staff);
//                    MarkerOptions place_staff = new MarkerOptions().position(new LatLng(23.791952, 120.861379));
                    MarkerOptions place_staff = new MarkerOptions().position(new LatLng(lat_staff, lng_staff));
                    Log.d(TAG, "onReceive: 緯度:" + lat_staff + "經度:" + lng_staff);
                    Log.d(TAG, "onReceive: 以下為place_staff印出的資訊:" + place_staff);
                    map.addMarker(place_staff);
                    showToast(DeliveryActivity.this, "員工位置物件:緯度為" + lat_staff + "經度為:" + lng_staff);
                    new FetchURL(DeliveryActivity.this)
                            .execute(getUrl(place_staff.getPosition(), latLng, "driving"), "driving");
                    break;
                // 有user斷線
                case "close":
                    // 將斷線的user從聊天清單中移除
//                    friendList.remove(friend);
//                    rvFriends.getAdapter().notifyDataSetChanged();
                    showToast(DeliveryActivity.this, user_Id + " is offline");
            }
            Log.d(TAG, message);
        }
    }

//    private class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
//        Context context;
//
//        FriendAdapter(Context context) {
//            this.context = context;
//        }
//
//        class FriendViewHolder extends RecyclerView.ViewHolder {
//            TextView tvFriendName;
//
//            FriendViewHolder(View itemView) {
//                super(itemView);
//                tvFriendName = itemView.findViewById(R.id.tvFrinedName);
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return friendList.size();
//        }
//
//        @Override
//        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater layoutInflater = LayoutInflater.from(context);
//            View itemView = layoutInflater.inflate(R.layout.card_friends, parent, false);
//            return new FriendViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(FriendViewHolder holder, int position) {
//            final String friend = friendList.get(position);
//            holder.tvFriendName.setText(friend);
//            // 點選聊天清單上的user即開啟聊天頁面
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
//                    intent.putExtra("friend", friend);
//                    startActivity(intent);
//                }
//            });
//        }
//
//    }

    // Activity結束即中斷WebSocket連線
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.disconnectServer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 請求user同意定位
        askPermissions();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkLocationSettings();
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
                        updateLocationInfo(lastLocation);
                    }
                }
            });

            // 持續取得最新位置。looper設為null代表以現行執行緒呼叫callback方法，而非使用其他執行緒
            fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback, null);
        }
    }

    private void updateLocationInfo(Location lastLocation) {
        TextView tvLastLocation = findViewById(R.id.tvLastLocation);
        StringBuilder msg = new StringBuilder();
        msg.append("自己位置相關資訊 \n");

        if (lastLocation == null) {
            Toast.makeText(this, getString(R.string.msg_LastLocationNotAvailable), Toast.LENGTH_SHORT).show();
            return;
        }

        // 取得定位時間
        Date date = new Date(lastLocation.getTime());
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String time = dateFormat.format(date);
        msg.append("定位時間: " + time + "\n");

        // 取得自己位置的緯經度、精準度、高度、方向與速度，不提供的資訊會回傳0.0
        msg.append("緯度: " + lastLocation.getLatitude() + "\n");
        msg.append("經度: " + lastLocation.getLongitude() + "\n");
        msg.append("精準度(公尺): " + lastLocation.getAccuracy() + "\n");
        msg.append("高度(公尺): " + lastLocation.getAltitude() + "\n");
        msg.append("方向(角度): " + lastLocation.getBearing() + "\n");
        msg.append("速度(公尺/秒): " + lastLocation.getSpeed() + "\n");

        tvLastLocation.setText(msg);
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

    //估狗地圖----------------------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        setupMap();
    }


    // 完成地圖相關設定
    @SuppressLint("MissingPermission")
    private void setupMap() {
        map.setMyLocationEnabled(true);
        map.addMarker(place2);
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                // 鏡頭焦點在玉山
////                .target(latLng)
//                .target(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
////                 地圖縮放層級定為7
//                .zoom(13)
//                .build();
//        // 改變鏡頭焦點到指定的新地點
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
//        map.animateCamera(cameraUpdate);

        //標記區------------------------------------------
//        addMarkersToMap();
//
//        // 如果不套用自訂InfoWindowAdapter會自動套用預設訊息視窗
//        map.setInfoWindowAdapter(new MyInfoWindowAdapter());
//
//        MyMarkerListener listener = new MyMarkerListener();
//        // 註冊OnMarkerClickListener，當標記被點擊時會自動呼叫該Listener的方法
//        map.setOnMarkerClickListener(listener);
//        // 註冊OnInfoWindowClickListener，當標記訊息視窗被點擊時會自動呼叫該Listener的方法
//        map.setOnInfoWindowClickListener(listener);
//        // 註冊OnMarkerDragListener，當標記被拖曳時會自動呼叫該Listener的方法
//        map.setOnMarkerDragListener(listener);

    }

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
                if (ActivityCompat.checkSelfPermission(DeliveryActivity.this,
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
                        resolvable.startResolutionForResult(DeliveryActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
    }

    //導航區
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
//        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyA392KrSTzJHYommhIlDDD6__eb8dJ8cTM";
//        Log.d("myLog", "getUrl: " + url);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }

    private int getDimension() {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 取得螢幕尺寸
        Display display = manager.getDefaultDisplay();
        // API 13列為deprecated，但為了支援舊版手機仍採用
        int width = display.getWidth();
        int height = display.getHeight();

        // 產生的QR code圖形尺寸(正方形)為螢幕較短一邊的1/2長度
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension / 2;

        // API 13開始支援
//                Display display = manager.getDefaultDisplay();
//                Point point = new Point();
//                display.getSize(point);
//                int width = point.x;
//                int height = point.y;
//                int smallerDimension = width < height ? width : height;
//                smallerDimension = smallerDimension / 2;
        return smallerDimension;
        //websocket區
    }
}

