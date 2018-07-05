package com.whatie.ati.androiddemo.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;

public class ColorBar extends AppCompatSeekBar {

    //最大为1535时，起点和终点颜色值一样，反向控制会出现问题
    private static final int MAX_PROGRESS = 1534;

    public ColorBar(Context context) {
        super(context);
        setMax(MAX_PROGRESS);
    }

    public ColorBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMax(MAX_PROGRESS);
    }

    public ColorBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMax(MAX_PROGRESS);
    }

    public int[] getRGBValue() {
        int r = 0;
        int g = 0;
        int b = 0;
        int i = getProgress();
        if (0 <= i && i < 256) {
            r = 255;
            g = i;
            b = 0;
        } else if (255 < i && i < 512) {
            r = 511 - i;
            g = 255;
            b = 0;
        } else if (511 < i && i < 768) {
            r = 0;
            g = 255;
            b = i - 512;
        } else if (767 < i && i < 1024) {
            r = 0;
            g = 1023 - i;
            b = 255;
        } else if (1023 < i && i < 1280) {
            r = i - 1024;
            g = 0;
            b = 255;
        } else if (1279 < i && i < MAX_PROGRESS + 1) {
            r = 255;
            g = 0;
            b = 1535 - i;
        }
        return new int[]{r, g, b};
    }

    public void setProgress(int[] rgb) {
        int r = rgb[0];
        int g = rgb[1];
        int b = rgb[2];
        int progress = 0;
        if (r == 255 && b == 0) {
            progress = g;
        } else if (g == 255 && b == 0) {
            progress = 511 - r;
        } else if (r == 0 && g == 255) {
            progress = b + 512;
        } else if (r == 0 && b == 255) {
            progress = 1023 - g;
        } else if (g == 0 && b == 255) {
            progress = r + 1024;
        } else if (r == 255 && g == 0) {
            progress = 1535 - b;
        }
        setProgress(progress);
    }

}
