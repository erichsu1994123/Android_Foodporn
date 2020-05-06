package com.example.veryhomepage.order;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.veryhomepage.R;
import com.example.veryhomepage.coupon.CouponVO;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.product.ProductVO;
import com.example.veryhomepage.task.CommonTask;
import com.example.veryhomepage.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.veryhomepage.main.Util.CART;
import static com.example.veryhomepage.main.Util.showToast;


public class CartActivity extends AppCompatActivity {
    private final int LOGIN_REQUEST = 0;
    private final static String TAG = "CartFragment";
    private CommonTask orderAddTask, getProductVOforCouponTask, balanceEnoughTask;
    private ImageTask productImageTask;
    private Button btnCheckOut, btnEmptyCart;
    private TextView tvTotal, tvAddress, tvCellphone, tvEmail, tvPayMethod, tvResult_PayMethod, tvCouponCode, tvResult_Coupon;
    private RecyclerView recyclerView;
    private Gson gson;
    private SharedPreferences pref;
    private CouponVO couponVO;
    private Boolean useDiscount = false;
    private InstantDeliveryOrderVO order = new InstantDeliveryOrderVO();
    private String address, cellphone, email, payMethod, couponCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        //回來再改
        //        pref.edit().put
        //回來再改
        address = pref.getString("member_address", "");
        email = pref.getString("member_email", "");
        cellphone = pref.getString("member_cellphone", "");

        Log.d(TAG, "onCreate: " + CART);


        //check out button
        btnCheckOut = findViewById(R.id.btnCheckout);
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (CART == null || CART.size() <= 0) {
                    showToast(CartActivity.this, R.string.cartEmpty);
                    return;
                }
                //利用 account和 password 去查 member_id
//                SharedPreferences pref = getActivity().getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
//                String account = pref.getString("account", "");
//                String password = pref.getString("password", "");
//                JsonObject jsonObject_member = new JsonObject();
//                jsonObject_member.addProperty("action", "findIdByAccountAndPassword");
//                jsonObject_member.addProperty("account", account);
//                jsonObject_member.addProperty("password", password);
//                String jsonOut_member = jsonObject_member.toString();
//                getMemberVOTask = new CommonTask(Util.URL + "MemberServlet", jsonOut_member);
//                try {
//                    String jsonIn = getMemberVOTask.execute().get();
//                    Gson gson = new Gson();
//                    member = gson.fromJson(jsonIn, Member.class);
//                    member_id = member.getMember_id();
//                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
//                }

                if (Util.networkConnected(CartActivity.this)) {
                    String member_id = pref.getString("member_id", "");
                    String url = Util.URL + "AnOrderServlet";

                    int sum = 0;
                    for (OrderProductVO orderProductVO : CART) {
                        Integer product_price = orderProductVO.getProduct_price();
//                        Log.e(TAG, "onClick: product_price: "+ product_price);
                        Integer quantity = orderProductVO.getQuantity();
//                        Log.e(TAG, "onClick: quantity: " + quantity );
//                        String product_id = orderProductVO.getProduct_id();

                        sum += product_price * quantity;

//                        Log.d(TAG, "onClick: sum被加了:" + sum);
                    }
//                    //判斷sum是否小於點數餘額
//                    JsonObject jsonObject_balanceEnough = new JsonObject();
//                    jsonObject_balanceEnough.addProperty("action", "balanceEnough");
//                    jsonObject_balanceEnough.addProperty("member_id", member_id);
//                    jsonObject_balanceEnough.addProperty("sum", sum);
//                    String jsonOut_balanceEnough = jsonObject_balanceEnough.toString();
//                    balanceEnoughTask = new CommonTask(Util.URL + "MemberServlet", jsonOut_balanceEnough);
//                    try {
//                        String enough = balanceEnoughTask.execute().get();
//                        if("false".equals(enough)){
//                            Util.showToast(CartActivity.this,"點數餘額不足");
//                            return;
//                        }
//
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    //////////////////////////////
//QRcode區
//                    EditText etQRCodeText = findViewById(R.id.etQRCodeText);
//                    String qrCodeText = etQRCodeText.getText().toString();//url
//                    String qrCodeText = "111";
//                    int smallerDimension = getDimension();
//                    switch (v.getId()) {
//                        case R.id.btnQRCode:
//                           Log.e(LOG_TAG, qrCodeText);
//                            // Encode with a QR Code image
//                            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrCodeText, null,
//                                    Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
//                                    smallerDimension);
//                            try {
//                                Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
//                                ivCode.setImageBitmap(bitmap);
//                            } catch (WriterException e) {
//                                e.printStackTrace();
//                            }
//                            break;

///////////////////////////////
                    order.setD_address(address);
                    Log.e(TAG, "onClick: address" + address);
                    order.setMember_id(member_id);
                    order.setTotal(sum);
                    //Cart存著OrderProuctVO集合
                    order.setOrderProductVOList(CART);
                    Log.d(TAG, "onClick: 購物車: " + CART);

                    String ordStr = gson.toJson(order);
                    Log.e("CART", ordStr);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "add");
                    //這邊就要包成訂單物件送過去
                    jsonObject.addProperty("member_id", member_id);
                    jsonObject.addProperty("order", ordStr);
                    //
//                    jsonObject.addProperty("qrcode", qrcode);

                    //
                    String jsonOut = jsonObject.toString();
                    orderAddTask = new CommonTask(url, jsonOut);
                    InstantDeliveryOrderVO successOrder = null;
                    try {
                        String result = orderAddTask.execute().get();
                        successOrder = gson.fromJson(result, InstantDeliveryOrderVO.class);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (successOrder == null) {
                        showToast(CartActivity.this, R.string.msg_FailCreateOrder);
                    } else {
//QRcode送的URL為http://192.168.43.132:8081/DA106_G4/OrderServlet?action=修改
                        CART.clear();
//                        Util.D_ORDER = successOrder;
                        String json_order = gson.toJson(successOrder);
                        pref.edit().putString("successOrder", json_order).apply();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order", successOrder);
                        Intent intentOrder = new Intent(CartActivity.this, OrderActivity.class);
                        intentOrder.putExtras(bundle);
                        startActivity(intentOrder);
                    }
                }

//        Intent loginIntent = new Intent(this, LoginActivity.class);
//        startActivityForResult(loginIntent, REQUEST_LOGIN);
            }
        });
        btnEmptyCart = findViewById(R.id.btnEmptyCart);
        btnEmptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CART == null || CART.size() <= 0) {
                    showToast(CartActivity.this, R.string.cartEmpty);
                    return;
                }
                String message = getString(R.string.msg_ClearCart);
                new AlertDialog.Builder(CartActivity.this)
                        .setIcon(R.drawable.cart)
                        .setTitle(R.string.btnClearCart)
                        .setMessage(message)
                        .setPositiveButton(R.string.text_btnSubmit,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        CART.clear();
                                        showTotal(CART);
                                        // notifyDataSetChanged()
                                        // refresh data set
                                        recyclerView.getAdapter().notifyDataSetChanged();
                                    }
                                })

                        .setNegativeButton(R.string.text_btnNo,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                    }
                                }).setCancelable(false).show();
            }
        });
        tvTotal = findViewById(R.id.tvTotal);


        recyclerView = findViewById(R.id.recyclerView_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new CartRecyclerViewAdapter(CartActivity.this, CART));
        tvAddress = findViewById(R.id.tv_address);
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                startActivityForResult(intent, LOGIN_REQUEST);
            }
        });
        tvCellphone = findViewById(R.id.tv_cellphone);
        tvEmail = findViewById(R.id.tv_email);
        //付款方式
        tvPayMethod = findViewById(R.id.tvPayMethod);
        tvResult_PayMethod = findViewById(R.id.tvResult_PayMethod);
        tvResult_PayMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"選到我了",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CartActivity.this, PayMethodActivity.class);

                startActivityForResult(intent, LOGIN_REQUEST);
            }
        });

        tvCouponCode = findViewById(R.id.tvCouponCode);
        tvCouponCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"選到我了",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CartActivity.this, CouponCodeActivity.class);
                startActivityForResult(intent, LOGIN_REQUEST);
            }
        });
        tvResult_Coupon = findViewById(R.id.tvResult_Coupon);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ssZ").create();
        showPersonalData();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showToast(CartActivity.this, "回傳回來");
        //判斷請求代碼是否相同，確認來源是否正確
        if (requestCode != LOGIN_REQUEST) {
            return;
        }
        Log.d(TAG, "onActivityResult: 進來了");

        if (resultCode == Util.RESULT_PAYMETHOD) {
            Bundle bundle = data.getExtras();

            payMethod = bundle.getString("payMethod", "付款方式選取失敗");
            Log.e(TAG, "onActivityResult: 取道付款方式: " + payMethod);
            if (payMethod != "付款方式選取失敗") {
                tvResult_PayMethod.setText(payMethod);
            }
        } else if (resultCode == Util.RESULT_ADDRESS) {
            Bundle bundle = data.getExtras();
            address = bundle.getString("member_address", "");
            Log.e(TAG, "onActivityResult: 取道地址為" + address);
            if (address == "") {
                Log.e(TAG, "onActivityResult: 地址未取到" + pref.getString("member_address", ""));
                address = pref.getString("member_address", "");
                tvAddress.setText("外送地址: " + address);
            } else {
//                Log.e(TAG, "onActivityResult: 地址有取道" + address );
                tvAddress.setText("外送地址: " + address);
            }
        } else if (resultCode == Util.RESULT_COUPON) {
            Bundle bundle = data.getExtras();
            couponVO = (CouponVO) (bundle.getSerializable("couponVO"));
            if (couponVO != null) {
                couponCode = couponVO.getCoupon_code();
//                Log.d(TAG, "onActivityResult: 有拿到couponVO" + couponVO);
                useDiscount = true;
                tvResult_Coupon.setText(couponCode);
            }


        }

        if ("貨到付款".equals(payMethod)) {
            order.setP_method(1);
            order.setP_status(1);//未繳費
            Log.e(TAG, "onActivityResult: 我是貨到付款: " + order.getP_method());
        } else if ("點數支付".equals(payMethod)) {
            order.setP_method(0);
            Log.e(TAG, "onActivityResult: 我是點數支付: " + order.getP_method());
        } else if ("線上刷卡".equals(payMethod)) {
            order.setP_method(2);
            Log.e(TAG, "onActivityResult: 我是信用卡支付: " + order.getP_method());
        }


        //判斷是否有用優惠卷

        if (useDiscount && couponVO != null) {
            for (OrderProductVO orderProductVO : CART) {
//                        Log.d(TAG, "onClick: 進來優惠卷使用了");
                //抓出優惠卷對應的商品
                //利用得到的優惠卷物件去做非同步連線抓對應商品
                String c_no = couponVO.getC_no();
//                            Log.e(TAG, "onClick: c_no:"+ c_no );

                ArrayList<ProductVO> arrayList = new ArrayList<>();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getSomeByC_no");
                jsonObject.addProperty("c_no", c_no);
                String jsonOut = jsonObject.toString();
                getProductVOforCouponTask = new CommonTask(Util.URL + "AnProductServlet", jsonOut);

                //拿出對應商品的arraylist
                try {
                    String jsonIn = getProductVOforCouponTask.execute().get();
                    Type listType = new TypeToken<List<ProductVO>>() {
                    }.getType();
                    Gson gson = new Gson();
                    arrayList = gson.fromJson(jsonIn, listType);
//                                Log.e(TAG, "onClick: 優惠卷對應商品有:" + arrayList );
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //把cart裡面的商品去比對arraylist裡面的商品
                //如果商品ID相符，將打折過後的價格設定進購物車商品的價格
                for (ProductVO aProductVO : arrayList) {
                    if (orderProductVO.getProduct_id().equals(aProductVO.getProduct_id())) {
//                                    Log.e(TAG, "onClick: 購物車商品ID: " + orderProductVO.getProduct_id());
//                                    Log.e(TAG, "onClick: 優惠卷商品ID: " + aProductVO.getProduct_id());
                        Integer price_discount = orderProductVO.getProduct_price() * couponVO.getDiscount() / 100;
//                                    Log.e(TAG, "onClick: 商品價格:" + orderProductVO.getProduct_price());
//                                    Log.e(TAG, "onClick: 打折數:" + ((couponVO.getDiscount())/100));
//                                    Log.e(TAG, "onClick: 打折後價格: " + price_discount );
                        Integer product_price = price_discount;
                        orderProductVO.setProduct_price(product_price);
                        showTotal(CART);
                        Log.e(TAG, "onActivityResult: " + CART.indexOf(orderProductVO));
                        View view = recyclerView.getLayoutManager().findViewByPosition(CART.indexOf(orderProductVO));
                        TextView textView = view.findViewById(R.id.tvCartBookPrice);
                        textView.setTextColor(0xffff5757);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
            }
        }
        showPersonalData();
    }




    @Override
    public void onStart() {
        super.onStart();
        showTotal(CART);
        showPersonalData();

    }


    private void showTotal(List<OrderProductVO> orderProductVOList) {
        double total = 0;
        for (OrderProductVO orderProductVO : orderProductVOList) {
            total += orderProductVO.getProduct_price() * orderProductVO.getQuantity();
        }
        String text = "Total: " + total;
        if (useDiscount) {
            tvTotal.setTextColor(0xffff5757);
        }
        tvTotal.setText(text);

    }


    private void showPersonalData() {
        tvAddress.setText("外送地址: " + address);
        tvEmail.setText("信箱: " + email);
        tvCellphone.setText("手機: " + cellphone);
        tvPayMethod.setText("付款方式:");
    }

    public void onCheckoutClick(View view) {

    }

    public void onEmptyCartClick(View view) {

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_LOGIN:
//                if (resultCode == RESULT_OK) {
//                    SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
//                    String userId = pref.getString("userId", "");
//                    if (Util.networkConnected(this)) {
//                        String url = Util.URL + "OrderServlet";
//
//                        OrderMaster order = new OrderMaster();
//                        order.setUserId(userId);
//                        int sum = 0;
//                        for (OrderBook book : CART) {
//                            sum += book.getPrice() * book.getQuantity();
//                        }
//                        order.setAmount(sum);
//                        order.setOrderBookList(CART);
//
//                        String ordStr = gson.toJson(order);
//                        Log.e("CART", ordStr);
//                        JsonObject jsonObject = new JsonObject();
//                        jsonObject.addProperty("action", "add");
//                        jsonObject.addProperty("userId", userId);
//                        jsonObject.addProperty("order", ordStr);
//                        String jsonOut = jsonObject.toString();
//                        orderAddTask = new CommonTask(url, jsonOut);
//                        OrderMaster successOrder = null;
//                        try {
//                            String result = orderAddTask.execute().get();
//                            successOrder = gson.fromJson(result, OrderMaster.class);
//                        } catch (Exception e) {
//                            Log.e(TAG, e.toString());
//                        }
//
//                        if (successOrder == null) {
//                            Util.showToast(CartActivity.this, R.string.msg_FailCreateOrder);
//                        } else {
//                            CART.clear();
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("order", successOrder);
//                            Intent intentOrder = new Intent(this, OrderActivity.class);
//                            intentOrder.putExtras(bundle);
//                            startActivity(intentOrder);
//                        }
//                    }
//                }
//                break;
//        }
//    }

private class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<OrderProductVO> orderProductVOList;
    private int imageSize;

    CartRecyclerViewAdapter(Context context, List<OrderProductVO> orderProductVOList) {
        this.context = context;
        this.orderProductVOList = orderProductVOList;
        imageSize = getResources().getDisplayMetrics().widthPixels / 4;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView ivCartBookImage, ivCartRemoveItem;
        TextView tvCartBookName, tvCartBookPrice;
        Spinner spCartBookQuantity;

        MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivCartBookImage = itemView.findViewById(R.id.ivCartBookImage);
            ivCartRemoveItem = itemView.findViewById(R.id.ivCartRemoveItem);
            tvCartBookName = itemView.findViewById(R.id.tvCartBookName);
            tvCartBookPrice = itemView.findViewById(R.id.tvCartBookPrice);
            spCartBookQuantity = itemView.findViewById(R.id.spCartBookQuantity);

        }
    }

    @Override
    public int getItemCount() {
//            if (CART.size() <= 0) {
//                layoutEmpty.setVisibility(View.VISIBLE);
//            } else {
//                layoutEmpty.setVisibility(View.GONE);
//            }
        return orderProductVOList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cardview_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final OrderProductVO orderProductVO = orderProductVOList.get(position);

        String url = Util.URL + "AnProductServlet";
        String recipe_id = orderProductVO.getRecipe_id();
        productImageTask = new ImageTask(url, recipe_id, imageSize, holder.ivCartBookImage);
        productImageTask.execute();
        holder.tvCartBookName.setText(orderProductVO.getProduct_name());

        holder.tvCartBookPrice.setText(String.valueOf(orderProductVO.getProduct_price()));
//            holder.tvCartBookPrice.setTextColor(0xffff5757);


        holder.spCartBookQuantity.setSelection(orderProductVO.getQuantity() - 1, true);
        holder.spCartBookQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                int quantity = Integer.parseInt(parent
                        .getItemAtPosition(position).toString());
                orderProductVO.setQuantity(quantity);
                showTotal(CART);
                showToast(context,
                        getString(R.string.msg_NewQuantity) + " " +
                                orderProductVO.getQuantity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        holder.ivCartRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = getString(R.string.cartRemove) + "「"
                        + orderProductVO.getProduct_name() + "」?";
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.cart)
                        .setTitle(R.string.cartRemove)
                        .setMessage(message)
                        .setPositiveButton(R.string.text_btnYes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        CART.remove(orderProductVO);
                                        showTotal(CART);
                                        CartRecyclerViewAdapter.this
                                                .notifyDataSetChanged();
                                    }
                                })
                        .setNegativeButton(R.string.text_btnNo,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.cancel();
                                    }
                                }).setCancelable(false).show();
            }
        });
    }

}

// Qrcode區
//    private int getDimension() {
//        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        // 取得螢幕尺寸
//        Display display = manager.getDefaultDisplay();
//        // API 13列為deprecated，但為了支援舊版手機仍採用
//        int width = display.getWidth();
//        int height = display.getHeight();
//
//        // 產生的QR code圖形尺寸(正方形)為螢幕較短一邊的1/2長度
//        int smallerDimension = width < height ? width : height;
//        smallerDimension = smallerDimension / 2;
//
//        // API 13開始支援
////                Display display = manager.getDefaultDisplay();
////                Point point = new Point();
////                display.getSize(point);
////                int width = point.x;
////                int height = point.y;
////                int smallerDimension = width < height ? width : height;
////                smallerDimension = smallerDimension / 2;
//        return smallerDimension;
//    }
    //
    @Override
    public void onStop() {
        super.onStop();
        if (orderAddTask != null) {
            orderAddTask.cancel(true);
        }
        if (productImageTask != null) {
            productImageTask.cancel(true);
        }
    }

}
