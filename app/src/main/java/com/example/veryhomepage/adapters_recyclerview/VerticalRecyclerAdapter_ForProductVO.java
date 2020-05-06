package com.example.veryhomepage.adapters_recyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.veryhomepage.R;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.product.ProductDetailActivity;
import com.example.veryhomepage.product.ProductVO;
import com.example.veryhomepage.task.ImageTask;

import java.util.ArrayList;

public class VerticalRecyclerAdapter_ForProductVO extends RecyclerView.Adapter<VerticalRecyclerAdapter_ForProductVO.HorizontalRVViewHolder> {
    ArrayList<ProductVO> arrayList;
    Context context;
    private ImageTask productVOImageTask;
    private int imageSize;

    public VerticalRecyclerAdapter_ForProductVO(Context context , ArrayList<ProductVO> arrayList){
        this.arrayList = arrayList;
        this.context = context;
        /* 螢幕寬度除以4當作將圖的尺寸 */
        imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;
    }

    @Override
    public HorizontalRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical_product,parent,false);
        return new HorizontalRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalRVViewHolder holder, int position) {
        final ProductVO productVO = arrayList.get(position);
        String url = Util.URL + "AnProductServlet";
        String recipe_id = productVO.getRecipe_id();
        productVOImageTask = new ImageTask(url, recipe_id, imageSize, holder.ivPic);
        productVOImageTask.execute();
        holder.tvTitleHorizontal.setText(productVO.getProduct_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"選到我了",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                // 傳送商品資料
                bundle.putSerializable("productVO", productVO);
                intent.putExtras(bundle);
                context.startActivity(intent);
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
            tvTitleHorizontal = itemView.findViewById(R.id.tvTitleVertical);
            ivPic = itemView.findViewById(R.id.ivPic);
        }
    }
}


