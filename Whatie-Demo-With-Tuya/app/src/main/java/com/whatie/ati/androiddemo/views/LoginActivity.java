package com.whatie.ati.androiddemo.views;


import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whatie.ati.androiddemo.R;
import com.d9lab.ati.whatiesdk.bean.BaseModelResponse;
import com.d9lab.ati.whatiesdk.bean.User;
import com.d9lab.ati.whatiesdk.callback.UserCallback;
import com.d9lab.ati.whatiesdk.ehome.EHome;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.util.Code;
import com.d9lab.ati.whatiesdk.util.MD5Utils;
import com.d9lab.ati.whatiesdk.util.SharedPreferenceUtils;
import com.d9lab.ati.whatiesdk.util.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liz on 2018/4/24.
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.et_login_email)
    EditText etEmail;
    @BindView(R.id.et_login_password)
    EditText etPwd;
    @BindView(R.id.tv_login_signIn)
    TextView buttonLogin;


    @Override
    protected int getContentViewId() {
        return R.layout.act_login;
    }

    @Override
    protected void initViews() {
        if ((int) SharedPreferenceUtils.get(mContext, Code.SP_USER_ID, -1) != -1) {
            loginAuto();
        }
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {

    }

    @OnClick({R.id.tv_login_signIn, R.id.tv_change_pwd, R.id.tv_forget_pwd, R.id.tv_signIn_signUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_signIn:
                if (etEmail.getText().toString().trim().equals("") || etPwd.getText().toString().trim().equals("")) {
                    Toast.makeText(mContext, getString(R.string.login_cannot_empty), Toast.LENGTH_SHORT).show();
                } else {
                    buttonLogin.setClickable(false);
                    loginWithEmail(etEmail.getText().toString().trim(), etPwd.getText().toString().trim());
                }
                break;
            case R.id.tv_change_pwd:
                startActivity(new Intent(LoginActivity.this, ChangePwdActivity.class));
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.tv_signIn_signUp:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
        }
    }

    private void loginAuto() {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", (String) SharedPreferenceUtils.get(mContext, Code.SP_USER_EMAIL, ""));
        params.put("password", (String) SharedPreferenceUtils.get(mContext, Code.SP_MD5_PASSWORD, ""));
        params.put("accessId", EHome.getAPPID());
        params.put("accessKey", EHome.getSECRETKEY());

        OkGo.<BaseModelResponse<User>>post(Urls.LOGIN)
                .tag(mContext)
                .params(params)
                .execute(new UserCallback() {
                    @Override
                    public void onSuccess(Response<BaseModelResponse<User>> response) {
                        if (response.body().isSuccess()) {
                            EHome.getInstance().setLogin(true);

                            EHome.getInstance().setmUser(response.body().getValue());
                            EHome.getInstance().setToken(response.body().getToken());
//                            SharedPreferenceUtils.put(mContext, "userId", Integer.valueOf(response.body().getValue().getId()));
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setAction("fromLogin");
                            startActivity(intent);
                            Toast.makeText(mContext, getString(R.string.login_auto_success), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            buttonLogin.setClickable(true);
                            if (response.body() != null) {
                                if (response.body().getMessage() != null || !response.body().getMessage().isEmpty()) {
                                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                    }

                    @Override
                    public void onError(Response<BaseModelResponse<User>> response) {
                        super.onError(response);
                        buttonLogin.setClickable(true);
                        if (response.body() != null) {
                            if (response.body().getMessage() != null || !response.body().getMessage().isEmpty()) {
                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                });
    }

    private void loginWithEmail(String email, final String password) {
        EHomeInterface.getINSTANCE().loginWithEmail(mContext, email, password,
                new UserCallback() {
                    @Override
                    public void onSuccess(Response<BaseModelResponse<User>> response) {
                        if (response.body().isSuccess()) {
                            EHome.getInstance().setLogin(true);
                            EHome.getInstance().setmUser(response.body().getValue());
                            EHome.getInstance().setToken(response.body().getToken());
                            SharedPreferenceUtils.put(mContext, Code.SP_MD5_PASSWORD, MD5Utils.encode(password));
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            buttonLogin.setClickable(true);
                            if (response.body() != null) {
                                if (response.body().getMessage() != null || !response.body().getMessage().isEmpty()) {
                                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();

                                }
                            }

                        }

                    }

                    @Override
                    public void onError(Response<BaseModelResponse<User>> response) {
                        super.onError(response);
                        buttonLogin.setClickable(true);
                        if (response.body() != null) {
                            if (response.body().getMessage() != null || !response.body().getMessage().isEmpty()) {
                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
