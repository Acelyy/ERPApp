package com.example.huangxinhui.erpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.example.huangxinhui.erpapp.Function.IntoActivity;
import com.example.huangxinhui.erpapp.Function.MoveActivity;
import com.example.huangxinhui.erpapp.Function.OutActivity;
import com.example.huangxinhui.erpapp.Function.QueryActivity;
import com.example.huangxinhui.erpapp.Function.ReceiveActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.query, R.id.receive, R.id.out, R.id.into, R.id.move})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.query:
                intent = new Intent(this, QueryActivity.class);
                break;
            case R.id.receive:
                intent = new Intent(this, ReceiveActivity.class);
                break;
            case R.id.out:
                intent = new Intent(this, OutActivity.class);
                break;
            case R.id.into:
                intent = new Intent(this, IntoActivity.class);
                break;
            case R.id.move:
                intent = new Intent(this, MoveActivity.class);
                break;
        }
        if (intent != null)
            startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
