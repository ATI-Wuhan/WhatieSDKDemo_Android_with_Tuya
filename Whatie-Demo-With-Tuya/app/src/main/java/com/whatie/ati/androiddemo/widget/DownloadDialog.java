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


/**
 * Created by Lan on 2016/10/25.
 */

public class DownloadDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "DownloadDialog";

    private TextView tvTitle;
    private TextView tvCancel;
    private NumberProgress npProgress;

    // 宽度占屏比
    public static final float WIDTH_FACTOR = 0.85f;

    private Context mContext;
    private DownloadDialogClickListener mListener;

    public DownloadDialog(Context context) {
        super(context, R.style.DownloadDialog);
        this.mContext = context;
        init();
    }

    private void init() {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_download, null);
        setContentView(view);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高
        lp.width = (int) (d.widthPixels * WIDTH_FACTOR);                // 设置对话框宽度
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.DownloadDialogAnimation);

        setCancelable(true);
        setCanceledOnTouchOutside(false);

        tvTitle = (TextView) view.findViewById(R.id.tv_dlg_download_title);
        tvCancel = (TextView) view.findViewById(R.id.tv_dlg_download_cancel);
        npProgress = (NumberProgress) view.findViewById(R.id.np_dlg_download);

        tvCancel.setOnClickListener(this);
    }

    public DownloadDialog setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    public DownloadDialog setCancelText(String text) {
        tvCancel.setText(text);
        return this;
    }

    public DownloadDialog setMaxProgress(int maxProgress) {
        npProgress.setMaxProgress(maxProgress);
        return this;
    }

    public DownloadDialog setCurrentProgress(int progress) {
        npProgress.setCurrentProgress(progress);
        return this;
    }

    @Override
    public void onClick(View v) {

        if (R.id.tv_dlg_download_cancel == v.getId()) {
            dismiss();
            if (mListener != null) {
                mListener.onCancelButtonClick();
            }
        }
    }

    public DownloadDialog setDownloadDialogClickListener(DownloadDialogClickListener listener) {
        this.mListener = listener;
        return this;
    }


    public interface DownloadDialogClickListener {
        void onCancelButtonClick();
    }

}
