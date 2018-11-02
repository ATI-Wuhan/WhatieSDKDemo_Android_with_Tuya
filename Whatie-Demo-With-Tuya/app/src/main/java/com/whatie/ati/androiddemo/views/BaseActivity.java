package com.whatie.ati.androiddemo.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.application.AppManager;
import com.whatie.ati.androiddemo.utils.WindowUtil;
import com.whatie.ati.androiddemo.widget.LoadingDialog;
import com.d9lab.ati.whatiesdk.ehome.EHome;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.drakeet.materialdialog.MaterialDialog;


/**
 * Created by liz on 2018/2/26.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private Unbinder mUnBinder;
    protected Context mContext = null;
    protected LoadingDialog mLoadingDialog;
    protected MaterialDialog materialDialog;


    private TextView tvStatusBar;
    private RelativeLayout rlTitleBar;

    private TextView tvTitle;
    private TextView tvTitleRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        WindowUtil.requestStatusBar(this);
        AppManager.getInstance().addActivity(this);

        if (getContentViewId() != 0) {
            setContentView(getContentViewId());
        }
        mUnBinder = ButterKnife.bind(this);
        mContext = this;
        mLoadingDialog = new LoadingDialog(mContext);
        materialDialog = new MaterialDialog(mContext);

        tvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        rlTitleBar = (RelativeLayout) findViewById(R.id.rl_title_bg);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitleRight = (TextView) findViewById(R.id.tv_title_right);

        if (tvStatusBar != null) {
            tvStatusBar.getLayoutParams().height = WindowUtil.getStatusBarHeight(this);
        }

        initViews();
        initEvents();
        initDatas();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        AppManager.getInstance().addActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EHome.getInstance().cancelTag(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(EHome.getInstance().isLogin() && !EHome.getInstance().isMqttOn()){
            EHome.getInstance().startMqttService();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        AppManager.getInstance().finishActivity(this);
    }

    /**
     * 绑定布局文件
     */
    protected abstract int getContentViewId();

    /**
     * 初始化控件
     */
    protected abstract void initViews();

    /**
     * 事件绑定
     */
    protected abstract void initEvents();

    /**
     * 初始化数据
     */
    protected abstract void initDatas();
    /**
     * 设置状态栏的颜色
     */
    public void setStatusBarColor(int colorInt) {
        if (tvStatusBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
                tvStatusBar.setBackground(getDrawable(colorInt));
            } else {
                tvStatusBar.setBackgroundColor(getResources().getColor(colorInt));
            }
        }
        if (colorInt == R.color.white || colorInt == R.color.transparent) {
            setStatusTextDark();
        }
    }

    /**
     * 设置title背景色
     *
     * @param colorInt
     */
    public void setTitleBarColor(int colorInt) {
        if (rlTitleBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rlTitleBar.setBackground(getDrawable(colorInt));
            } else {
                rlTitleBar.setBackgroundColor(getResources().getColor(colorInt));
            }
            if (colorInt == R.color.white) {
                tvTitle.setTextColor(getResources().getColor(R.color.black_title_text));
                tvTitleRight.setTextColor(getResources().getColor(R.color.black_title_text));
            }
        }

    }

    /**
     * 状态栏文字及图标设为黑色
     */
    public void setStatusTextDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 状态栏文字及图标设为白色
     */
    public void setStatusTextLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}
