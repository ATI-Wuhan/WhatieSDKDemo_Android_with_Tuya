package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatie.ati.androiddemo.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by surface pro3 on 2018/4/23.
 */

public class TimeTypeActivity extends BaseActivity {
    private static final String TAG = "TimeTypeActivity";

    String day0="0000000";
    String day1="0";
    String day2="0";
    String day3="0";
    String day4="0";
    String day5="0";
    String day6="0";
    String day7="0";
    private String timeType = "000000";


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.iv_time_day1)
    ImageView ivTimeDay1;
    @BindView(R.id.iv_time_day2)
    ImageView ivTimeDay2;
    @BindView(R.id.iv_time_day3)
    ImageView ivTimeDay3;
    @BindView(R.id.iv_time_day4)
    ImageView ivTimeDay4;
    @BindView(R.id.iv_time_day5)
    ImageView ivTimeDay5;
    @BindView(R.id.iv_time_day6)
    ImageView ivTimeDay6;
    @BindView(R.id.iv_time_day7)
    ImageView ivTimeDay7;

    @Override
    protected int getContentViewId() {
        return R.layout.act_timer_day;
    }


    @Override
    protected void initViews() {
        tvTitle.setText(getString(R.string.time_day_setting_title));
        timeType = getIntent().getStringExtra("time_type");
        initTime();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onBackPressed() {
        sendTime();
    }

    @OnClick({R.id.ll_title_left, R.id.rl_time_day1,R.id.rl_time_day2,R.id.rl_time_day3,R.id.rl_time_day4,
            R.id.rl_time_day5,R.id.rl_time_day6,R.id.rl_time_day7})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left: {
                sendTime();
            }
            break;
            case R.id.rl_time_day1:
                if(ivTimeDay1.isShown()){
                    ivTimeDay1.setVisibility(View.GONE);
                }else {
                    ivTimeDay1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_time_day2:
                if(ivTimeDay2.isShown()){
                    ivTimeDay2.setVisibility(View.GONE);
                }else {
                    ivTimeDay2.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_time_day3:
                if(ivTimeDay3.isShown()){
                    ivTimeDay3.setVisibility(View.GONE);
                }else {
                    ivTimeDay3.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_time_day4:
                if(ivTimeDay4.isShown()){
                    ivTimeDay4.setVisibility(View.GONE);
                }else {
                    ivTimeDay4.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_time_day5:
                if(ivTimeDay5.isShown()){
                    ivTimeDay5.setVisibility(View.GONE);
                }else {
                    ivTimeDay5.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_time_day6:
                if(ivTimeDay6.isShown()){
                    ivTimeDay6.setVisibility(View.GONE);
                }else {
                    ivTimeDay6.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_time_day7:
                if(ivTimeDay7.isShown()){
                    ivTimeDay7.setVisibility(View.GONE);
                }else {
                    ivTimeDay7.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void sendTime(){
        if(ivTimeDay1.isShown()){
            day1="1";}
        if(ivTimeDay2.isShown()){
            day2="1";}
        if(ivTimeDay3.isShown()){
            day3="1";}
        if(ivTimeDay4.isShown()){
            day4="1";}
        if(ivTimeDay5.isShown()){
            day5="1";}
        if(ivTimeDay6.isShown()){
            day6="1";}
        if(ivTimeDay7.isShown()){
            day7="1";}

        day0=day7+day6+day5+day4+day3+day2+day1;
        Intent intent=new Intent();
        intent.putExtra("time_day_setting",day0);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void initTime() {
        if (timeType.charAt(0) == '1') {
            ivTimeDay7.setVisibility(View.VISIBLE);
        }
        if (timeType.charAt(1) == '1') {
            ivTimeDay6.setVisibility(View.VISIBLE);
        }
        if (timeType.charAt(2) == '1') {
            ivTimeDay5.setVisibility(View.VISIBLE);
        }
        if (timeType.charAt(3) == '1') {
            ivTimeDay4.setVisibility(View.VISIBLE);
        }
        if (timeType.charAt(4) == '1') {
            ivTimeDay3.setVisibility(View.VISIBLE);
        }
        if (timeType.charAt(5) == '1') {
            ivTimeDay2.setVisibility(View.VISIBLE);
        }
        if (timeType.charAt(6) == '1') {
            ivTimeDay1.setVisibility(View.VISIBLE);
        }
    }

}