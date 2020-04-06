package com.example.veryhomepage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.veryhomepage.adapters.models.HorizontalModel;
import com.example.veryhomepage.adapters.models.VerticalModel;
import com.example.veryhomepage.R;

import java.util.ArrayList;

public class VerticalRecyclerViewAdapter extends RecyclerView.Adapter<VerticalRecyclerViewAdapter.VerticalRVViewHolder> {
    ArrayList<VerticalModel> arrayList;
    Context context;

    public VerticalRecyclerViewAdapter(Context context, ArrayList<VerticalModel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public VerticalRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical,parent,false);
        return new VerticalRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VerticalRVViewHolder holder, int position) {
       VerticalModel verticalModel = arrayList.get(position);
       String title = verticalModel.getTitle();
       ArrayList<HorizontalModel> singleItem = verticalModel.getArrayList();

       holder.tvTitle.setText(title);
       HorizontalRecyclerAdapter horizontalRecyclerAdapter = new HorizontalRecyclerAdapter(context,singleItem);

       holder.recyclerView.setHasFixedSize(true);
       holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));
       holder.recyclerView.setAdapter(horizontalRecyclerAdapter);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VerticalRVViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;
        TextView tvTitle;

        public VerticalRVViewHolder(View itemView){
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView1);
            tvTitle = itemView.findViewById(R.id.tvTitle1);

        }
    }
}
