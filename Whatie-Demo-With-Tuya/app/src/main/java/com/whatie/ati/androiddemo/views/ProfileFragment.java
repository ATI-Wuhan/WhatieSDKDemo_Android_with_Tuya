package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.constants.Constants;
import com.whatie.ati.androiddemo.constants.EhomeResource;
import com.whatie.ati.androiddemo.widget.CircleImageView;
import com.d9lab.ati.whatiesdk.bean.BaseModelResponse;
import com.d9lab.ati.whatiesdk.bean.User;
import com.d9lab.ati.whatiesdk.callback.UserCallback;
import com.d9lab.ati.whatiesdk.ehome.EHome;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.util.Code;
import com.d9lab.ati.whatiesdk.util.SharedPreferenceUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static com.lzy.okgo.cache.CacheMode.NO_CACHE;

/**
 * Created by 神火 on 2018/6/7.
 */

public class ProfileFragment extends BaseFragment {
    @BindView(R.id.tv_profile_name)
    TextView tvProfileName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    @BindView(R.id.iv_profile_photo)
    CircleImageView ivProfilePhoto;

    private static String USER_ICON_FILE_NAME_CAMERA = "IconCamera.jpg";

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.frag_profile;
    }


    @Override
    protected void initViews() {

        tvTitle.setText(getString(R.string.profile_title));
        llTitleLeft.setVisibility(View.GONE);
        llTitleRight.setVisibility(View.GONE);
        if ((int) SharedPreferenceUtils.get(mContext, Code.SP_USER_ID, -1) == -1) {
            tvTitleRight.setText(R.string.personal_login);
            tvTitleRight.setVisibility(View.VISIBLE);
        } else {
            tvProfileName.setText((String) SharedPreferenceUtils.get(mContext, Code.SP_USER_NAME, ""));
            saveImageCache((String) SharedPreferenceUtils.get(mContext, Code.SP_USER_THUMB, ""));
            if (EHome.getInstance().isLogin()) {
                ivTitleRight.setImageResource(R.drawable.ic_settings);
                ivTitleRight.setVisibility(View.VISIBLE);
                ivTitleLeft.setImageResource(R.drawable.ic_scan);
                ivTitleLeft.setVisibility(View.VISIBLE);
                tvTitleLeft.setVisibility(View.GONE);
            } else {
                llTitleLeft.setVisibility(View.GONE);
                tvTitleRight.setText(R.string.personal_login);
            }
        }
        ivProfilePhoto.setImageResource(EhomeResource.IC_AVATAR[Code.THEME_NOW]);
        if (!EHome.getInstance().isLogin()) {
            ivProfilePhoto.setImageResource(EhomeResource.IC_AVATAR[Code.THEME_NOW]);
        }
        if ((int) SharedPreferenceUtils.get(mContext, Code.SP_USER_ID, -1) == -1) {
            tvTitleRight.setVisibility(View.VISIBLE);
        }
        if (EHome.getInstance().isLogin()) {
            tvTitleRight.setVisibility(View.GONE);
            saveImageCache(EHome.getInstance().getmUser().getPortraitThumb().getPath());
        }
    }

    public void saveImageCache(String url) {
        Log.d("profilefragment", "saveImageCache: "   +url);
        File file = new File(Constants.getAppImageFolder(), USER_ICON_FILE_NAME_CAMERA);
        if (file.exists()) {
            Log.d("profilefragment", "saveImageCache:    file.exists"  );

            Picasso.with(mContext).load(file)
                    .error(R.mipmap.default_image)
                    .into(ivProfilePhoto);
        } else {
            Log.d("profilefragment", "saveImageCache:    else"  );
            OkGo.<File>get(url)
                    .tag(this)
                    .cacheKey(url)
                    .cacheMode(NO_CACHE)
                    .cacheTime(-1)
                    .execute(new FileCallback(Constants.getAppImageFolder(), USER_ICON_FILE_NAME_CAMERA) {
                        @Override
                        public void onSuccess(Response<File> response) {

                            Picasso.with(mContext).load(new File(response.body().getAbsolutePath()))
                                    .error(R.mipmap.default_image)
                                    .into(ivProfilePhoto);
                        }

                        @Override
                        public void onError(Response<File> response) {
                        }
                    });
        }
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onResume() {
        super.onResume();
        tvProfileName.setText((String) SharedPreferenceUtils.get(getContext(), "userName", "userName"));
        EHomeInterface.getINSTANCE().getCustomerInfo(mContext, new UserCallback() {
            @Override
            public void onSuccess(Response<BaseModelResponse<User>> response) {
                if (response.body().isSuccess()) {
                    if (response.body().getValue().getPortraitThumb().getPath() != null) {
                        SharedPreferenceUtils.put(mContext, Code.SP_USER_THUMB, response.body().getValue().getPortraitThumb().getPath());
                        Log.d("profilefragment", "onSuccess: "+(String) SharedPreferenceUtils.get(mContext, Code.SP_USER_THUMB, ""));

                    }
                }
            }
        });
        saveImageCache((String) SharedPreferenceUtils.get(mContext, Code.SP_USER_THUMB, ""));


    }

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @OnClick({R.id.iv_profile_photo, R.id.rl_profile_menu_my_devices, R.id.rl_profile_menu_integration,
            R.id.rl_profile_menu_help_feedback, R.id.tv_title_right, R.id.ll_title_left, R.id.iv_title_right, R.id.rl_profile_menu_devices_sharing})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_profile_photo:
                Intent personalIntent = new Intent(getActivity(), PersonalActivity.class);
                startActivity(personalIntent);
                break;
            case R.id.rl_profile_menu_my_devices:
//                Intent deviceListIntent = new Intent(getActivity(), MyDeviceListActivity.class);
//                startActivity(deviceListIntent);
                break;
            case R.id.rl_profile_menu_integration:
                Intent IntegrationIntent = new Intent(getActivity(), IntegrationActivity.class);
                startActivity(IntegrationIntent);
                break;

            case R.id.rl_profile_menu_help_feedback:
                Intent helpIntent = new Intent(getActivity(), HelpAndFActivity.class);
                startActivity(helpIntent);
                break;
            case R.id.tv_title_right:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.iv_title_right:
//                startActivity(new Intent(getActivity(),AboutActivity.class));
                break;
            case R.id.ll_title_left:
//                startActivity(new Intent(getActivity(),ScanQRcodeActivity.class));
                break;
            case R.id.rl_profile_menu_devices_sharing:
                startActivity(new Intent(getActivity(), SharedDeviceListActivity.class));
                break;

        }

    }


}
