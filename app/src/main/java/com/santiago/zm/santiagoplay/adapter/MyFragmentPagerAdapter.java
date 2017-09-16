package com.santiago.zm.santiagoplay.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.santiago.zm.santiagoplay.constant.Constant;

import java.util.List;

/**
 * Created by Santiago on 2017/9/2.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> mFragments;


    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Constant.titles[position];
    }
}
