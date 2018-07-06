package com.example.huangxinhui.erpapp.Information;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.huangxinhui.erpapp.Adapter.ReceiverAdapter;
import com.example.huangxinhui.erpapp.JavaBean.LoginResult;
import com.example.huangxinhui.erpapp.JavaBean.Machine;
import com.example.huangxinhui.erpapp.JavaBean.Query;
import com.example.huangxinhui.erpapp.JavaBean.Wear;
import com.example.huangxinhui.erpapp.R;
import com.example.huangxinhui.erpapp.Util.IpConfig;
import com.example.huangxinhui.erpapp.Util.JsonReader;
import com.example.huangxinhui.erpapp.Util.JsonUtil;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<String, String> wear;

    private Map<String, String> machine;

    PopupWindow pop;

    private Map<String, String> data_map;

    String WarrantyBook = "";

    String FurnaceCode = "";

    EditText code,information;

    ProgressDialog dialog;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    dialog.show();
                    break;
                case 0:
                    // 处理异常，网络请求失败
                    Toast.makeText(ReceiveInformationActivity.this, "网络异常，请检查网络是否通畅", Toast.LENGTH_SHORT).show();
                    if (dialog.isShowing())
                        dialog.dismiss();
                    break;
                case 1:
                    // 处理服务器返回的数据
                    String data = msg.getData().getString("data");
                    if (JsonUtil.isJson(data)) {// 判断是否为json
                        LoginResult result = JSON.parseObject(data, LoginResult.class);
                        if (result != null && !result.getResult().equals("F")) {
                            Toast.makeText(ReceiveInformationActivity.this, "接收入库成功", Toast.LENGTH_SHORT).show();
                            ReceiveInformationActivity.this.finish();
                        } else {
                            Toast.makeText(ReceiveInformationActivity.this, "接收入库失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ReceiveInformationActivity.this, "接收入库失败", Toast.LENGTH_SHORT).show();
                    }
                    if (dialog.isShowing())
                        dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_information);
        ButterKnife.bind(this);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        listReceiver.setLayoutManager(new LinearLayoutManager(this));
        titleName.setText(getIntent().getExtras().getString("title") == null ? "" : getIntent().getExtras().getString("title"));
        data = (ArrayList<Query.DataBean>) getIntent().getExtras().getSerializable("data");
        wear = getWear(JSON.parseArray(JsonReader.getJson("wear.json", this), Wear.class));
        machine = getMachine(JSON.parseArray(JsonReader.getJson("machine.json", this), Machine.class));
        for (int i=0;i<data.size();i++){
            for(int j=0;j<data.get(i).getList_info().size();j++){
                if(data.get(i).getList_info().get(j).getKey().equals("移出库别")){
                    data.get(i).getList_info().get(j).setValue(wear.get(data.get(i).getList_info().get(j).getValue()));
                }
                if(data.get(i).getList_info().get(j).getKey().equals("连铸机号")){
                    data.get(i).getList_info().get(j).setValue(machine.get(data.get(i).getList_info().get(j).getValue()));
                }
            }
        }
        if (data != null && !data.isEmpty() && data.get(0).getName().equals("data")) {
            data_map = exchange(data.get(0).getList_info());
            FurnaceCode = data_map.get("出库炉号");
            WarrantyBook = data_map.get("质保书编号");
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
        dialog = new ProgressDialog(this);
        dialog.setMessage("查询中");
    }
    /**
     * 将Wear集合转化为Map，便于取值
     *
     * @param wears
     * @return
     */
    private Map<String, String> getWear(List<Wear> wears) {
        Map<String, String> result = new HashMap<>();
        for (Wear bean : wears) {
            result.put(bean.getValue(),bean.getKey());
        }
        return result;
    }

    /**
     * 将Machine集合转化为Map，便于取值
     *
     * @param machine
     * @return
     */
    private Map<String, String> getMachine(List<Machine> machine) {
        Map<String, String> result = new HashMap<>();
        for (Machine bean : machine) {
            result.put(bean.getValue(),bean.getKey());
        }
        return result;
    }

    /**
     * 将List<Info>列表转化为Map，便于取值
     *
     * @return
     */
    private Map<String, String> exchange(List<Query.DataBean.Info> data) {
        Map<String, String> result = new HashMap<>();
        for (Query.DataBean.Info info : data) {
            result.put(info.getKey(), info.getValue());
        }
        return result;
    }

    private void initPopupWindow() {
        final View popView = LayoutInflater.from(this).inflate(R.layout.pop_receiver, null);
        code = popView.findViewById(R.id.code);
        information = popView.findViewById(R.id.information);
        popView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 提交接收入库
                new Thread(new receiveLibThread(
                        WarrantyBook,
                        FurnaceCode,
                        code.getText().toString().trim(),
                        information.getText().toString().trim()
                )).start();;
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

    class receiveLibThread implements Runnable{
        private String chgLocRptNo;
        private String heatNo;
        private String areaNo;
        private String rowNo;

        public receiveLibThread(String chgLocRptNo,String heatNo,String areaNo, String rowNo) {
            this.chgLocRptNo = chgLocRptNo;
            this.heatNo = heatNo;
            this.areaNo = areaNo;
            this.rowNo = rowNo;
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

            String data = String.format("%-10s", "GPIS07")
                    + String.format("%-12s", chgLocRptNo) + String.format("%-10s", heatNo)
                    + String.format("%-10s", areaNo) + String.format("%-10s", rowNo)
                    + "*";
            // 设置需调用WebService接口需要传入的参数
            Log.i("params", data);
            rpc.addProperty("data", data);

            // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER10);

            envelope.bodyOut = rpc;
            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = false;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE transport = new HttpTransportSE(endPoint);
            mHandler.sendEmptyMessage(-1);
            try {
                // 调用WebService
                transport.call(soapAction, envelope);
                Object object = (Object) envelope.getResponse();
                // 获取返回的结果
                result = object.toString();
                Log.i("receivelib", result);

                // 如果有数据返回，通知handler 1
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("data", result);
                msg.setData(bundle);
                msg.sendToTarget();
            } catch (Exception e) {
                // 如果捕获异常，通知handler 0
                mHandler.sendEmptyMessage(0);
                e.printStackTrace();
            }
        }
    }
}
