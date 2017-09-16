package com.santiago.zm.santiagoplay.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.santiago.zm.santiagoplay.R;
import com.santiago.zm.santiagoplay.adapter.MyFragmentPagerAdapter;
import com.santiago.zm.santiagoplay.utils.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager vp = (ViewPager) findViewById(R.id.viewpager);
        getFragments();
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        vp.setAdapter(adapter);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.navigation);
        tabStrip.setViewPager(vp);
    }

    public void getFragments() {
        fragments = new ArrayList<>();
        HistoryTodayFragment history = new HistoryTodayFragment();
        fragments.add(history);
        HotArticleFragment hot = new HotArticleFragment();
        fragments.add(hot);
        RecommendArticleFragment recommend = new RecommendArticleFragment();
        fragments.add(recommend);
        JokeArticleFragment joke = new JokeArticleFragment();
        fragments.add(joke);
        HealthArticleFragment health = new HealthArticleFragment();
        fragments.add(health);
        SecretArticleFragment secret = new SecretArticleFragment();
        fragments.add(secret);
    }
}
