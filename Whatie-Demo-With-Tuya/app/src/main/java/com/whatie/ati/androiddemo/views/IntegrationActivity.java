package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.constants.EhomeResource;
import com.d9lab.ati.whatiesdk.util.Code;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 神火 on 2018/6/27.
 */

public class IntegrationActivity extends BaseActivity {
    @BindView(R.id.iv_integration)
    ImageView ivIntegration;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int getContentViewId() {
        return R.layout.act_integration;
    }



    @Override
    protected void initViews() {
        tvTitle.setText(getString(R.string.integration));
        ivIntegration.setImageResource(EhomeResource.IC_AMAZON[Code.THEME_NOW]);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {

    }


    @OnClick({R.id.ll_title_left, R.id.iv_integration})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.iv_integration:
                Intent integrationIntent = new Intent(IntegrationActivity.this, IntegrationAmazonActivity.class);
                startActivity(integrationIntent);
                break;
        }
    }
}
