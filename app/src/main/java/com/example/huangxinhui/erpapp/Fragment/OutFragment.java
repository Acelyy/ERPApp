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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.huangxinhui.erpapp.Adapter.OutAdapter;
import com.example.huangxinhui.erpapp.JavaBean.BusinessType;
import com.example.huangxinhui.erpapp.JavaBean.LoginResult;
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
import butterknife.Unbinder;

public class OutFragment extends Fragment {

    Unbinder unbinder;
    List<Query.DataBean.Info> data;

    Map<String, String> data_map;

    ProgressDialog dialog;

    @BindView(R.id.list_out)
    RecyclerView listOut;

    private Map<String, String> status;
    private Map<String, String> wear;

    private PopupWindow pop;

    private List<BusinessType> types = new ArrayList<>();
    private int select;

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
                            Toast.makeText(getActivity(), "出库成功", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "出库失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "出库失败", Toast.LENGTH_SHORT).show();
                    }
                    if (dialog.isShowing())
                        dialog.dismiss();
                    break;
            }
        }
    };


    public static OutFragment getInstance(ArrayList<Query.DataBean.Info> data) {
        OutFragment qf = new OutFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        qf.setArguments(bundle);
        return qf;
    }

    /**
     * 初始化pop
     */
    private void initPop() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_out, null);
        final TextView lb = view.findViewById(R.id.lb);
        TextView outWarehouseNo = view.findViewById(R.id.outWarehouseNo);
//        EditText chgLocDate = view.findViewById(R.id.chgLocDate);
//        EditText carNo = view.findViewById(R.id.carNo);
//        EditText operateShift = view.findViewById(R.id.operateShift);
//        EditText operateCrew = view.findViewById(R.id.operateCrew);
//        EditText driver = view.findViewById(R.id.driver);
//        EditText inWarehouseNo = view.findViewById(R.id.inWarehouseNo);
//        EditText qty = view.findViewById(R.id.qty);
//        EditText bz = view.findViewById(R.id.bz);
        lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        select = options1;
                        lb.setText(types.get(select).getName());
                        showPop();
                    }
                }).setOutSideCancelable(false)
                        .setContentTextSize(25)
                        .build();
                pvOptions.setPicker(types);
                pvOptions.setSelectOptions(select);
                pvOptions.setTitleText("请选择班组");
                pvOptions.show();
            }
        });
        outWarehouseNo.setText(data_map.get("当前库"));

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new OutConfirmThread(
                        "B",
                        "X30",
                        "20180626",
                        "2301",
                        "1",
                        "B",
                        "001",
                        "X40",
                        "杜成栋",

                        data_map.get("炉号"),
                        data_map.get("长度"),
                        data_map.get("重量"),
                        data_map.get("钢种"),
                        status.get(data_map.get("钢坯状态")),
                        data_map.get("块数"),// 后面改
                        wear.get(data_map.get("当前库")),
                        data_map.get("当前跨"),
                        data_map.get("当前储序")
                )).start();
            }
        });
        pop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = JSON.parseObject(JsonReader.getJson("status.json", getActivity()), Map.class);
        wear = getWear(JSON.parseArray(JsonReader.getJson("wear.json", getActivity()), Wear.class));
        data = (List<Query.DataBean.Info>) getArguments().getSerializable("data");
        data_map = exchange(data);
        types.add(new BusinessType("热输", "A"));
        types.add(new BusinessType("冷送", "B"));
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

    /**
     * 将Wear集合转化为Map，便于取值
     *
     * @param wears
     * @return
     */
    private Map<String, String> getWear(List<Wear> wears) {
        Map<String, String> result = new HashMap<>();
        for (Wear bean : wears) {
            result.put(bean.getKey(), bean.getValue());
        }
        return result;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_out, container, false);
        ScreenAdapterTools.getInstance().loadView(view);
        unbinder = ButterKnife.bind(this, view);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("出库中");
        initPop();
        listOut.setLayoutManager(new LinearLayoutManager(getActivity()));
        OutAdapter adapter = new OutAdapter(data, getActivity());
        adapter.setOnButtonClickListener(new OutAdapter.OnButtonClickListener() {
            @Override
            public void onCLick(View view, int position) {
                showPop();
            }
        });
        listOut.setAdapter(adapter);
        return view;
    }

    private void showPop() {
        pop.showAtLocation(listOut, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class OutConfirmThread implements Runnable {
        private String lb;
        private String outWarehouseNo;
        private String chgLocDate;
        private String carNo;
        private String operateShift;
        private String operateCrew;
        private String driver;
        private String inWarehouseNo;
        private String bz;

        private String heatNo;
        private String length;
        private String weight;
        private String spec;
        private String status;
        private String qty;
        private String warehouseNo;
        private String areaNo;
        private String rowNo;

        public OutConfirmThread(String lb,
                                String outWarehouseNo,
                                String chgLocDate,
                                String carNo,
                                String operateShift,
                                String operateCrew,
                                String driver,
                                String inWarehouseNo,
                                String bz,

                                String heatNo,
                                String length,
                                String weight,
                                String spec,
                                String status,
                                String qty,
                                String warehouseNo,
                                String areaNo,
                                String rowNo) {
            this.lb = lb;
            this.outWarehouseNo = outWarehouseNo;
            this.chgLocDate = chgLocDate;
            this.carNo = carNo;
            this.operateShift = operateShift;
            this.operateCrew = operateCrew;
            this.driver = driver;
            this.inWarehouseNo = inWarehouseNo;
            this.bz = bz;
            this.heatNo = heatNo;
            this.length = length;
            this.weight = weight;
            this.spec = spec;
            this.status = status;
            this.qty = qty;
            this.warehouseNo = warehouseNo;
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

            String data = String.format("%-10s", "GPIS08")
                    + String.format("%-10s", lb)
                    + String.format("%-10s", outWarehouseNo)
                    + String.format("%-8s", chgLocDate)
                    + String.format("%-15s", carNo)
                    + String.format("%-5s", operateShift)
                    + String.format("%-5s", operateCrew)
                    + String.format("%-10s", driver)
                    + String.format("%-10s", inWarehouseNo)
                    + String.format("%-50s", bz)

                    + String.format("%-10s", heatNo)
                    + String.format("%-10s", length)
                    + String.format("%-10s", weight)
                    + String.format("%-20s", spec)
                    + String.format("%-5s", "12")
                    + String.format("%-3s", qty)
                    + String.format("%-10s", warehouseNo)
                    + String.format("%-10s", areaNo)
                    + String.format("%-10s", rowNo)
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
                Log.i("login", result);

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
