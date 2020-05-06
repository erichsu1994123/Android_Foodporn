package com.example.veryhomepage.main;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.veryhomepage.R;
import com.example.veryhomepage.livestream.LiveChatActivity;
import com.example.veryhomepage.livestream.Livestream;
import com.example.veryhomepage.livestream.LsTrailerActivity;
import com.example.veryhomepage.livestream.Mp4FileActivity;
import com.example.veryhomepage.livestream.SeeTrailerActivity;
import com.example.veryhomepage.livestream.memberActivity;
import com.example.veryhomepage.task.CommonTask;
import com.example.veryhomepage.task.CommonTaskMain;
import com.example.veryhomepage.task.TrailerImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeThirdFragment_LiveStream extends Fragment {
    private static final String TAG = "MainActivity";
    private RecyclerView adRecycleView;
    private List<Livestream> cardList;
    private TextView tvUserName;
    private String name;
    private CommonTaskMain memberTask;
    private CommonTask lsTask2;
    private TrailerImageTask lsImageTask;
    private String userName;
    private Button btMember, btnLight, btnSearch;
    private Dialog myDialog;
    private ImageView ivMakeTrailer, ivWatchTrailer;
    private Spinner spCategory;
    private Activity activity;


    public HomeThirdFragment_LiveStream() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_third_live_stream, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        findViews();

        adRecycleView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //adRecycleView.setAdapter(new LivestreamAdapter(cardList));

        if (Util.networkConnected(activity)) {
            try {
                //List<Livestream> categoryList = new ArrayList<>();
                List<String> category = new ArrayList<>();
                category.add(0, "直播預告");
                category.add(1, "現正直播");
                category.add(2, "精選直播");
//                for (int i = 0; i < 3; i++) {
//                    category.add(categoryList.get(i).getCname());
//                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, category);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCategory.setAdapter(adapter);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getSome");
//            String jsonOut = jsonObject.toString();
//            updateUI(jsonOut);
        } else {
            Util.showToast(activity, R.string.msg_NoNetwork);
        }
    }


    public void findViews() {
        spCategory = activity.findViewById(R.id.spCategory);
        adRecycleView = activity.findViewById(R.id.adRecycleView);
        tvUserName = activity.findViewById(R.id.tvUserName);
        btnLight = activity.findViewById(R.id.btnLight);
        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog = new Dialog(activity);
                myDialog.setTitle(getString(R.string.dialog_light));

                myDialog.setCancelable(true);
                myDialog.setContentView(R.layout.dialog_lightbox);
                Window dialogWindow = myDialog.getWindow();
                dialogWindow.setGravity(Gravity.CENTER);
                WindowManager m = activity.getWindowManager();

//                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                lp.width = 1000;
//                lp.alpha = 1.0f;
//                dialogWindow.setAttributes(lp);

                Display d = m.getDefaultDisplay(); // 取得螢幕寬、高用
                WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 獲取對話視窗當前的参數值
                p.height = (int) (d.getHeight() * 0.6); // 高度設置為螢幕的0.6 (60%)
                p.width = (int) (d.getWidth() * 0.95); // 寬度設置為螢幕的0.95 (95%)
                dialogWindow.setAttributes(p);

                ivMakeTrailer = myDialog.findViewById(R.id.ivMakeTrailer);
                ivMakeTrailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, LsTrailerActivity.class);
                        startActivity(intent);
                    }
                });

                ivWatchTrailer = myDialog.findViewById(R.id.ivWatchTrailer);
                ivWatchTrailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, SeeTrailerActivity.class);
                        startActivity(intent);
                    }
                });
                myDialog.show();
            }
        });
        btMember = activity.findViewById(R.id.btMember);
        btMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, memberActivity.class);
                startActivity(intent);
            }
        });
        btnSearch = activity.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // spinner position starts from 0
                int i = spCategory.getSelectedItemPosition() + 1;
                String url = Util.URL + "AnLivestreamServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "findByCategory");
                jsonObject.addProperty("status", String.valueOf(i));
                String jsonOut = jsonObject.toString();
                lsTask2 = new CommonTask(url, jsonOut);
                List<Livestream> lsList = null;
                try {
                    String jsonIn = lsTask2.execute().get();
                    Log.e("main.jsonIn", jsonIn);
                    Type listType = new TypeToken<List<Livestream>>() {
                    }.getType();
                    lsList = new Gson().fromJson(jsonIn, listType);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (lsList == null || lsList.isEmpty()) {
                    Util.showToast(activity, R.string.msg_BooksNotFound);
                } else {
                    adRecycleView.setAdapter(new LivestreamAdapter(activity, lsList));
                }
            }
        });

    }

//    private void updateUI(String jsonOut) {
//        lsTask2 = new CommonTask(Util.URL + "AnLivestreamServlet", jsonOut);
//        List<Livestream> livestreamList = null;
//        try {
//            String jsonIn = lsTask2.execute().get();
//            Type listType = new TypeToken<List<Livestream>>() {
//            }.getType();
//            livestreamList = new Gson().fromJson(jsonIn, listType);
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//        if (livestreamList == null || livestreamList.isEmpty()) {
//            Util.showToast(this, R.string.msg_BooksNotFound);
//        } else {
//            adRecycleView.setAdapter(new LivestreamAdapter(this, livestreamList));
//        }
//    }

    public void onSearch(View view) {

    }

    public void showUserName(String userName) {
        String url = Util.URL + "AnMemberServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "findNameByAccount");
        jsonObject.addProperty("account", userName);
        String jsonOut = jsonObject.toString();
        memberTask = new CommonTaskMain(url, jsonOut);
        try {
            String result = memberTask.execute().get();
            JsonObject jsonObject1 = new JsonParser().parse(result).getAsJsonObject();

            tvUserName.setText(jsonObject1.get("account").getAsString());
            // isMember = Boolean.valueOf(result);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        // String userName = getString(R.string.hiUser) + ": " + result;
    }

//    public boolean isLoggedIn() {
//        SharedPreferences pref = getSharedPreferences(Util.PREF_FILE,
//                MODE_PRIVATE);
//        return pref.getBoolean ("login", false);
//    }

    @Override
    public void onStop() {
        super.onStop();
        if (memberTask != null) {
            memberTask.cancel(true);
        }
        if (lsImageTask != null) {
            lsImageTask.cancel(true);
        }
        if (lsTask2 != null) {
            lsTask2.cancel(true);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && requestCode == 1) {
            userName = data.getStringExtra("userName");
            showUserName(userName);
            //Log.e("userName", userName);
        }
    }


    private class LivestreamAdapter extends RecyclerView.Adapter<LivestreamAdapter.ViewHolder> {
        private Context context;
        private LayoutInflater layoutInflater;
        private List<Livestream> lsList;
        private int imageSize;

        LivestreamAdapter(Context context, List<Livestream> lsList) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.lsList = lsList;
            /* 螢幕寬度除以4當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView ivLsPicture;
            private TextView tvTitle, tvId;

            ViewHolder(View view) {
                super(view);
                ivLsPicture = view.findViewById(R.id.ivLsPicture);
                tvTitle = view.findViewById(R.id.tvTitle);
                tvId = view.findViewById(R.id.tvId);
            }

//            public void setItem(ImageView item){
//                ivLsPicture = item;
//                tvTitle.setText((CharSequence) item);
//            }

        }

        @Override
        public int getItemCount() {
            return lsList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_livestream, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Livestream livestream = lsList.get(position);
            String url = Util.URL + "AnLivestreamServlet";
            final String livestream_id = livestream.getLivestream_id();
            Log.e("lid", livestream_id);

            lsImageTask = new TrailerImageTask(url, livestream_id, imageSize, holder.ivLsPicture);
            lsImageTask.execute();

            holder.tvTitle.setText(livestream.getTitle());
            holder.tvId.setText(livestream.getLivestream_id());
            // itemView為ViewHolder內建屬性(指的就是每一列)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (livestream.getStatus() == 1) {//直播預告
                        Toast.makeText(activity, livestream.getLivestream_id(), Toast.LENGTH_SHORT).show();
                    }
                    if (livestream.getStatus() == 2) { //直播中

                        Intent intent = new Intent(activity, LiveChatActivity.class);
                        intent.putExtra("userName", tvUserName.getText().toString().trim());
                        intent.putExtra("livestream_id", livestream_id);
                        startActivity(intent);
                    }
                    if (livestream.getStatus() == 3) {//精選直播
                        Intent intent = new Intent(activity, Mp4FileActivity.class);
                        intent.putExtra("livestream_id", livestream_id);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
