package com.example.veryhomepage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.veryhomepage.adapters.models.HorizontalModel;
import com.example.veryhomepage.R;

import java.util.ArrayList;

public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.HorizontalRVViewHolder> {
    ArrayList<HorizontalModel> arrayList;
    Context context;

    public HorizontalRecyclerAdapter(Context context ,ArrayList<HorizontalModel> arrayList){
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public HorizontalRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal,parent,false);
        return new HorizontalRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalRVViewHolder holder, int position) {
        final HorizontalModel horizontalModel = arrayList.get(position);
        holder.tvTitleHorizontal.setText(horizontalModel.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,horizontalModel.getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HorizontalRVViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitleHorizontal;
        ImageView ivPic;

        public HorizontalRVViewHolder(View itemView){
            super(itemView);
            tvTitleHorizontal = itemView.findViewById(R.id.tvTitleHorizontal);
            ivPic = itemView.findViewById(R.id.ivPic);
        }
    }
}

