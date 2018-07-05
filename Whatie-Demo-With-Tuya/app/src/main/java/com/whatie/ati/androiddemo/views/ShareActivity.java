package com.whatie.ati.androiddemo.views;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whatie.ati.androiddemo.R;
import com.d9lab.ati.whatiesdk.bean.BaseResponse;
import com.d9lab.ati.whatiesdk.callback.BaseCallback;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.util.Code;
import com.d9lab.ati.whatiesdk.util.SharedPreferenceUtils;
import com.lzy.okgo.model.Response;

import butterknife.BindView;

/**
 * Created by liz on 2018/4/26.
 */

public class ShareActivity extends BaseActivity {
    @BindView(R.id.et_shared_email)
    EditText etSharedEmail;
    @BindView(R.id.tv_share_button)
    TextView confirm;
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
    private int deviceId;

    @Override
    protected int getContentViewId() {
        return R.layout.act_share_with_email;
    }

    @Override
    protected void initViews() {
        tvTitle.setText(getString(R.string.pop_share_device));
        this.deviceId = getIntent().getIntExtra(Code.DEVICE_ID, -1);

    }

    @Override
    protected void initEvents() {
        ivTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etSharedEmail.getText().toString().trim().equals("")) {
                    EHomeInterface.getINSTANCE().addShare(mContext, (int) SharedPreferenceUtils.get(mContext, "userId", -1), etSharedEmail.getText().toString().trim(), deviceId, new BaseCallback() {
                        @Override
                        public void onSuccess(Response<BaseResponse> response) {
                            if(response.body().isSuccess()){
                                if (((int) SharedPreferenceUtils.get(mContext, "userId", -1) )!= -1) {
                                    Toast.makeText(mContext, getString(R.string.toast_share_success), Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    Toast.makeText(mContext, getString(R.string.toast_share_fail), Toast.LENGTH_SHORT).show();

                                }
                            }
                            else {
                                Toast.makeText(mContext,response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }


                        }

                        @Override
                        public void onError(Response<BaseResponse> response) {
                            super.onError(response);
                            Toast.makeText(mContext, getString(R.string.toast_share_fail), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initDatas() {

    }

}
