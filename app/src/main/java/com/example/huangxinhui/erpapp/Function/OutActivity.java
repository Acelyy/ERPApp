package com.example.huangxinhui.erpapp.Function;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.huangxinhui.erpapp.R;
import com.example.huangxinhui.erpapp.Util.IpConfig;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OutActivity extends AppCompatActivity {

    @BindView(R.id.outCode)
    EditText outCode;
    @BindView(R.id.brevityCode)
    EditText brevityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);
        ButterKnife.bind(this);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());

    }

    @OnClick({R.id.back, R.id.query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.query:
                new Thread(new OutThread(outCode.getText().toString().trim(),brevityCode.getText().toString().trim())).start();
                break;
        }
    }

    class OutThread implements Runnable{
        private String heatNo;
        private String heatNoj;

        public OutThread(String outCode, String brevityCode) {
            this.heatNo = outCode;
            this.heatNoj = brevityCode;
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

            String data = String.format("%-10s", "GPIS05")
                    + String.format("%-10s", heatNo) + String.format("%-10s", heatNoj)
                    + "*";
            // 设置需调用WebService接口需要传入的参数
            Log.i("params", data);
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
                Log.i("out", result);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
