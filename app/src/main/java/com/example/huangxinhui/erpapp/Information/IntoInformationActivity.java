package com.example.huangxinhui.erpapp.Information;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.huangxinhui.erpapp.R;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

public class IntoInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into_information);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());

    }
}
