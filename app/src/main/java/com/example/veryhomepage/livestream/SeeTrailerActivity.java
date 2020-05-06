package com.example.veryhomepage.livestream;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.veryhomepage.R;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.sql.Date;

public class SeeTrailerActivity extends AppCompatActivity {
    private static final String TAG = "SeeTrailerActivity";
    private TextView tvMember_id,tvLsId,tvDate1, tvTitle1, tvIntro1;
    private ImageView ivPic1;
    private CommonTask seeTrailerTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_trailer);

        findViews();
        setMemberId();
        setTrailer();
    }

    private void findViews(){
        tvMember_id = findViewById(R.id.tvMember_id);
        tvDate1 = findViewById(R.id.tvDate1);
        tvTitle1 = findViewById(R.id.tvTitle1);
        tvIntro1 = findViewById(R.id.tvIntro1);
        ivPic1 = findViewById(R.id.ivPic1);
        tvLsId = findViewById(R.id.tvLsId);
    }

    private void setMemberId(){
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        //        preferences.edit().putBoolean("login", true).apply();
        String member_id = preferences.getString("member_id","");

        tvMember_id.setText("廚師ID : "+ member_id);
    }

    private void setTrailer(){
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        //        preferences.edit().putBoolean("login", true).apply();
        String member_id = preferences.getString("member_id","");
        String url = Util.URL + "AnLivestreamServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findTrByStatus");
        jsonObject.addProperty("member_id", member_id);
        String jsonOut = jsonObject.toString();
        seeTrailerTask = new CommonTask(url, jsonOut);
        try {
            String result = seeTrailerTask.execute().get();
            Log.e("result", result);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
            //   java.util.Date now = new java.util.Date();
            Livestream jjj = gson.fromJson(result,Livestream.class);

            String livestream_id = jjj.getLivestream_id();
            String introduction = jjj.getIntroduction();
            String title = jjj.getTitle();
            Date livestreamdate = jjj.getLivestream_date();
            Log.e("livestream_id", livestream_id);
            Log.e("introduction", introduction);
            Log.e("livestreamdate", String.valueOf(livestreamdate));
            // tvDate1 .setText((CharSequence) livestreamdate);
            tvLsId.setText(livestream_id);//ok

            tvIntro1.setText(introduction);//ok

            tvDate1.setText(String.valueOf(livestreamdate));

            tvTitle1.setText(title);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (seeTrailerTask != null) {
            seeTrailerTask.cancel(true);
        }
    }

}
