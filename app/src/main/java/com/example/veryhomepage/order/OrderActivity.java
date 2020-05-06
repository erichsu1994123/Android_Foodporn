package com.example.veryhomepage.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.veryhomepage.R;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.product.ProductDetailActivity;
import com.example.veryhomepage.product.ProductVO;
import com.example.veryhomepage.task.ImageTask;

import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private ImageTask productImageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setViews();
    }

    private void setViews() {
        TextView tvOrder = findViewById(R.id.tvOrder);
        ListView listView = findViewById(R.id.listView);
        InstantDeliveryOrderVO order = (InstantDeliveryOrderVO) getIntent().getSerializableExtra("order");
        final List<OrderProductVO> orderProductVOList = order.getOrderProductVOList();

        String ido_no = order.getIdo_no();
        String member_id = order.getMember_id();
//        DateFormat df_default = DateFormat.getDateInstance(DateFormat.MEDIUM,
//                Locale.getDefault());
//        String date = df_default.format(order.getTimestamp());
        double sum = 0;
        for (OrderProductVO orderProductVO : orderProductVOList) {
            sum += orderProductVO.getProduct_price() * orderProductVO.getQuantity();
        }
        String orderStr = getString(R.string.col_OrderId) + ": " + ido_no + "\n"
                + getString(R.string.col_OrderCustomer) + ": " + member_id + "\n"
//                + getString(R.string.col_OrderDate) + ": " + date + "\n"
                + getString(R.string.col_OrderSum) + ": " + sum;
        tvOrder.setText(orderStr);

        listView.setAdapter(new ProductListAdapter(this, orderProductVOList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductVO orderProduct = orderProductVOList.get(position);
                Intent intent = new Intent(OrderActivity.this, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", orderProduct);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private class ProductListAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<OrderProductVO> productList;
        private int imageSize;

        public ProductListAdapter(Context context, List<OrderProductVO> productList) {
            layoutInflater = LayoutInflater.from(context);
            this.productList = productList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Object getItem(int position) {
            return productList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.listview_orderproduct, parent, false);
            }

            OrderProductVO orderProductVO = productList.get(position);

            String url = Util.URL + "AnProductServlet";
            String recipe_id = orderProductVO.getRecipe_id();
            ImageView ivOrderBookImage = convertView.findViewById(R.id.ivOrderProductImage);
            productImageTask = new ImageTask(url, recipe_id, imageSize, ivOrderBookImage);
            productImageTask.execute();

            TextView tvOrderProductName = convertView.findViewById(R.id.tvOrderProductName);
            TextView tvOrderProductPrice = convertView.findViewById(R.id.tvOrderProductPrice);
            TextView tvOrderProductQuantity = convertView.findViewById(R.id.tvOrderProductQuantity);
            tvOrderProductName.setText(orderProductVO.getProduct_name());
            tvOrderProductPrice.setText(getString(R.string.col_Price) + ": "
                    + String.valueOf(orderProductVO.getProduct_price()));
            tvOrderProductQuantity.setText("x " + orderProductVO.getQuantity());
            return convertView;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (productImageTask != null) {
            productImageTask.cancel(true);
        }
    }


}
