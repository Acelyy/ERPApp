package com.example.huangxinhui.erpapp.Information;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.huangxinhui.erpapp.Adapter.ReceiverAdapter;
import com.example.huangxinhui.erpapp.JavaBean.Query;
import com.example.huangxinhui.erpapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveInformationActivity extends AppCompatActivity {

    @BindView(R.id.titleName)
    TextView titleName;
    @BindView(R.id.list_receiver)
    RecyclerView listReceiver;

    ReceiverAdapter adapter;

    ArrayList<Query.DataBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_information);
        ButterKnife.bind(this);
        listReceiver.setLayoutManager(new LinearLayoutManager(this));
        titleName.setText(getIntent().getExtras().getString("title") == null ? "" : getIntent().getExtras().getString("title"));
        data = (ArrayList<Query.DataBean>) getIntent().getExtras().getSerializable("data");
        if (data != null && !data.isEmpty() && data.get(0).getName().equals("data")) {
            adapter = new ReceiverAdapter(data.get(0).getList_info(), this);
            adapter.setOnButtonClickListener(new ReceiverAdapter.OnButtonClickListener() {
                @Override
                public void onCLick(View view, int position) {

                    // 点击入库
                }
            });
            listReceiver.setAdapter(adapter);
        }
    }

    @OnClick({R.id.back, R.id.icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.icon:
                // 查看质保书
                Intent intent = new Intent(ReceiveInformationActivity.this, WarrantyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
