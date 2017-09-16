package com.santiago.zm.santiagoplay.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.santiago.zm.santiagoplay.R;
import com.santiago.zm.santiagoplay.bean.HistoryResult;
import com.santiago.zm.santiagoplay.bridge.OnRecyclerItemClickListener;

import java.util.List;

/**
 * Created by Santiago on 2017/9/2.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<HistoryResult> results;
    OnRecyclerItemClickListener mListener;

    public HistoryAdapter(List<HistoryResult> results){
        this.results = results;
    }

    public void update(List<HistoryResult> updateResult){
        results = updateResult;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final HistoryAdapter.ViewHolder holder, final int position) {
        HistoryResult result = results.get(position);
        holder.title.setText(result.title);
        holder.date.setText(result.year+"年"+result.month+"月"+result.day+"日");
        if (mListener!=null){

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,date;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
        }
    }

    public void setOnItemClick(OnRecyclerItemClickListener listener){
        mListener = listener;
    }
}
