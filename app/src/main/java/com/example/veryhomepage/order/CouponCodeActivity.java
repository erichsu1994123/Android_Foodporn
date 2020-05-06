package com.example.veryhomepage.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.veryhomepage.R;
import com.example.veryhomepage.coupon.CouponVO;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

public class CouponCodeActivity extends AppCompatActivity {
    private final String TAG ="CouponCodeActivity";
    private EditText etCouponCode;
    private Button btnCouponCode;
    private CommonTask commonTask;
    private Gson gson;
    private CouponVO couponVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_code);
        findViews();

    }

    private void findViews() {
        etCouponCode = findViewById(R.id.etCouponCode);
        btnCouponCode = findViewById(R.id.btnCouponCode);
        btnCouponCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coupon_code = etCouponCode.getText().toString();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "discount");
                jsonObject.addProperty("coupon_code", coupon_code);
                String jsonOut = jsonObject.toString();
                commonTask = new CommonTask(Util.URL + "AnCouponServlet", jsonOut);
                try {
                    String jsonIn = commonTask.execute().get();
                    Log.d(TAG, "onClick: "+ jsonIn);
                    gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ssZ").create();
                    couponVO = gson.fromJson(jsonIn, CouponVO.class);

//                    Util.showToast(CouponCodeActivity.this,"優惠卷打折數:"+ discount);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("couponVO", couponVO);
                intent.putExtras(bundle);
                setResult(Util.RESULT_COUPON, intent);
                finish();
            }
        });
    }
}
