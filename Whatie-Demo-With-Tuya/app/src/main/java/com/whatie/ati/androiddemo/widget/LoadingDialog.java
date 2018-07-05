package com.whatie.ati.androiddemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.widget.spinnerview.SpinView;


/**
 * 自定义加载对话框
 */
public class LoadingDialog extends Dialog {

    private Context mContext = null;
    private TextView tvMsg;
    private SpinView svLoading;
    private String msg = "Loading...";

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialogStyle);
        this.mContext = context;

        init();
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_loading, null);
        setContentView(view);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高
        lp.width = (int) (d.widthPixels * 0.5);                         // 设置对话框宽度
        lp.height = (int) (d.widthPixels * 0.5);                       // 设置对话框高度
        dialogWindow.setAttributes(lp);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        tvMsg = (TextView) view.findViewById(R.id.tv_dlg_loading);
        svLoading = (SpinView) view.findViewById(R.id.sv_dlg_loading);
    }

    public LoadingDialog setMessage(String msg) {
        this.msg = msg;
        tvMsg.setText(msg);
        return this;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public void show() {
        tvMsg.setText(msg);
        super.show();

    }
}
