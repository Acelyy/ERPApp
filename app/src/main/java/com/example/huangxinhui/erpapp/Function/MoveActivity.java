package com.example.huangxinhui.erpapp.Function;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.huangxinhui.erpapp.R;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

public class MoveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());

    }
}
