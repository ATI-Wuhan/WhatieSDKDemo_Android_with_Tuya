package com.whatie.ati.androiddemo.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.utils.BaseRecyclerAdapter;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 神火 on 2018/6/7.
 */

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.ll_home_button)
    LinearLayout llHomeButton;
    @BindView(R.id.iv_main_profile)
    ImageView ivMainProfile;
    @BindView(R.id.tv_main_profile)
    TextView tvMainProfile;
    @BindView(R.id.ll_profile_button)
    LinearLayout llProfileButton;
    @BindView(R.id.rl_navigation_bar)
    LinearLayout rlNavigationBar;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.iv_main_home)
    ImageView ivMainHome;
    @BindView(R.id.tv_main_home)
    TextView tvMainHome;

    private Fragment mCurrentFragment = null;
    FragmentManager mFragmentManager = getSupportFragmentManager();
    private DeviceListFragment mDeviceListFragment;
    private ProfileFragment mProfileFragment;
    private boolean resumeRefresh = false;

    //    private MaterialDialog mUpdateDialog;
    private Object mDownloadTag = new Object();
    public static final int STORAGE = 0;
    private static final int CAMERA = 1;

    private String action;

    private BaseRecyclerAdapter<DeviceVo> mAdapter;
    private List<DeviceVo> mDeviceVos = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.act_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action = getIntent().getAction();
    }

    @Override
    protected void initViews() {
        ivMainHome.setImageResource(R.drawable.ic_main_home);
        ivMainProfile.setImageResource(R.drawable.ic_main_profile);
        homePressed();
        showHome();

    }

    private void homePressed() {
        tvMainHome.setTextColor(getResources().getColor(R.color.colorAccent));
        ivMainHome.setSelected(true);
        tvMainProfile.setTextColor(getResources().getColor(R.color.navibar_text_color));
        ivMainProfile.setSelected(false);


    }

    private void profilePressed() {
        tvMainProfile.setTextColor(getResources().getColor(R.color.colorAccent));
        ivMainProfile.setSelected(true);
        tvMainHome.setTextColor(getResources().getColor(R.color.navibar_text_color));
        ivMainHome.setSelected(false);
    }


    @Override
    protected void initEvents() {

    }


    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void initDatas() {

    }

    @OnClick({R.id.ll_home_button, R.id.ll_profile_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_home_button:
                homePressed();
                showHome();
                break;
            case R.id.ll_profile_button:
                profilePressed();
                showProfile();
                break;

        }
    }

    private void showHome() {
        if (mDeviceListFragment == null) {
            mDeviceListFragment = DeviceListFragment.newInstance();
        }
        addOrShowFragment(mDeviceListFragment);
//        if(action != null && action.equals("fromLogin")){
//            mDeviceListFragment.getDevices();
//        }

    }

    private void showProfile() {
        if (mProfileFragment == null) {
            mProfileFragment = ProfileFragment.newInstance();
        }
        addOrShowFragment(mProfileFragment);
    }

    private void addOrShowFragment(Fragment fragment) {   //???

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (mCurrentFragment != null) {
            if (mCurrentFragment == fragment) return;
            if (!fragment.isAdded()) {
                // 如果当前fragment未被添加，则添加到Fragment管理器中
                transaction.hide(mCurrentFragment)
                        .add(R.id.fl_content, fragment)
                        .commitAllowingStateLoss();
            } else {
                transaction.hide(mCurrentFragment).show(fragment).commitAllowingStateLoss();
            }
        } else {
            if (!fragment.isAdded()) {
                mFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.fl_content, fragment).commitAllowingStateLoss();
            } else {
                mFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .hide(mCurrentFragment).show(fragment).commitAllowingStateLoss();
            }
        }
        mCurrentFragment = fragment;
    }

}
