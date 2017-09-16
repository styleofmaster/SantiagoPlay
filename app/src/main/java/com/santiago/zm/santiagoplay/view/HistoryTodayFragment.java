package com.santiago.zm.santiagoplay.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.santiago.zm.santiagoplay.R;
import com.santiago.zm.santiagoplay.adapter.HistoryAdapter;
import com.santiago.zm.santiagoplay.bean.HistoryResult;
import com.santiago.zm.santiagoplay.bean.RequestDate;
import com.santiago.zm.santiagoplay.bridge.OnRecyclerItemClickListener;
import com.santiago.zm.santiagoplay.utils.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Santiago on 2017/9/2.
 */

public class HistoryTodayFragment extends Fragment {

    int width, height;
    PopupWindow pop;
    View popView;
    int count;
    HistoryAdapter adapter;
    Handler handler;
    ProgressBar pb;
    RecyclerView rv;
    List<HistoryResult> results;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        initView(view);

//        rv.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));

        return view;
    }


    private void initView(View view){
        popView = getActivity().getLayoutInflater().inflate(R.layout.pop_history, null);
        width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        height = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        rv = view.findViewById(R.id.recycler);
        handler = new Handler();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        pb = view.findViewById(R.id.progress);

        new Thread(new Runnable() {
            @Override
            public void run() {
                results = HttpUtil.getHistory(getRequestDate());
                adapter = new HistoryAdapter(results);
                adapter.setOnItemClick(new OnRecyclerItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), HistoryContentActivity.class);
                        intent.putExtra("content", results.get(position).content);
                        startActivity(intent);
                    }
                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.GONE);
                        rv.setAdapter(adapter);
                    }
                });


            }
        }).start();

        final Button popBtn = view.findViewById(R.id.his_menu);
        popBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pop == null){
                    initPop();
                }
                pop.showAsDropDown(popBtn,0,-220);
            }
        });
    }

    private void initPop() {
        pop = new PopupWindow(popView, width, 250);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        pop.update();
        popView.findViewById(R.id.pop_last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count--;
                getDataFromDate();
            }
        });

        popView.findViewById(R.id.pop_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                getDataFromDate();
            }
        });

        popView.findViewById(R.id.pop_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                getDataFromDate();
            }
        });
    }

    public RequestDate getRequestDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,count);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = format.format(calendar.getTime());
        String[] dates = yesterday.split("-");
        RequestDate rd = new RequestDate(Integer.parseInt(dates[1]),Integer.parseInt(dates[2]));

        return rd;
    }

    public void getDataFromDate(){
        rv.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                results = HttpUtil.getHistory(getRequestDate());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        adapter.update(results);
                    }
                });
            }
        }).start();

    }
}
