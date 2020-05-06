package com.example.veryhomepage.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.veryhomepage.R;
import com.example.veryhomepage.main.Util;

public class CreditCardActivity extends AppCompatActivity {
    private EditText tiletName, tiletCreditNum, tiletCreditDate, tiletCheckNum;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        findViews();
        setData();
    }

    private void findViews() {
        tiletName = findViewById(R.id.tiletName);
        tiletCreditNum = findViewById(R.id.tiletCreditNum);
        tiletCreditDate = findViewById(R.id.tiletCreditDate);
        tiletCheckNum = findViewById(R.id.tiletCheckNum);
    }

    private void setData() {
        sharedPreferences = getSharedPreferences("preference", MODE_PRIVATE);
        String member_name = sharedPreferences.getString("member_name","");
        tiletName.setText(member_name);
    }

    public void onUseClick(View view) {
        Bundle data = getIntent().getExtras();
        String payMethod = data.getString("payMethod");
        String creditName = tiletName.getText().toString();
        String creditNum = tiletCreditNum.getText().toString();
        String creditDate = tiletCreditDate.getText().toString();
        String checkNum = tiletCheckNum.getText().toString();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("creditName", creditName);
        bundle.putString("creditNum", creditNum);
        bundle.putString("creditDate", creditDate);
        bundle.putString("checkNum", checkNum);
        bundle.putString("payMethod", payMethod);

        intent.putExtras(bundle);
        setResult(Util.RESULT_PAYMETHOD, intent);
        finish();
    }

}
