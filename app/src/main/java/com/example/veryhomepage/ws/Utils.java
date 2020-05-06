package com.example.veryhomepage.ws;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

import static android.content.Context.MODE_PRIVATE;

public class Utils {
    private final static String TAG = "Util";
    public static final String SERVER_URI =
//            "ws://192.168.43.132:8081/DA106_G4/DeliveryWS/";
//            "ws://35.229.239.13:8081/DA106_G4_Foodporn_Git/DeliveryWS/";
            "ws://192.168.43.132:8081/DA106_G4_Foodporn_Git/DeliveryWS/";
    public static GetStaffLocationWebSocketClient getStaffLocationWebSocketClient;

    // 建立WebSocket連線
    public static void connectServer(Context context, String userName) {
        URI uri = null;
        try {
            uri = new URI(SERVER_URI + userName);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (getStaffLocationWebSocketClient == null) {
            getStaffLocationWebSocketClient = new GetStaffLocationWebSocketClient(uri, context);
            getStaffLocationWebSocketClient.connect();
        }
    }

    // 中斷WebSocket連線
    public static void disconnectServer() {
        if (getStaffLocationWebSocketClient != null) {
            getStaffLocationWebSocketClient.close();
            getStaffLocationWebSocketClient = null;
        }
    }

    public static void setUserName(Context context, String userName) {
        SharedPreferences preferences =
                context.getSharedPreferences("user", MODE_PRIVATE);
        preferences.edit().putString("userName", userName).apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences("user", MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        Log.d(TAG, "userName = " + userName);
        return userName;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show();
    }


}
