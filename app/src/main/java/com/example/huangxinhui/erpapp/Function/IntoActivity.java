package com.example.huangxinhui.erpapp.Function;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.huangxinhui.erpapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntoActivity extends AppCompatActivity {

    @BindView(R.id.intoCode)
    EditText intoCode;
    @BindView(R.id.brevityCode)
    EditText brevityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_into);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.query:
                break;
        }
    }

    class IntoThread implements Runnable {

        @Override
        public void run() {

        }
    }
}
