package com.example.huangxinhui.erpapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huangxinhui.erpapp.Adapter.OutAdapter;
import com.example.huangxinhui.erpapp.JavaBean.Query;
import com.example.huangxinhui.erpapp.R;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OutFragment extends Fragment {

    Unbinder unbinder;
    List<Query.DataBean.Info> data;
    @BindView(R.id.list_out)
    RecyclerView listOut;

    public static OutFragment getIntance(ArrayList<Query.DataBean.Info> data) {
        OutFragment qf = new OutFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        qf.setArguments(bundle);
        return qf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (List<Query.DataBean.Info>) getArguments().getSerializable("data");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_fragment_out, container, false);
        ScreenAdapterTools.getInstance().loadView(view);
        unbinder = ButterKnife.bind(this, view);
        listOut.setLayoutManager(new LinearLayoutManager(getActivity()));
        listOut.setAdapter(new OutAdapter(data, getActivity()));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
