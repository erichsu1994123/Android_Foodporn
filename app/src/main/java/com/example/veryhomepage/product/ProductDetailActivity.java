package com.example.veryhomepage.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.veryhomepage.R;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.order.OrderProductVO;
import com.example.veryhomepage.task.ImageTask;

import static com.example.veryhomepage.main.Util.CART;
import static com.example.veryhomepage.main.Util.showToast;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView ivRecipeAct;
    private TextView tvR_Name, tvR_Price, tvR_Content;
    private ImageTask productVOImageTask;
    private int imageSize;
    private ProductVO productVO;
    private AddSubView addSubView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        findViews();
        showResult();
    }


    private void findViews() {
        ivRecipeAct = findViewById(R.id.ivRecipeAct);
        tvR_Name = findViewById(R.id.tvR_Name);
        tvR_Price = findViewById(R.id.tvR_Price);
        tvR_Content = findViewById(R.id.tvR_Content);
        addSubView = findViewById(R.id.add_sub_view);
    }

    private void showResult() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        productVO = (ProductVO) bundle.getSerializable("productVO");

        String url = Util.URL + "AnProductServlet";;
        String recipe_id = productVO.getRecipe_id();
        productVOImageTask = new ImageTask(url, recipe_id, imageSize, ivRecipeAct);
        productVOImageTask.execute();

        tvR_Name.setText("料理包名稱: " + productVO.getProduct_name());
        tvR_Price.setText("料理包價格:" + productVO.getProduct_price());
        tvR_Content.setText("料理包介紹:" + productVO.getContent());

    }

    public void onAddCartClick(View view) {
        showToast(this,"選購數量為" + addSubView.getCurrentValue());
        OrderProductVO orderProductVO = new OrderProductVO(productVO, addSubView.getCurrentValue());
        int index = CART.indexOf(orderProductVO);
        // 要比對欲加入商品與購物車內商品的id是否相同(會呼叫ProductVO.equals())，
        // 相同則將該商品取出後，數量+1；不同則將該商品放入購物車內
        // 購物車不使用HashSet是因為即使快速找到值相同也無法取得該值，必須使用for-each，
        // 而且user購物品項少所以即使不使用HashSet，效能不見得比較差
        if (index == -1) {
            CART.add(orderProductVO);
        } else {
            orderProductVO = CART.get(index);
            orderProductVO.setQuantity(orderProductVO.getQuantity() + 1);
        }
        finish();

//        StringBuilder sb = new StringBuilder();
//        for (OrderProductVO oProduct : CART) {
//            String text = "\n-" + oProduct.getProduct_name() + " x "
//                    + oProduct.getQuantity();
//            sb.append(text);
//        }
//        String message = getString(R.string.cartContents) + " " + sb.toString();
//        new AlertDialog.Builder(ProductDetailActivity.this)
//                .setIcon(R.drawable.cart)
//                .setTitle(getString(R.string.cartAdd) + "\n「" + productVO.getProduct_name() + "」")
//                .setMessage(message)
//                .setNeutralButton(R.string.text_btnConfirm,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
////                                Intent intent = new Intent(this, MainActivity.class);
//                                //        Bundle bundle = new Bundle();
//                                //        intent.putExtras(bundle);
////                                startActivity(intent);
//                            }
//                        }).show();
    }
}
