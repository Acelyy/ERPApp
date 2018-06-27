package com.example.huangxinhui.erpapp.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.huangxinhui.erpapp.Adapter.IntoAdapter;
import com.example.huangxinhui.erpapp.Adapter.MoveAdapter;
import com.example.huangxinhui.erpapp.JavaBean.LoginResult;
import com.example.huangxinhui.erpapp.JavaBean.Query;
import com.example.huangxinhui.erpapp.R;
import com.example.huangxinhui.erpapp.Util.IpConfig;
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
import butterknife.Unbinder;

public class MoveFragment extends Fragment {
    Unbinder unbinder;
    List<Query.DataBean.Info> data;
    @BindView(R.id.list_query)
    RecyclerView listMove;
    MoveAdapter adapter;
    PopupWindow pop;
    ProgressDialog dialog;

    EditText qty, modiAreaNo, modiRowNo;

    Map<String, String> data_map;
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
                    Toast.makeText(getActivity(), "网络异常，请检查网络是否通畅", Toast.LENGTH_SHORT).show();
                    if (dialog.isShowing())
                        dialog.dismiss();
                    break;
                case 1:
                    // 处理服务器返回的数据
                    String data = msg.getData().getString("data");
                    if (JsonUtil.isJson(data)) {// 判断是否为json
                        LoginResult result = JSON.parseObject(data, LoginResult.class);
                        if (result != null && !result.getResult().equals("F")) {
                            Toast.makeText(getActivity(), "入库成功", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "入库失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "入库失败", Toast.LENGTH_SHORT).show();
                    }
                    if (dialog.isShowing())
                        dialog.dismiss();
                    break;
            }
        }
    };

    public static MoveFragment getInstance(ArrayList<Query.DataBean.Info> data) {
        MoveFragment mf = new MoveFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        mf.setArguments(bundle);
        return mf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.data = (List<Query.DataBean.Info>) getArguments().getSerializable("data");
        data_map = exchange(data);
    }

    /**
     * 将List转化为Map
     */
    private Map<String, String> exchange(List<Query.DataBean.Info> data) {
        Map<String, String> result = new HashMap<>();
        for (Query.DataBean.Info bean : data) {
            result.put(bean.getKey(), bean.getValue());
        }
        return result;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_query, container, false);
        unbinder = ButterKnife.bind(this, view);
        listMove.setLayoutManager(new LinearLayoutManager(getActivity()));
        ScreenAdapterTools.getInstance().loadView(view);
        initPopupWindow();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("查询中");
        adapter = new MoveAdapter(data, getActivity());
        adapter.setOnButtonClickListener(new MoveAdapter.OnButtonClickListener() {
            @Override
            public void onCLick(View view, int position) {
                pop.showAtLocation(listMove, Gravity.CENTER, 0, 0);
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 0.5f; //0.0-1.0
                getActivity().getWindow().setAttributes(lp);
            }
        });
        listMove.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initPopupWindow() {
        final View popView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_move, null);
        qty = popView.findViewById(R.id.qty);
        modiAreaNo = popView.findViewById(R.id.modiAreaNo);
        modiRowNo = popView.findViewById(R.id.modiRowNo);
        popView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 提交钢柸入库
                new Thread(new moceLibThread(
                        data_map.get("炉号"),
                        data_map.get("长度"),
                        data_map.get("钢种"),
                        data_map.get("状态"),
                        data_map.get("库别"),
                        data_map.get("当前跨"),
                        data_map.get("当前储序"),
                        qty.getText().toString().trim(),
                        modiAreaNo.getText().toString().trim(),
                        modiRowNo.getText().toString().trim()
                )).start();
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
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f; //0.0-1.0
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    class moceLibThread implements Runnable {
        private String heatNo;
        private String lenght;
        private String spec;
        private String status;
        private String warehouseNo;
        private String areaNo;
        private String rowNo;
        private String qty;
        private String modiAreaNo;
        private String modiRowNo;

        public moceLibThread(String heatNo, String lenght, String spec, String status, String warehouseNo, String areaNo, String rowNo, String qty, String modiAreaNo, String modiRowNo) {
            this.heatNo = heatNo;
            this.lenght = lenght;
            this.spec = spec;
            this.status = status;
            this.warehouseNo = warehouseNo;
            this.areaNo = areaNo;
            this.rowNo = rowNo;
            this.qty = qty;
            this.modiAreaNo = modiAreaNo;
            this.modiRowNo = modiRowNo;
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

            String data = String.format("%-10s", "GPIS10")
                    + String.format("%-12s", heatNo)
                    + String.format("%-12s", lenght)
                    + String.format("%-20s", spec)
                    + String.format("%-5s", status)
                    + String.format("%-12s", warehouseNo)
                    + String.format("%-12s", areaNo)
                    + String.format("%-12s", rowNo)
                    + String.format("%-3s", qty)
                    + String.format("%-12s", modiAreaNo)
                    + String.format("%-12s", modiRowNo)
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
            mHandler.sendEmptyMessage(-1);
            try {
                // 调用WebService
                transport.call(soapAction, envelope);
                Object object = (Object) envelope.getResponse();
                // 获取返回的结果
                result = object.toString();
                Log.i("intolib", result);

                // 如果有数据返回，通知handler 1
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("data", result);
                msg.setData(bundle);
                msg.sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
