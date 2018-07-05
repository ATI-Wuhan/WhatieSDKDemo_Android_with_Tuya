package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whatie.ati.androiddemo.R;
import com.d9lab.ati.whatiesdk.bean.BaseResponse;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.callback.BaseCallback;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.util.Code;
import com.lzy.okgo.model.Response;

import butterknife.BindView;

/**
 * Created by 神火 on 2018/6/7.
 */

public class ChangeDeviceNameActivity extends BaseActivity {
    @BindView(R.id.et_change_device_name)
    EditText etDeviceName;
    @BindView(R.id.tv_change_device_name_button)
    TextView tvChangeConfirm;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    private DeviceVo deviceVo;

    private String id;
    private String name;
    @Override
    protected int getContentViewId() {
        return R.layout.act_change_device_name;
    }

    @Override
    protected void initViews() {
        id=getIntent().getStringExtra(Code.DEV_ID);
        name=getIntent().getStringExtra(Code.DEVICE_NAME);
        etDeviceName.setText(name);
        tvTitle.setText(getString(R.string.pop_change_device));
    }

    @Override
    protected void initEvents() {
        ivTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvChangeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etDeviceName.getText().toString().trim().equals("")) {
                    EHomeInterface.getINSTANCE().updateDeviceName(mContext, id, etDeviceName.getText().toString().trim(),
                            new BaseCallback() {
                                @Override
                                public void onSuccess(Response<BaseResponse> response) {
                                    if (response.body().isSuccess()) {
                                        Toast.makeText(mContext, getString(R.string.change_device_name_success), Toast.LENGTH_SHORT).show();
                                        Intent result=new Intent();
                                        result.putExtra(Code.RETURN_DEVICE_NAME, etDeviceName.getText().toString().trim());
                                        setResult(RESULT_OK, result);
                                        finish();
                                    } else {
                                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(Response<BaseResponse> response) {
                                    super.onError(response);
                                    Toast.makeText(mContext, getString(R.string.change_device_name_fail), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(mContext, getString(R.string.change_device_name_cannot_empty), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void initDatas() {

    }

}
