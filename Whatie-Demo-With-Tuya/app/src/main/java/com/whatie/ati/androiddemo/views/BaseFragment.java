package com.whatie.ati.androiddemo.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.utils.WindowUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 神火 on 2018/6/7.
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    private Unbinder mUnBinder;
    private TextView tvStatusBar;
    private RelativeLayout rlTitleBar;

    private TextView tvTitle;
    private TextView tvTitleRight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayoutId(), container, false);
        mUnBinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvStatusBar = view.findViewById(R.id.tv_status_bar);
        rlTitleBar = view.findViewById(R.id.rl_title_bg);
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitleRight = view.findViewById(R.id.tv_title_right);

        if (tvStatusBar != null) {
            tvStatusBar.getLayoutParams().height = WindowUtil.getStatusBarHeight(mContext);
        }
        initViews();
        initEvents();
        initDatas();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OkGo.getInstance().cancelTag(mContext);
        mUnBinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected abstract int getFragmentLayoutId();
    protected abstract void initViews();
    protected abstract void initEvents();
    protected abstract void initDatas();
    /**
     * 设置状态栏的颜色
     */
    public void setStatusBarColor(int colorInt) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            tvStatusBar.setBackground(mContext.getDrawable(colorInt));
        } else {
            tvStatusBar.setBackgroundColor(getResources().getColor(colorInt));
        }
        if (colorInt == R.color.white || colorInt == R.color.transparent) {
            setStatusTextDark();
        }
    }

    /**
     * 设置title背景色
     * @param colorInt
     */
    public void setTitleBarColor(int colorInt) {
        if (rlTitleBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rlTitleBar.setBackground(mContext.getDrawable(colorInt));
            } else {
                rlTitleBar.setBackgroundColor(getResources().getColor(colorInt));
            }
            if(colorInt == R.color.white) {
                tvTitle.setTextColor(getResources().getColor(R.color.black_title_text));
                tvTitleRight.setTextColor(getResources().getColor(R.color.black_title_text));
            }
        }
    }

    /**
     * 状态栏文字及图标设为白色
     */
    public void setStatusTextDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 状态栏文字及图标设为黑色
     */
    public void setStatusTextLight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity().getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

}
