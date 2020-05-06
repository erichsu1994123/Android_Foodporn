package com.example.veryhomepage.member;

//import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.veryhomepage.R;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private TextView tvMessage;
    private CommonTask isMemberTask, getMemberVOTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvMessage = findViewById(R.id.tvMessage);
        setResult(RESULT_CANCELED);
    }

    protected void onStart(){ //check
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        boolean login = preferences.getBoolean("login", false);
        if (login) {
            String account = preferences.getString("account", "");
            String password = preferences.getString("password", "");
            if (isMember(account, password)) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    private void showMessage(int msgResId) {
        tvMessage.setText(msgResId);
    }

    public void onLoginClick(View view) {
        EditText etUser = findViewById(R.id.etUser);
        EditText etPassword = findViewById(R.id.etPassword);
        String account = etUser.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (account.length() <= 0 || password.length() <= 0) {
            showMessage(R.string.msg_InvalidUserOrPassword);
            return;
        }

        if (isMember(account, password)) {
            SharedPreferences preferences = getSharedPreferences(
                    Util.PREF_FILE, MODE_PRIVATE);
            preferences.edit().putBoolean("login", true)
                    .putString("account", account)
                    .putString("password", password).apply();
            //嘗試preferences放進member_id
            JsonObject jsonObject_member = new JsonObject();
            jsonObject_member.addProperty("action", "findOneByAccountAndPassword");
            jsonObject_member.addProperty("account", account);
            jsonObject_member.addProperty("password", password);
            String jsonOut_member = jsonObject_member.toString();
            Log.e(TAG, "onLoginClick: jsonOut_member: "+ jsonOut_member);
            getMemberVOTask = new CommonTask(Util.URL + "AnMemberServlet", jsonOut_member);
            try {
                String jsonIn = getMemberVOTask.execute().get();
                Gson gson = new Gson();
                Member member = gson.fromJson(jsonIn, Member.class);
                Log.d(TAG, "onLoginClick: " + member);
                String member_id = member.getMember_id();
                String member_name = member.getMember_name();
                String member_address = member.getMember_address();
                String member_email = member.getEmail();
                String member_cellphone = member.getCellphone();
//                String member_creditcard = member.getMember_creditcard();
                Log.d(TAG, "onLoginClick: " + member_name + member_address);

                SharedPreferences preferences2 = getSharedPreferences(
                        Util.PREF_FILE, MODE_PRIVATE);
                preferences2.edit()
                        .putString("member_id", member_id)
                        .putString("member_name", member_name)
                        .putString("member_address", member_address)
                        .putString("member_cellphone", member_cellphone)
                        .putString("member_email", member_email)
//                        .putString("member_creditcard",member_creditcard)
                        .apply();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            setResult(RESULT_OK);
            finish();
        } else {
            showMessage(R.string.msg_InvalidUserOrPassword);
        }
    }

//    public void onNewAccountClick(View view) {
//        Intent intent = new Intent(this, signUpActivity.class);
//        startActivity(intent);
//    }

    private boolean isMember(final String account, final String password) {
        boolean isMember = false;
        if (Util.networkConnected(this)) {
            String url = Util.URL + "AnMemberServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isMember");
            jsonObject.addProperty("account", account);
            jsonObject.addProperty("password", password);
            String jsonOut = jsonObject.toString();
            isMemberTask = new CommonTask(url, jsonOut);
            try {
                String result = isMemberTask.execute().get();
                isMember = Boolean.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                isMember = false;
            }
        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }
        return isMember;
    }

    protected void onStop() {
        super.onStop();
        if (isMemberTask != null) {
            isMemberTask.cancel(true);
        }
    }
}
