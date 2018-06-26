package com.example.huangxinhui.erpapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huangxinhui.erpapp.Adapter.MoveAdapter;
import com.example.huangxinhui.erpapp.JavaBean.Query;
import com.example.huangxinhui.erpapp.R;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MoveFragment extends Fragment {
    Unbinder unbinder;
    List<Query.DataBean.Info> data;
    @BindView(R.id.list_query)
    RecyclerView listMove;


    public static MoveFragment getInstance(ArrayList<Query.DataBean.Info> data) {
        MoveFragment mf = new MoveFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        mf.setArguments(bundle);
        return mf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.data = (List<Query.DataBean.Info>) getArguments().getSerializable("data");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_query, container, false);
        unbinder = ButterKnife.bind(this, view);
        listMove.setLayoutManager(new LinearLayoutManager(getActivity()));
        listMove.setAdapter(new MoveAdapter(data,getActivity()));
        ScreenAdapterTools.getInstance().loadView(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
