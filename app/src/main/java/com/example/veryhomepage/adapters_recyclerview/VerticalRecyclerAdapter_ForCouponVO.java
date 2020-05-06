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
import com.example.veryhomepage.coupon.CouponActivity;
import com.example.veryhomepage.coupon.CouponVO;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.task.CommonTask;
import com.example.veryhomepage.task.ImageTask;

import java.util.ArrayList;

public class VerticalRecyclerAdapter_ForCouponVO extends RecyclerView.Adapter<VerticalRecyclerAdapter_ForCouponVO.HorizontalRVViewHolder> {
    ArrayList<CouponVO> arrayList;
    Context context;
    private CommonTask getCouponVOTask;
    private ImageTask couponVOImageTask;
    private int imageSize;

    public VerticalRecyclerAdapter_ForCouponVO(Context context , ArrayList<CouponVO> arrayList){
        this.arrayList = arrayList;
        this.context = context;
        /* 螢幕寬度除以4當作將圖的尺寸 */
        imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;
    }

    @Override
    public HorizontalRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical_coupon,parent,false);
        return new HorizontalRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalRVViewHolder holder, int position) {
        final CouponVO couponVO = arrayList.get(position);
        String url = Util.URL + "AnCouponServlet";
        String c_no = couponVO.getC_no();
        couponVOImageTask = new ImageTask(url, c_no, imageSize, holder.ivPic);
        couponVOImageTask.execute();
        holder.tvTitleHorizontal.setText(couponVO.getC_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CouponActivity.class);
                Bundle bundle = new Bundle();
                // 傳送優惠卷資料
                bundle.putSerializable("couponVO",couponVO);
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

