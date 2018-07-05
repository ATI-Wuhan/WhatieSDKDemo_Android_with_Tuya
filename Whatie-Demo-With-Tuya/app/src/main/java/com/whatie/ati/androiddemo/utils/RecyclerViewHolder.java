package com.whatie.ati.androiddemo.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.constants.Constants;
import com.whatie.ati.androiddemo.widget.togglebutton.ToggleButton;
import com.d9lab.ati.whatiesdk.util.LogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.lzy.okgo.cache.CacheMode.NO_CACHE;


public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "RecyclerViewHolder";
    private SparseArray<View> mViews;//集合类，layout里包含的View,以view的id作为key，value是view对象
    private Context mContext;//上下文对象

    public RecyclerViewHolder(Context ctx, View itemView) {
        super(itemView);
        mContext = ctx;
        mViews = new SparseArray<View>();
    }

    private <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getView(int viewId) {
        return findViewById(viewId);
    }

    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    public ImageButton getImageButton(int viewId) {
        return (ImageButton) getView(viewId);
    }

    public EditText getEditText(int viewId) {
        return (EditText) getView(viewId);
    }

    public LinearLayout getLinearLayout(int viewId) {
        return (LinearLayout) getView(viewId);
    }

    public RelativeLayout getRelativeLayout(int viewId) {
        return (RelativeLayout) getView(viewId);
    }

    public RecyclerViewHolder setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }
    public RecyclerViewHolder setToggleState(int viewId, boolean state) {
        ToggleButton view = (ToggleButton) findViewById(viewId);
        if(state){
            view.toggleOn();
        }else {
            view.toggleOff();
        }
        return this;
    }
    public RecyclerViewHolder setImageCache(int viewId, String path){
        ImageView view = findViewById(viewId);
        Picasso.with(mContext).load(new File(path))
                .error(R.mipmap.default_image)
                .into(view);
        return this;
    }
    public RecyclerViewHolder saveImageCache(final int viewId, String url, String fileName){
        File file = new File(Constants.getCachePath(mContext), fileName);
        LogUtil.log(TAG, "saveImageCache: " + fileName+ file.exists());
        if( file.exists() ){
            ImageView view = findViewById(viewId);
            Picasso.with(mContext).load(file)
                    .error(R.mipmap.default_image)
                    .into(view);
        }else {
            OkGo.<File>get(url)
                    .tag(this)
                    .cacheKey(url)
                    .cacheMode(NO_CACHE)
                    .cacheTime(-1)
                    .execute(new FileCallback(Constants.getCachePath(mContext), fileName){
                        @Override
                        public void onSuccess(Response<File> response) {
                            setImageCache(viewId, response.body().getAbsolutePath());
                        }

                        @Override
                        public void onError(Response<File> response) {

                        }
                    });
        }

        return this;
    }

    public RecyclerViewHolder setTextColor(int viewId, int color) {
        TextView view = findViewById(viewId);
        view.setTextColor(color);
        return this;
    }

    public RecyclerViewHolder setImageResource(int viewId, int imageResId) {
        ImageView view = findViewById(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public RecyclerViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public RecyclerViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public RecyclerViewHolder setLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = findViewById(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public RecyclerViewHolder setSwitchState(int viewId, boolean state){
        Switch view = findViewById(viewId);
        view.setChecked(state);
        return  this;
    }


}