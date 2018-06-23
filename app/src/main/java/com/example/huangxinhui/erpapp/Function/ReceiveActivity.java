package com.example.huangxinhui.erpapp.Function;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.huangxinhui.erpapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiveActivity extends AppCompatActivity {
    @BindView(R.id.producedDate)
    TextView producedDate;

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.producedDate, R.id.query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.producedDate:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View itemView = LayoutInflater.from(this).inflate(R.layout.item_date, null);
                final DatePicker picker = itemView.findViewById(R.id.dataPicker);
                builder.setTitle("请选择查询日期")
                        .setView(itemView)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                date = new SimpleDateFormat("yyyyMMdd").format(new Date(picker.getYear()-1900, picker.getMonth(), picker.getDayOfMonth()));
                                producedDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(picker.getYear() - 1900, picker.getMonth(), picker.getDayOfMonth())));
                            }
                        }
                ).create().show();
                break;
            case R.id.query:
                break;
        }
    }


}
