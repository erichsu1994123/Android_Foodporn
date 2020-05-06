package com.example.veryhomepage.coupon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.veryhomepage.R;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.product.ProductDetailActivity;
import com.example.veryhomepage.product.ProductVO;
import com.example.veryhomepage.task.CommonTask;
import com.example.veryhomepage.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CouponActivity extends AppCompatActivity {
    private ImageView ivCouponAct;
    private TextView tvC_Name, tvC_Date, tvC_Code;
    private ImageTask couponVOImageTask, productVOImageTask;
    private CommonTask getProductVOforCouponTask;
    private int imageSize, imageSize_P;
    private RecyclerView recyclerView;
    ArrayList<ProductVO> arrayList;
    private CouponVO couponVO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        imageSize_P = this.getResources().getDisplayMetrics().widthPixels / 2;
        imageSize = 1;
        findViews();
        showResult();

    }

    private void findViews() {
        ivCouponAct = findViewById(R.id.ivCouponAct);
        tvC_Name = findViewById(R.id.tvC_Name);
        tvC_Date = findViewById(R.id.tvC_Date);
        tvC_Code = findViewById(R.id.tvC_Code);
        recyclerView = findViewById(R.id.rvProduct);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        couponVO = (CouponVO) bundle.getSerializable("couponVO");
        String c_no = couponVO.getC_no();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getSomeByC_no");
        jsonObject.addProperty("c_no", c_no);
        String jsonOut = jsonObject.toString();

        getProductVOforCouponTask = new CommonTask(Util.URL + "AnProductServlet", jsonOut);
        try {
            String jsonIn = getProductVOforCouponTask.execute().get();
            Type listType = new TypeToken<List<ProductVO>>(){}.getType();
            Gson gson = new Gson();
            arrayList = gson.fromJson(jsonIn, listType);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (arrayList == null || arrayList.isEmpty()) {
            Log.e("TAGbyME", "arrayList是空的");
            Util.showToast(this, R.string.msg_BooksNotFound);
        }

        recyclerView.setAdapter(new MyAdapter(CouponActivity.this, arrayList));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        1, StaggeredGridLayoutManager.VERTICAL));
    }

    private void showResult() {
        String url = Util.URL + "AnCouponServlet";
        String c_no = couponVO.getC_no();
        Log.e("TAG", "showResult: c_no:" + c_no );
        couponVOImageTask = new ImageTask(url, c_no, imageSize, ivCouponAct);
        couponVOImageTask.execute();
        tvC_Name.setText("優惠卷名稱: " + couponVO.getC_name());
        tvC_Date.setText("使用日期: " + couponVO.getStart_date() + "~" + couponVO.getEnd_date());
        tvC_Code.setText("優惠代碼: "+ couponVO.getCoupon_code());

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        ArrayList<ProductVO> arrayList;
        Context context;

        public MyAdapter(Context context,ArrayList<ProductVO> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product,parent,false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final ProductVO productVO = arrayList.get(position);
//            Log.d("TAG", "onBindViewHolder: " + productVO);
            String name = productVO.getProduct_name();
//            Utils.showToast(context, "商品物件:" + productVO);
//            Log.d("TAG", "onBindViewHolder: "+ name);
            holder.textView.setText(name);
            productVOImageTask = new ImageTask(Util.URL +"AnProductServlet", productVO.getRecipe_id(), imageSize_P, holder.imageView);
            productVOImageTask.execute();
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

        class ViewHolder extends RecyclerView.ViewHolder{
            private ImageView imageView;
            private TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivPic);
                textView = itemView.findViewById(R.id.tilName);
            }


        }


    }
}
