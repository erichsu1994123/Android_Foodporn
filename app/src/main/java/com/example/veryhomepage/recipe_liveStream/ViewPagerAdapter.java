package com.example.veryhomepage.recipe_liveStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentsList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }


    @Override
    public int getCount() {
        return fragmentsList.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) { return fragmentTitle.get(position); }

    public void addFragment(Fragment fragment, String title){
        fragmentsList.add(fragment);
        fragmentTitle.add(title);
    }

}
