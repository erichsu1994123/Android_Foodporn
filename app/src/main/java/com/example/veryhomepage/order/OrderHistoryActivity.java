package com.example.veryhomepage.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.veryhomepage.R;
import com.example.veryhomepage.main.Util;
import com.example.veryhomepage.task.CommonTask;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderHistoryActivity extends AppCompatActivity {
    private final static String TAG = "OrderHistoryActivity";
    private String member_id;
//    private RadioGroup rgSearch;
//    private LinearLayout layoutDate;
    private ListView lvOrderHistory;
    private CommonTask orderGetAllTask;
//    private Button btnStartDate, btnEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
//        btnStartDate = findViewById(R.id.btnStartDate);
//        btnEndDate = findViewById(R.id.btnEndDate);
        member_id = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE).getString("member_id", "");
        if (member_id.isEmpty()) {
            finish();
        }
        lvOrderHistory = findViewById(R.id.lvOrderHistory);
//        layoutDate = findViewById(R.id.layoutDate);
//        rgSearch = findViewById(R.id.rgSearch);
//        rgSearch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // if rbByDate is checked, show date picker dialog
//                if (checkedId == R.id.rbByDate) {
//                    layoutDate.setVisibility(View.VISIBLE);
//                    showNow();
//                    String start = btnStartDate.getText().toString();
//                    String end = btnEndDate.getText().toString();
//                    showOrders(start, end);
//                } else {
//                    layoutDate.setVisibility(View.GONE);
//                    showOrders("", "");
//                }
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        showOrders("", "");
    }

    // click Search by Date radio button
//    public void onByDateClick(View view) {
//        layoutDate.setVisibility(View.VISIBLE);
//    }
//
//    // click Start Date or End Date button
//    public void onDateClick(View view) {
//        DatePickerFragment datePickerFragment = new DatePickerFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("resId", view.getId());
//        datePickerFragment.setArguments(bundle);
//        FragmentManager fm = getSupportFragmentManager();
//        datePickerFragment.show(fm, "datePicker");
//    }

    public void onAllClick(View view) {
        showOrders("", "");
    }

    // click Search button
//    public void onSearchClick(View view) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//        String start = btnStartDate.getText().toString();
//        String end = btnEndDate.getText().toString();
//        try {
//            Date startDate = simpleDateFormat.parse(start);
//            Date endDate = simpleDateFormat.parse(end);
//            // end date should not be less than start date
//            if (startDate.after(endDate)) {
//                Toast.makeText(
//                        this,
//                        R.string.msg_EndDateNotLessThanStartDate,
//                        Toast.LENGTH_SHORT)
//                        .show();
//                return;
//            }
//        } catch (ParseException e) {
//            Log.e(TAG, e.toString());
//        }
//        showOrders(start, end);
//    }

    private void showOrders(String start, String end) {
        if (Util.networkConnected(this)) {
            String url = Util.URL + "AnOrderServlet";
            List<InstantDeliveryOrderVO> orders = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            jsonObject.addProperty("member_id", member_id);
            jsonObject.addProperty("start", start);
            jsonObject.addProperty("end", end);
            String jsonOut = jsonObject.toString();
            orderGetAllTask = new CommonTask(url, jsonOut);
            try {
                String result = orderGetAllTask.execute().get();
                Type listType = new TypeToken<List<InstantDeliveryOrderVO>>() {}.getType();

                orders = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(result, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (orders == null || orders.isEmpty()) {
                //空值放進去Adapter會爆炸
                orders = new ArrayList<>();
                lvOrderHistory.setAdapter(new OrdersAdapter(this, orders));
                Util.showToast(this, R.string.msg_NoOrderFound);
            } else {
                lvOrderHistory.setAdapter(new OrdersAdapter(this, orders));
                lvOrderHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        InstantDeliveryOrderVO order = (InstantDeliveryOrderVO) parent.getItemAtPosition(position);
                        Intent intent = new Intent(OrderHistoryActivity.this, OrderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order", order);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        } else {
            Util.showToast(this, R.string.msg_NoNetwork);
        }
    }

//    public static class DatePickerFragment extends DialogFragment implements
//            DatePickerDialog.OnDateSetListener {
//        FragmentActivity activity;
//        Bundle bundle;
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            activity = getActivity();
//            bundle = getArguments();
//            if (activity == null || bundle == null) {
//                Log.e(TAG, "activity or bundle is null");
//            }
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            int resId = bundle.getInt("resId");
//            Button button = activity.findViewById(resId);
//
//            // DatePickerDialog will show the date on the clicked button without parse exception
//            int year, month, day;
//            Calendar calendar = Calendar.getInstance();
//            try {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//                Date date = simpleDateFormat.parse(button.getText().toString());
//                calendar.setTime(date);
//            } catch (ParseException e) {
//                Log.e(TAG, e.toString());
//            } finally {
//                year = calendar.get(Calendar.YEAR);
//                month = calendar.get(Calendar.MONTH);
//                day = calendar.get(Calendar.DAY_OF_MONTH);
//            }
//            return new DatePickerDialog(activity, this, year, month, day);
//        }
//
//        @Override
//        // display the date on the clicked button
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            int resId = bundle.getInt("resId");
//            updateDisplay(activity, resId, year, month, day);
//        }
//    }
//
//    private void showNow() {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        updateDisplay(this, R.id.btnStartDate, year, month, day);
//        updateDisplay(this, R.id.btnEndDate, year, month, day);
//    }

//    /**
//     * update the date information on the clicked button
//     *
//     * @param activity current activity
//     * @param resId    resource ID of the clicked button
//     */
//    private static void updateDisplay(Activity activity, int resId, int year, int month, int day) {
//        Button button = activity.findViewById(resId);
//        button.setText(new StringBuilder().append(year).append("-")
//                .append(pad(month + 1)).append("-").append(pad(day)));
//    }
//
//    private static String pad(int number) {
//        if (number >= 10)
//            return String.valueOf(number);
//        else
//            return "0" + String.valueOf(number);
//    }

    private class OrdersAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<InstantDeliveryOrderVO> orders;

        OrdersAdapter(Context context, List<InstantDeliveryOrderVO> orders) {
            this.layoutInflater = LayoutInflater.from(context);
            this.orders = orders;
        }

        @Override
        public int getCount() {
            if (orders == null || orders.size() <= 0) {
                return 0;
            }
            return orders.size();
        }

        @Override
        public Object getItem(int position) {
            return orders.get(position);
        }

        @Override
        public long getItemId(int position) { return 0; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            InstantDeliveryOrderVO order = orders.get(position);
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.listview_ordermaster, parent, false);
            }
            TextView tvOrderId = convertView.findViewById(R.id.tvOrderId);
            tvOrderId.setText(order.getIdo_no());

            TextView tvCustomer = convertView.findViewById(R.id.tvCustomer);
            tvCustomer.setText(order.getMember_id());

            List<OrderProductVO> orderProductVOList = order.getOrderProductVOList();
            double sum = 0;
            for (OrderProductVO orderProductVO : orderProductVOList) {
                sum += orderProductVO.getProduct_price() * orderProductVO.getQuantity();
            }
            TextView tvSum = convertView.findViewById(R.id.tvSum);
            tvSum.setText(String.valueOf(sum));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            TextView tvDate = convertView.findViewById(R.id.tvDate);
//            tvDate.setText(simpleDateFormat.format(order.getTimestamp())); 將來訂單完成時要補上
            return convertView;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (orderGetAllTask != null) {
            orderGetAllTask.cancel(true);
        }
    }
}
