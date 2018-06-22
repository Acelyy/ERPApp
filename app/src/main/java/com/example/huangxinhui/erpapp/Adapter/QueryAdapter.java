package com.example.huangxinhui.erpapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.huangxinhui.erpapp.JavaBean.Query;
import com.example.huangxinhui.erpapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QueryAdapter extends BaseAdapter {

    private List<Query.DataBean.Info> data;
    private LayoutInflater inflater;

    public QueryAdapter(List<Query.DataBean.Info> data, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_information, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(data.get(position).getKey());
        holder.num.setText(data.get(position).getValue());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.num)
        TextView num;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
