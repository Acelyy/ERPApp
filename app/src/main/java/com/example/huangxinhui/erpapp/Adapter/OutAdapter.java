package com.example.huangxinhui.erpapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.huangxinhui.erpapp.JavaBean.Query;
import com.example.huangxinhui.erpapp.R;

import java.util.List;

public class OutAdapter extends RecyclerView.Adapter<OutAdapter.ViewHolder> {
    private static final int TYPE_LAYOUT = 1;
    private static final int TYPE_BUTTON = 2;
    private OutAdapter.OnButtonClickListener onButtonClickListener;
    private List<Query.DataBean.Info> data;
    private LayoutInflater inflater;

    public OutAdapter(List<Query.DataBean.Info> data, Context context) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    public void setOnButtonClickListener(OutAdapter.OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            return TYPE_BUTTON;
        } else {
            return TYPE_LAYOUT;
        }
    }

    @Override
    public OutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BUTTON) {
            return new OutAdapter.ViewHolder(inflater.inflate(R.layout.list_button, parent, false));
        } else {
            return new OutAdapter.ViewHolder(inflater.inflate(R.layout.list_information, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_LAYOUT:
                holder.name.setText(data.get(position).getKey());
                holder.num.setText(data.get(position).getValue());
                break;
            case TYPE_BUTTON:
                holder.button.setText("点击出库");
                if (onButtonClickListener != null) {
                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onButtonClickListener.onCLick(view, position);
                        }
                    });
                }
                break;
        }
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size()+1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        TextView num;

        Button button;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            num = view.findViewById(R.id.num);
            button = view.findViewById(R.id.button);
        }
    }

    /**
     * 按钮的点击事件
     */
    public interface OnButtonClickListener {
        void onCLick(View view, int position);
    }
}
