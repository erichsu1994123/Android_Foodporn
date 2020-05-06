package com.example.veryhomepage.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.veryhomepage.R;
import com.example.veryhomepage.adapters_recyclerview.HorizontalRecyclerViewAdapter;
import com.example.veryhomepage.adapters_recyclerview.models.HorizontalModel;
import com.example.veryhomepage.coupon.CouponVO;
import com.example.veryhomepage.recipe.RecipeVO;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFourthFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";
    private androidx.recyclerview.widget.RecyclerView verticalRecyclerView;
    private ArrayList<HorizontalModel> arrayListVertical;
    private HorizontalRecyclerViewAdapter adapter;

    public HomeFourthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_fourth, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        arrayListVertical = new ArrayList<>();
        verticalRecyclerView = getActivity().findViewById(R.id.recyclerView_1);
        verticalRecyclerView.setHasFixedSize(true);
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapter = new HorizontalRecyclerViewAdapter(getActivity(), arrayListVertical);

        verticalRecyclerView.setAdapter(adapter);

        setData();
    }

    public void setData() {
        HorizontalModel mHorizontalModel = null;

        mHorizontalModel = new HorizontalModel();
        mHorizontalModel.setTitle("優惠卷");
        ArrayList<CouponVO> arrayList = new ArrayList<>();
        CouponVO mCouponVO = new CouponVO();
        arrayList.add(mCouponVO);
        mHorizontalModel.setArrayList(arrayList);
        arrayListVertical.add(mHorizontalModel);

//        mVerticalModel = new VerticalModel();
//        mVerticalModel.setTitle("食材");
//        ArrayList<ProductVO> arrayList1 = new ArrayList<>();
//        ProductVO mProductVO = new ProductVO();
//        arrayList1.add(mProductVO);
//        mVerticalModel.setArrayList1(arrayList1);
//        arrayListVertical.add(mVerticalModel);

        mHorizontalModel = new HorizontalModel();
        mHorizontalModel.setTitle("熱門料理包");
        ArrayList<RecipeVO> arrayList2 = new ArrayList<>();
        RecipeVO mRecipeVO = new RecipeVO();
        arrayList2.add(mRecipeVO);
        mHorizontalModel.setArrayList2(arrayList2);
        arrayListVertical.add(mHorizontalModel);
        adapter.notifyDataSetChanged();
    }
}
