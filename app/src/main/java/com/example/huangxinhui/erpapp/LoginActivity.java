package com.example.huangxinhui.erpapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.huangxinhui.erpapp.Util.IpConfig;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.user)
    EditText user;
    @BindView(R.id.pwd)
    EditText pwd;
    @BindView(R.id.remember)
    CheckBox remember;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
        if (sp.getBoolean("remember", false)) {
            user.setText(sp.getString("username", null));
            pwd.setText(sp.getString("password", null));
            remember.setChecked(true);
        }
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
        if (remember.isChecked()) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username", user.getText().toString());
            editor.putString("password", pwd.getText().toString());
            editor.putBoolean("remember", true);
            editor.commit();
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("username", "");
            editor.putString("password", "");
            editor.putBoolean("remember", false);
            editor.commit();
        }
        new Thread(new LoginThread(user.getText().toString().trim(), pwd.getText().toString())).start();
    }

    class LoginThread implements Runnable {
        private String userName;
        private String password;

        public LoginThread(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        @Override
        public void run() {
            String result = "";
            String nameSpace = "http://service.sh.icsc.com";
            // 调用的方法名称
            String methodName = "run";
            // EndPoint
            //String endPoint = "http://10.10.4.210:9081/erp/sh/Scanning.ws";//测试
            String endPoint = "http://" + IpConfig.IP + "/erp/sh/Scanning.ws";//正式
            // SOAP Action
            String soapAction = "http://service.sh.icsc.com/run";

            // 指定WebService的命名空间和调用的方法名
            SoapObject rpc = new SoapObject(nameSpace, methodName);


            String data = String.format("%-10s", "GPIS00")
                    + String.format("%-10s", userName) + String.format("%-10s", password)
                    + String.format("%-10s", "") + "*";
            // 设置需调用WebService接口需要传入的参数
            rpc.addProperty("date", data);

            // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER10);

            envelope.bodyOut = rpc;
            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = false;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(endPoint);
            try {
                // 调用WebService
                transport.call(soapAction, envelope);
                Object object = (Object) envelope.getResponse();
                // 获取返回的结果
                result = object.toString();
                Log.i("login", result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
