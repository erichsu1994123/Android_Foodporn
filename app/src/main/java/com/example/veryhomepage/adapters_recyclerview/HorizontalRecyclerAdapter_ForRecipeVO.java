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
import com.example.veryhomepage.product.ProductVO;
import com.example.veryhomepage.recipe.RecipeActivity;
import com.example.veryhomepage.recipe.RecipeVO;

import java.util.ArrayList;

public class HorizontalRecyclerAdapter_ForRecipeVO extends RecyclerView.Adapter<HorizontalRecyclerAdapter_ForRecipeVO.HorizontalRVViewHolder> {
    ArrayList<RecipeVO> arrayList;
    ArrayList<ProductVO> arrayList_PtoR;
    Context context;
    private int imageSize;

    public HorizontalRecyclerAdapter_ForRecipeVO(Context context , ArrayList<RecipeVO> arrayList){
        this.arrayList = arrayList;
//        this.arrayList_PtoR = arrayList_PtoR;
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
        final RecipeVO recipeVO = arrayList.get(position);
//        final ProductVO productVO = arrayList_PtoR.get(position);
        holder.tvTitleHorizontal.setText(recipeVO.getRecipe_name());

//        int index = arrayList_PtoR.indexOf(recipeVO);
//
//        final int price = arrayList_PtoR.get(index).getProduct_price();
//
            //從Android轉成byte[](不建議)
//        String base64 = recipeVO.getRecipe_photo();
//        byte[] dedcodedString = Base64.decode(base64.split(",")[1], Base64.DEFAULT);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(dedcodedString,0 ,dedcodedString.length);
//        Bitmap covertedImage = Util.getResizedBitmap(decodedByte, imageSize);
//        holder.ivPic.setImageBitmap(covertedImage);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"選到我了",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, RecipeActivity.class);
                Bundle bundle = new Bundle();
//                // 傳送食譜+食譜價格資料
                bundle.putSerializable("recipeVO",recipeVO);
//                bundle.putInt("price", price);
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


