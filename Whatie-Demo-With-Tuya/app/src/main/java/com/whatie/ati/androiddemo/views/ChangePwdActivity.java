package com.whatie.ati.androiddemo.views;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whatie.ati.androiddemo.R;
import com.d9lab.ati.whatiesdk.bean.BaseResponse;
import com.d9lab.ati.whatiesdk.callback.BaseCallback;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.util.Code;
import com.d9lab.ati.whatiesdk.util.MD5Utils;
import com.d9lab.ati.whatiesdk.util.SharedPreferenceUtils;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liz on 2018/4/27.
 */

public class ChangePwdActivity extends BaseActivity {
    @BindView(R.id.iv_change_pwd_back)
    ImageView ivChangePwdBack;
    @BindView(R.id.et_change_pwd_old)
    EditText etChangePwdOld;
    @BindView(R.id.et_change_pwd_new)
    EditText etChangePwdNew;
    @BindView(R.id.et_change_pwd_confirm_new)
    EditText etChangePwdConfirmNew;
    @BindView(R.id.tv_change_pwd_submit)
    TextView tvChangePwdSubmit;
    private String mOldPwd;
    private String mNewPwd;
    private String mConfirmPwd;

    @Override
    protected int getContentViewId() {
        return R.layout.act_change_pwd;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {

    }
    @OnClick({R.id.iv_change_pwd_back, R.id.tv_change_pwd_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_change_pwd_back:
                finish();
                break;
            case R.id.tv_change_pwd_submit:
                mOldPwd = etChangePwdOld.getText().toString().trim();
                mNewPwd = etChangePwdNew.getText().toString();
                mConfirmPwd = etChangePwdConfirmNew.getText().toString();
                if(mOldPwd.equals("") | mNewPwd.equals("") | mConfirmPwd.equals("")){
                    Toast.makeText(mContext, getString(R.string.change_password_tip),Toast.LENGTH_SHORT).show();
                }else if(! mNewPwd.equals(mConfirmPwd)){
                    Toast.makeText(mContext, getString(R.string.change_password_match),Toast.LENGTH_SHORT).show();
                }else {
                    mLoadingDialog.show();
                    changePwd(mOldPwd, mNewPwd);
                }
                break;
        }
    }
    private void changePwd(String oldPwd, final String newPwd){
        EHomeInterface.getINSTANCE().changePassword(mContext,
                (String) SharedPreferenceUtils.get(mContext, Code.SP_USER_EMAIL, ""),
                oldPwd,
                newPwd,
                new BaseCallback() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        mLoadingDialog.dismiss();
                        Toast.makeText(mContext, response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        if(response.body().isSuccess()){
                            SharedPreferenceUtils.put(mContext, Code.SP_MD5_PASSWORD, MD5Utils.encode(newPwd));
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        super.onError(response);
                        mLoadingDialog.dismiss();
                        Toast.makeText(mContext, Code.NETWORK_WRONG,Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
