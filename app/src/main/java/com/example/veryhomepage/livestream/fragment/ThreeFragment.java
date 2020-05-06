package com.example.veryhomepage.livestream.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.veryhomepage.R;
import com.example.veryhomepage.livestream.Livestream;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

//import android.support.v7.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeFragment extends Fragment {
    private View view;
    private CommonTask lsCollectionTask, titleTask;
    private static final String TAG = "ThreeFragment";
    //private String livestream_id;
    private RecyclerView recyclerCardView_f;
    private RecyclerCardViewItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_three, container, false);

        showLs();
        return view;

    }




    @Override
    public void onStart() {
        super.onStart();
    }


    private void showLs(){
        if (Util.networkConnected(getActivity())){
            SharedPreferences preferences = this.getActivity().getSharedPreferences("preference", Context.MODE_PRIVATE);
            String member_id = preferences.getString("member_id", "");
            Log.e("memmmm", member_id);
            String url = Util.URL + "CollectionServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findlsBymemberid");
            jsonObject.addProperty("member_id", member_id);
            String jsonOut = jsonObject.toString();
            lsCollectionTask = new CommonTask(url, jsonOut);
            List<Livestream> lsList = null;
            try {
               String lsId = lsCollectionTask.execute().get();
               Log.e("isid", lsId);
                Type listType = new TypeToken<List<Livestream>>() {
                }.getType();
                lsList = new Gson().fromJson(lsId, listType);
                if (lsList == null || lsList.isEmpty()) {
                    Util.showToast(getActivity(), R.string.msg_BooksNotFound);
                } else {
                    RecyclerView recyclerCardView_f = (RecyclerView) view.findViewById(R.id.recyclerCardView_f);
                    recyclerCardView_f.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
//
                    recyclerCardView_f.setAdapter(new RecyclerCardViewItemAdapter(getContext(),lsList));
                }
                //Log.e("lsid", lsId);
//                JsonObject jsonObject1 = new JsonParser().parse(lsId).getAsJsonObject();
//                String livestream_id = jsonObject1.get("livestream_id").getAsString();
//                this.livestream_id = livestream_id;
                //jsonObject1 is LsID, need id to get ls pic and title to show
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Util.showToast(getContext(), R.string.msg_NoNetwork);
        }
    }


    //CardView
    private class RecyclerCardViewItemAdapter extends RecyclerView.Adapter<RecyclerCardViewItemAdapter.RecyclerCardViewHolder>{

        private Context context;
        private List<Livestream> lsList;

        public RecyclerCardViewItemAdapter(Context context, List<Livestream> lsList) {
            this.context = context;
            this.lsList = lsList;
        }

        public class RecyclerCardViewHolder extends RecyclerView.ViewHolder{

          //  private final ImageView ivLsPicture;
            private TextView tvlsid, tvlstitle;
           // private final TextView textView5;

            public RecyclerCardViewHolder(View itemView) {
                super(itemView);
                tvlsid = (TextView) itemView.findViewById(R.id.tvlsid);
                tvlstitle = (TextView) itemView.findViewById(R.id.tvlstitle);
            }
        }

        @Override
        public RecyclerCardViewItemAdapter.RecyclerCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.cardview_lscollection,parent,false);
            return new RecyclerCardViewItemAdapter.RecyclerCardViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerCardViewItemAdapter.RecyclerCardViewHolder holder, int position) {
           // String livestream_id = showLs();
            final Livestream livestream = lsList.get(position);
//            Log.e("3", livestream_id);
            String livestream_id = livestream.getLivestream_id();
            holder.tvlsid.setText(livestream_id);//ok don't move

            String url = Util.URL + "AnLivestreamServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findTitleById");
            jsonObject.addProperty("livestream_id", livestream_id);
            String jsonOut = jsonObject.toString();
            titleTask = new CommonTask(url, jsonOut);
            try {
                String title = titleTask.execute().get();
                Log.e("title", title);
                JsonObject jsonObject1 = new JsonParser().parse(title).getAsJsonObject();
                holder.tvlstitle.setText(jsonObject1.get("title").getAsString());
                Log.e("try", "rty");
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

//             holder.ivLsPicture.setImageResource(item.getImage());
           // holder.tvlstitle.setText(item.getSubtitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ImageView imageView = new ImageView(context);
                   // imageView.setImageResource(item.getImage());
//                    Toast toast = new Toast(context);
//                    toast.setView(imageView);
//                    toast.setDuration(Toast.LENGTH_SHORT);
//                    toast.show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return lsList.size();
        }
    }

}
