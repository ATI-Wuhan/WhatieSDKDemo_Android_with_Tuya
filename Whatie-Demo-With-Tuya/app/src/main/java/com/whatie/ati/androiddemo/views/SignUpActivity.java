package com.whatie.ati.androiddemo.views;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whatie.ati.androiddemo.R;
import com.d9lab.ati.whatiesdk.bean.BaseModelResponse;
import com.d9lab.ati.whatiesdk.bean.User;
import com.d9lab.ati.whatiesdk.callback.UserCallback;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 神火 on 2018/6/7.
 */

public class SignUpActivity extends BaseActivity {
    @BindView(R.id.et_signUp_email)
    EditText etEmail;
    @BindView(R.id.et_signUp)
    EditText etPwd;
    @BindView(R.id.tv_signUp_signUp)
    TextView buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected int getContentViewId() {
        return R.layout.act_sign_up;
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

    @OnClick({R.id.tv_signUp_signUp,R.id.iv_signUp_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_signUp_back:
                finish();
                break;
            case R.id.tv_signUp_signUp:
                if(etEmail.getText().toString().trim().equals("") ||etPwd.getText().toString().trim().equals("")) {
                    Toast.makeText(mContext, getString(R.string.login_cannot_empty), Toast.LENGTH_SHORT).show();
                } else {
                    buttonLogin.setClickable(false);

                    EHomeInterface.getINSTANCE().registerAccountWithEmail(mContext, etEmail.getText().toString().trim(), etPwd.getText().toString().trim(),
                            new UserCallback() {
                                @Override
                                public void onSuccess(Response<BaseModelResponse<User>> response) {
                                    if (response.body().isSuccess()) {
                                        Toast.makeText(mContext, getString(R.string.sign_up_success), Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        buttonLogin.setClickable(true);
                                        Toast.makeText(mContext, getString(R.string.sign_up_fail), Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onError(Response<BaseModelResponse<User>> response) {
                                    super.onError(response);
                                    buttonLogin.setClickable(true);
                                    Toast.makeText(mContext, getString(R.string.sign_up_fail), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                break;

        }
    }

}
