package com.example.veryhomepage.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.veryhomepage.R;
import com.example.veryhomepage.main.Util;

public class PayMethodActivity extends AppCompatActivity {
    private final int LOGIN_REQUEST = 0;
    private final String TAG = "PayMethodActivity";
    private RadioGroup rgBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_method);
        findViews();
    }

    private void findViews() {
        rgBrand = findViewById(R.id.rgBrand);

        rgBrand.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton rb = radioGroup.findViewById(id);
                String payMethod = rb.getText().toString();

                Log.d(TAG, "onCheckedChanged: " + payMethod);
                if ("貨到付款".equals(payMethod)) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("payMethod", payMethod);
                    intent.putExtras(bundle);
                    setResult(Util.RESULT_PAYMETHOD, intent);
                    finish();
                } else if ("點數支付".equals(payMethod)) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("payMethod", payMethod);
                    intent.putExtras(bundle);
                    setResult(Util.RESULT_PAYMETHOD, intent);
                    finish();
                } else if ("線上刷卡".equals(payMethod)) {
                    Intent intent = new Intent(PayMethodActivity.this, CreditCardActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("payMethod", payMethod);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, LOGIN_REQUEST);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != LOGIN_REQUEST) {
            return;
        }

        if (resultCode == Util.RESULT_PAYMETHOD){
            setResult(Util.RESULT_PAYMETHOD, data);
            finish();
        }
    }
}