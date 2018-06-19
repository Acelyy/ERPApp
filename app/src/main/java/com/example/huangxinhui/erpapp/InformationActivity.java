package com.example.huangxinhui.erpapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InformationActivity extends AppCompatActivity {

    @BindView(R.id.list)
    ExpandableListView list;
    private String[] titles = {"基本信息","化学成分信息"};
    private String[][] ints = {
            {"炉 号","生产日期","班 次","连铸机号","状 态","用 途","去 向","场 型","长 度"},
            {"C","Sl","K","S","Mn","PE","H","O"}
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);
        ExpandableListAdapter adapter = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return titles.length;
            }

            @Override
            public int getChildrenCount(int i) {
                return ints[i].length;
            }

            @Override
            public Object getGroup(int i) {
                return titles[i];
            }

            @Override
            public Object getChild(int i, int i1) {
                return ints[i][i1];
            }

            @Override
            public long getGroupId(int i) {
                return i;
            }

            @Override
            public long getChildId(int i, int i1) {
                return i1;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
                View viewtitle = LayoutInflater.from(InformationActivity.this).inflate(R.layout.list_title, viewGroup, false);
                TextView title = viewtitle.findViewById(R.id.title);
                title.setText(getGroup(i).toString());
                return viewtitle;
            }

            @Override
            public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
                View viewint = LayoutInflater.from(InformationActivity.this).inflate(R.layout.list_inf,viewGroup,false);
                TextView name = viewint.findViewById(R.id.name);
                name.setText(getChild(i,i1).toString());
                TextView edit = viewint.findViewById(R.id.edit);
                return viewint;
            }

            @Override
            public boolean isChildSelectable(int i, int i1) {
                return true;
            }
        };
        ExpandableListView expanlistview = (ExpandableListView)findViewById(R.id.list);
        expanlistview.setAdapter(adapter);
    }
}
