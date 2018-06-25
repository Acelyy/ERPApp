package com.example.huangxinhui.erpapp.Information;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.huangxinhui.erpapp.Adapter.ReceiverAdapter;
import com.example.huangxinhui.erpapp.JavaBean.Query;
import com.example.huangxinhui.erpapp.R;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveInformationActivity extends AppCompatActivity {

    @BindView(R.id.titleName)
    TextView titleName;
    @BindView(R.id.list_receiver)
    RecyclerView listReceiver;
    @BindView(R.id.parent)
    LinearLayout parent;

    ReceiverAdapter adapter;

    ArrayList<Query.DataBean> data;

    PopupWindow pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_information);
        ButterKnife.bind(this);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        listReceiver.setLayoutManager(new LinearLayoutManager(this));
        titleName.setText(getIntent().getExtras().getString("title") == null ? "" : getIntent().getExtras().getString("title"));
        data = (ArrayList<Query.DataBean>) getIntent().getExtras().getSerializable("data");
        if (data != null && !data.isEmpty() && data.get(0).getName().equals("data")) {
            adapter = new ReceiverAdapter(data.get(0).getList_info(), this);
            adapter.setOnButtonClickListener(new ReceiverAdapter.OnButtonClickListener() {
                @Override
                public void onCLick(View view, int position) {
                    // 点击入库
                    pop.showAtLocation(parent, Gravity.CENTER, 0, 0);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.5f; //0.0-1.0
                    getWindow().setAttributes(lp);
                }
            });
            listReceiver.setAdapter(adapter);
        }

        initPopupWindow();
    }

    private void initPopupWindow() {
        final View popView = LayoutInflater.from(this).inflate(R.layout.pop_receiver, null);
        popView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 提交接收入库

            }
        });
        pop = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.update();
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f; //0.0-1.0
                getWindow().setAttributes(lp);
            }
        });
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
