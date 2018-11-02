package com.whatie.ati.androiddemo.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.whatie.ati.androiddemo.R;


public class ToastUtil {
    private static boolean isShow = true;//默认显示
    private static Toast mToast = null;//全局唯一的Toast

    /*private控制不应该被实例化*/
    private ToastUtil() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 全局控制是否显示Toast
     *
     * @param isShowToast
     */
    public static void controlShow(boolean isShowToast) {
        isShow = isShowToast;
    }

    /**
     * 取消Toast显示
     */
    public void cancelToast() {
        if (isShow && mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param resId   资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showShort(Context context, int resId) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param resId   资源ID:getResources().getString(R.string.xxxxxx);
     */
    public static void showLong(Context context, int resId) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration 单位:毫秒
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param resId    资源ID:getResources().getString(R.string.xxxxxx);
     * @param duration 单位:毫秒
     */
    public static void show(Context context, int resId, int duration) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, duration);
            } else {
                mToast.setText(resId);
            }
            mToast.show();
        }
    }

    /**
     * 自定义Toast的View
     *
     * @param context
     * @param message
     * @param duration 单位:毫秒
     * @param view     显示自己的View
     */
    public static void customToastView(Context context, CharSequence message, int duration, View view) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            if (view != null) {
                mToast.setView(view);
            }
            mToast.show();
        }
    }

    /**
     * 自定义Toast的位置
     *
     * @param context
     * @param message
     * @param duration 单位:毫秒
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void customToastGravity(Context context, CharSequence message, int duration, int gravity, int xOffset, int yOffset) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.setGravity(gravity, xOffset, yOffset);
            mToast.show();
        }
    }

    /**
     * 自定义带图片和文字的Toast，最终的效果就是上面是图片，下面是文字
     *
     * @param context
     * @param message
     * @param iconResId 图片的资源id,如:R.drawable.icon
     * @param duration
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void showToastWithImageAndText(Context context, CharSequence message, int iconResId, int duration, int gravity, int xOffset, int yOffset) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.setGravity(gravity, xOffset, yOffset);
            LinearLayout toastView = (LinearLayout) mToast.getView();
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(iconResId);
            toastView.addView(imageView, 0);
            mToast.show();
        }
    }

    /**
     * 自定义Toast,针对类型CharSequence
     *
     * @param context
     * @param message
     * @param duration
     * @param view
     * @param isGravity        true,表示后面的三个布局参数生效,false,表示不生效
     * @param gravity
     * @param xOffset
     * @param yOffset
     * @param isMargin         true,表示后面的两个参数生效，false,表示不生效
     * @param horizontalMargin
     * @param verticalMargin
     */
    public static void customToastAll(Context context, CharSequence message, int duration, View view, boolean isGravity, int gravity, int xOffset, int yOffset, boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
            } else {
                mToast.setText(message);
            }
            if (view != null) {
                mToast.setView(view);
            }
            if (isMargin) {
                mToast.setMargin(horizontalMargin, verticalMargin);
            }
            if (isGravity) {
                mToast.setGravity(gravity, xOffset, yOffset);
            }
            mToast.show();
        }
    }

    /**
     * 自定义Toast,针对类型resId
     *
     * @param context
     * @param resId
     * @param duration
     * @param view             :应该是一个布局，布局中包含了自己设置好的内容
     * @param isGravity        true,表示后面的三个布局参数生效,false,表示不生效
     * @param gravity
     * @param xOffset
     * @param yOffset
     * @param isMargin         true,表示后面的两个参数生效，false,表示不生效
     * @param horizontalMargin
     * @param verticalMargin
     */
    public static void customToastAll(Context context, int resId, int duration, View view, boolean isGravity, int gravity, int xOffset, int yOffset, boolean isMargin, float horizontalMargin, float verticalMargin) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, resId, duration);
            } else {
                mToast.setText(resId);
            }
            if (view != null) {
                mToast.setView(view);
            }
            if (isMargin) {
                mToast.setMargin(horizontalMargin, verticalMargin);
            }
            if (isGravity) {
                mToast.setGravity(gravity, xOffset, yOffset);
            }
            mToast.show();
        }
    }

    public static int codeToStringId(int code) {
        int stringId = R.string.unknown_error;
        switch (code) {
            case 10001:
                stringId = R.string.response10001;
                break;
            case 10002:
                stringId = R.string.response10002;
                break;
            case 10003:
                stringId = R.string.response10003;
                break;
            case 10101:
                stringId = R.string.response10101;
                break;
            case 10102:
                stringId = R.string.response10102;
                break;
            case 10201:
                stringId = R.string.response10201;
                break;
            case 10202:
                stringId = R.string.response10202;
                break;
            case 10301:
                stringId = R.string.response10301;
                break;
            case 10302:
                stringId = R.string.response10302;
                break;
            case 10401:
                stringId = R.string.response10401;
                break;
            case 10402:
                stringId = R.string.response10402;
                break;
            case 10501:
                stringId = R.string.response10501;
                break;
            case 10602:
                stringId = R.string.response10602;
                break;
            case 10603:
                stringId = R.string.response10603;
                break;
            case 10701:
                stringId = R.string.response10701;
                break;
            case 10702:
                stringId = R.string.response10702;
                break;
            case 10703:
                stringId = R.string.response10703;
                break;
            case 10704:
                stringId = R.string.response10704;
                break;
            case 10705:
                stringId = R.string.response10705;
                break;
            case 10801:
                stringId = R.string.response10801;
                break;
            case 10802:
                stringId = R.string.response10802;
                break;
            case 10803:
                stringId = R.string.response10803;
                break;
            case 10804:
                stringId = R.string.response10804;
                break;
            case 10901:
                stringId = R.string.response10901;
                break;
            case 10902:
                stringId = R.string.response10902;
                break;
            case 11001:
                stringId = R.string.response11001;
                break;
            case 11101:
                stringId = R.string.response11101;
                break;
            case 11201:
                stringId = R.string.response11201;
                break;
            case 11301:
                stringId = R.string.response11301;
                break;
            case 11302:
                stringId = R.string.response11302;
                break;
            case 11303:
                stringId = R.string.response11303;
                break;
            case 11304:
                stringId = R.string.response11304;
                break;
            case 11401:
                stringId = R.string.response11401;
                break;
            case 11402:
                stringId = R.string.response11402;
                break;
            case 11501:
                stringId = R.string.response11501;
                break;
            case 11601:
                stringId = R.string.response11601;
                break;
            case 11701:
                stringId = R.string.response11701;
                break;
            case 11702:
                stringId = R.string.response11702;
                break;
            case 11801:
                stringId = R.string.response11801;
                break;
            case 11802:
                stringId = R.string.response11802;
                break;
            case 11803:
                stringId = R.string.response11803;
                break;
            case 11901:
                stringId = R.string.response11901;
                break;
            case 12001:
                stringId = R.string.response12001;
                break;
            case 12101:
                stringId = R.string.response12101;
                break;
            case 12102:
                stringId = R.string.response12102;
                break;
            case 12103:
                stringId = R.string.response12103;
                break;
            case 12201:
                stringId = R.string.response12201;
                break;
            case 12301:
                stringId = R.string.response12301;
                break;
            case 12302:
                stringId = R.string.response12302;
                break;
            case 12401:
                stringId = R.string.response12401;
                break;
            case 12501:
                stringId = R.string.response12501;
                break;
            case 12601:
                stringId = R.string.response12601;
                break;
            case 12602:
                stringId = R.string.response12602;
                break;
            case 20101:
                stringId = R.string.response20101;
                break;
            case 20201:
                stringId = R.string.response20201;
                break;
            case 20301:
                stringId = R.string.response20301;
                break;
            case 20302:
                stringId = R.string.response20302;
                break;
            case 20401:
                stringId = R.string.response20401;
                break;
            case 20501:
                stringId = R.string.response20501;
                break;
            case 20601:
                stringId = R.string.response20601;
                break;
            case 20701:
                stringId = R.string.response20701;
                break;
        }
        return stringId;
    }

}
