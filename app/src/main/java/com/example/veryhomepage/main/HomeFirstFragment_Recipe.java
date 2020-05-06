package com.example.veryhomepage.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.veryhomepage.R;
import com.example.veryhomepage.member.Member;
import com.example.veryhomepage.recipe_liveStream.CallFragment;
import com.example.veryhomepage.recipe_liveStream.RecyclerViewFragment;
import com.example.veryhomepage.recipe_liveStream.StatusFragment;
import com.example.veryhomepage.recipe_liveStream.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFirstFragment_Recipe extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Member member;
//    private View view;

    public HomeFirstFragment_Recipe() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        if (view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent != null) {
//                parent.removeView(view);
//            }
//            return view;
//        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_first_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = getActivity().findViewById(R.id.myToolbar);
        tabLayout = getActivity().findViewById(R.id.tablayout);
        viewPager = getActivity().findViewById(R.id.myViewPager);
//        viewPager.setOffscreenPageLimit(2);

//        //巢狀fragment不能用setSupportActionBar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar); //setToolbar
        setupViewPager(viewPager); //setViewPager
        tabLayout.setupWithViewPager(viewPager); //Adapter轉換資料
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new RecyclerViewFragment(), "食譜");
        viewPagerAdapter.addFragment(new StatusFragment(), "直播");
        viewPagerAdapter.addFragment(new CallFragment(), "其他");
        viewPager.setAdapter(viewPagerAdapter);
    }
}
