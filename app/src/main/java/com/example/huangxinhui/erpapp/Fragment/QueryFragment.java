package com.example.huangxinhui.erpapp.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.huangxinhui.erpapp.Adapter.QueryAdapter;
import com.example.huangxinhui.erpapp.JavaBean.Query;
import com.example.huangxinhui.erpapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class QueryFragment extends Fragment {
    Unbinder unbinder;

    List<Query.DataBean.Info> data;
    @BindView(R.id.list_query)
    RecyclerView listQuery;

    public static QueryFragment getInstance(ArrayList<Query.DataBean.Info> data) {
        QueryFragment f = new QueryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (List<Query.DataBean.Info>) getArguments().getSerializable("data");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_query, container, false);
        unbinder = ButterKnife.bind(this, view);
        listQuery.setLayoutManager(new LinearLayoutManager(getActivity()));
        listQuery.setAdapter(new QueryAdapter(data, getActivity()));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
