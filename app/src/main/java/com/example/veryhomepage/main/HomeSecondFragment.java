package com.example.veryhomepage.main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.veryhomepage.R;
import com.example.veryhomepage.order.CartActivity;
import com.example.veryhomepage.order.DeliveryActivity;
import com.example.veryhomepage.order.OrderHistoryActivity;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeSecondFragment extends Fragment {
    private Button btnLogout, btnOrderHistory, btnDelivery, btnCart;

    public HomeSecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_second, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.showToast(getActivity(),getActivity().getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE).getString("member_id", ""));
        btnLogout = getActivity().findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getSharedPreferences(Util.PREF_FILE,
                        MODE_PRIVATE);
                pref.edit().putBoolean("login", false).apply();
                v.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnOrderHistory = getActivity().findViewById(R.id.btnOrderHistory);
        btnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
                startActivity(intent);
            }
        });

        btnDelivery = getActivity().findViewById(R.id.btnDelivery);
        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeliveryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", Util.D_ORDER);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnCart = getActivity().findViewById(R.id.btnCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onLogoutClick(View view) {

    }


}
