package com.example.veryhomepage.livestream;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.veryhomepage.R;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.task.CommonTask;


public class Mp4FileActivity extends AppCompatActivity {
    private static final String TAG = "Mp4FileActivity";
    private static final String URL = Util.URL + "AnMp4DBServlet";
    private VideoView videoView;
    private CommonTask videoTask;
    private String livestream_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp4_file);
        findViews();
        livestream_id = (String) this.getIntent().getSerializableExtra("livestream_id");
        if (livestream_id.trim().isEmpty()) {
            Util.showToast(this, R.string.msg_BooksNotFound);
        }
        else {
//            String url = Util.URL + "AnMp4DBServlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("livestream_id", livestream_id);
//            String jsonOut = jsonObject.toString();
//            videoTask = new CommonTask(url, jsonOut);
//            try {
//             videoTask.execute().get();
////                Util.showToast(this, aa);
//
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
            playVideo();
        }
    }

    private void findViews() {
        videoView = (VideoView) findViewById(R.id.videoView);
    }

    private void playVideo() {

        videoView.setVideoPath(URL + "?livestream_id=" + livestream_id);


        MediaController controller = new MediaController(this);
        videoView.setMediaController(controller);
        videoView.start();
    }
}
