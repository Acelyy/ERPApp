package com.example.huangxinhui.erpapp.Information;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.huangxinhui.erpapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveInformationActivity extends AppCompatActivity {

    @BindView(R.id.titleName)
    TextView titleName;
    @BindView(R.id.tl_tab)
    TabLayout tlTab;
    @BindView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_information);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
