package com.example.huangxinhui.erpapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.huangxinhui.erpapp.JavaBean.Query;
import com.example.huangxinhui.erpapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OutAdapter extends RecyclerView.Adapter<OutAdapter.ViewHolder> {
    private List<Query.DataBean.Info> data;
    private LayoutInflater inflater;
    public OutAdapter(List<Query.DataBean.Info> data, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
        public OutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OutAdapter.ViewHolder holder = new OutAdapter.ViewHolder(inflater.inflate(R.layout.list_information, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(data.get(position).getKey());
        holder.num.setText(data.get(position).getValue());
    }


    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.num)
        TextView num;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
