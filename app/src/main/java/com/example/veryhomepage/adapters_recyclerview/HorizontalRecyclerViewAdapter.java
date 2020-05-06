package com.example.veryhomepage.adapters_recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.veryhomepage.R;
import com.example.veryhomepage.adapters_recyclerview.models.HorizontalModel;
import com.example.veryhomepage.coupon.CouponVO;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.product.ProductVO;
import com.example.veryhomepage.recipe.RecipeVO;
import com.example.veryhomepage.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter {
    private static final String TAG = "HorizontalVA";
    ArrayList<HorizontalModel> arrayList;
    Context context;
    private CommonTask getCouponVOTask, getProductVOTask, getRecipeVOTask;
    ArrayList<CouponVO> couponVOList = null;
    ArrayList<ProductVO> productVOList = null;
    ArrayList<RecipeVO> recipeVOList = null;
    ArrayList arrayList_PtoR = null;

    public HorizontalRecyclerViewAdapter(Context context, ArrayList<HorizontalModel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() { return arrayList.size(); }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //switch case 寫法
//        View view = null;
//        switch (viewType){
//            case 0:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal,parent,false);
//                return new VerticalRVViewHolder(view);
//            case 1:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal,parent,false);
//                return new VerticalRVViewHolder2(view);
//        }
//        return null;

        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal, parent, false);
            return new HorizontalRVViewHolder_Coupon(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal, parent, false);
            return new HorizontalRVViewHolder_Product(view);
//        } else {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal, parent, false);
//            return new VerticalRVViewHolder_Recipe(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HorizontalModel horizontalModel = arrayList.get(position);
        String title = horizontalModel.getTitle();

        if (position == 0) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            updateUI(jsonOut);

//            for (ProductVO aProductVO : productVOList) {
//                if (aProductVO.getRecipe_id() != null) {
//                    arrayList_PtoR.add(aProductVO);
//                }
//            }
        }

        if (holder instanceof HorizontalRVViewHolder_Coupon) {
            ((HorizontalRVViewHolder_Coupon) holder).tvTitle.setText(title);
            VerticalRecyclerAdapter_ForCouponVO verticalRecyclerAdapter_forCouponVO = new VerticalRecyclerAdapter_ForCouponVO(context, couponVOList);
            ((HorizontalRVViewHolder_Coupon) holder).recyclerView.setHasFixedSize(true);
            ((HorizontalRVViewHolder_Coupon) holder).recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL));
//            ((HorizontalRVViewHolder_Coupon) holder).recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            ((HorizontalRVViewHolder_Coupon) holder).recyclerView.setAdapter(verticalRecyclerAdapter_forCouponVO);

        } else if (holder instanceof HorizontalRVViewHolder_Product) {
            ((HorizontalRVViewHolder_Product) holder).tvTitle.setText(title);
            VerticalRecyclerAdapter_ForProductVO verticalRecyclerAdapter_forProductVO = new VerticalRecyclerAdapter_ForProductVO(context, productVOList);
            ((HorizontalRVViewHolder_Product) holder).recyclerView.setHasFixedSize(true);
            ((HorizontalRVViewHolder_Product) holder).recyclerView.setLayoutManager(
                    new StaggeredGridLayoutManager(
                            2, StaggeredGridLayoutManager.VERTICAL));
//            ((HorizontalRVViewHolder_Product) holder).recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            ((HorizontalRVViewHolder_Product) holder).recyclerView.setAdapter(verticalRecyclerAdapter_forProductVO);
//        } else if (holder instanceof VerticalRVViewHolder_Recipe) {
//            ((VerticalRVViewHolder_Recipe) holder).tvTitle.setText(title);
//            HorizontalRecyclerAdapter_ForRecipeVO horizontalRecyclerAdapter_forRecipeVO = new HorizontalRecyclerAdapter_ForRecipeVO(context, recipeVOList);
//            ((VerticalRVViewHolder_Recipe) holder).recyclerView.setHasFixedSize(true);
//            ((VerticalRVViewHolder_Recipe) holder).recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//            ((VerticalRVViewHolder_Recipe) holder).recyclerView.setAdapter(horizontalRecyclerAdapter_forRecipeVO);
        }
    }


    public class HorizontalRVViewHolder_Coupon extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView tvTitle;

        public HorizontalRVViewHolder_Coupon(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView1);
            tvTitle = itemView.findViewById(R.id.tvTitle1);
        }
    }

    public class HorizontalRVViewHolder_Product extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView tvTitle;

        public HorizontalRVViewHolder_Product(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView1);
            tvTitle = itemView.findViewById(R.id.tvTitle1);
        }
    }

//    public class VerticalRVViewHolder_Recipe extends RecyclerView.ViewHolder {
//        RecyclerView recyclerView;
//        TextView tvTitle;
//
//        public VerticalRVViewHolder_Recipe(View itemView) {
//            super(itemView);
//            recyclerView = itemView.findViewById(R.id.recyclerView1);
//            tvTitle = itemView.findViewById(R.id.tvTitle1);
//        }
//    }

    private void updateUI(String jsonOut) {

        getCouponVOTask = new CommonTask(Util.URL + "AnCouponServlet", jsonOut);
        getProductVOTask = new CommonTask(Util.URL + "AnProductServlet", jsonOut);

        try {
            String jsonIn = getCouponVOTask.execute().get();
            Type listType = new TypeToken<List<CouponVO>>() {
            }.getType();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            couponVOList = gson.fromJson(jsonIn, listType);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (couponVOList == null || couponVOList.isEmpty()) {
            Log.e(TAG, "couponVOList是空的");
            Util.showToast(context, R.string.msg_BooksNotFound);
        }

        try {
            String jsonIn = getProductVOTask.execute().get();
            Type listType = new TypeToken<List<ProductVO>>() {
            }.getType();
            Gson gson = new Gson();
            productVOList = gson.fromJson(jsonIn, listType);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (productVOList == null || productVOList.isEmpty()) {
            Log.e(TAG, "productVOList是空的");
            Util.showToast(context, R.string.msg_BooksNotFound);
        }

//        getRecipeVOTask = new CommonTask(Util.URL + "RecipeServlet", jsonOut);
//        try {
//            String jsonIn = getRecipeVOTask.execute().get();
//            Type listType = new TypeToken<List<RecipeVO>>() {
//            }.getType();
//            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
//            recipeVOList = gson.fromJson(jsonIn, listType);
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//        if (recipeVOList == null || recipeVOList.isEmpty()) {
//            Log.e(TAG, "recipeVOList是空的");
//            Util.showToast(context, R.string.msg_BooksNotFound);
//        }
    }
}
