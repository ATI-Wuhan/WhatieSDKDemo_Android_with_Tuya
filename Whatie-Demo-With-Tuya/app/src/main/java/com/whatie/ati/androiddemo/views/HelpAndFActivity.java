package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whatie.ati.androiddemo.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 神火 on 2018/6/27.
 */

public class HelpAndFActivity extends BaseActivity {
    private static final String TAG = "HelpAndFActivity";

    @BindView(R.id.tv_help_submit)
    TextView tvSkip;
    @BindView(R.id.tv_title)
    TextView tvSkipTitle;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;

    @Override
    protected int getContentViewId() {
        return R.layout.act_help_feedback;
    }

    protected void initViews() {
        tvSkipTitle.setText(getString(R.string.help_feedback_title));
    }


    protected void initDatas() {
    }

    protected void initPresenter() {
    }

    protected void initEvents() {
    }

    @OnClick({R.id.ll_title_left, R.id.tv_help_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.tv_help_submit:
                Intent submitIntent = new Intent(HelpAndFActivity.this, AddFeedBackActivity.class);
                startActivity(submitIntent);
                break;
        }
    }
}

