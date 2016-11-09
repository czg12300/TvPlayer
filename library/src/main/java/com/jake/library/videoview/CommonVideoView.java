package com.jake.library.videoview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jake.library.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jakechen on 2016/11/9.
 */

public class CommonVideoView extends IjkVideoView {
    public CommonVideoView(Context context) {
        super(context);
    }

    public CommonVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private PopDialog mPopDialog;

    @Override
    protected void showVolumeView(float percent) {
        checkPopDialog();
        mPopDialog.setIconResId(R.drawable.ic_volume);
        mPopDialog.setProgress(percent);
        mPopDialog.show();
    }

    @Override
    protected void changeVolumeProgress(float percent) {
        mPopDialog.setProgress(percent);
        mPopDialog.show();
    }

    @Override
    protected void dismissVolumeView() {
        mPopDialog.dismiss();
    }

    @Override
    protected void showBrightnessView(float progress) {
        checkPopDialog();
        mPopDialog.setIconResId(R.drawable.ic_brightness);
        mPopDialog.setProgress(progress);
        mPopDialog.show();
    }

    private void checkPopDialog() {
        if (mPopDialog == null) {
            mPopDialog = new PopDialog(getContext());
        }
    }

    @Override
    protected void changeBrightnessProgress(float progress) {
        mPopDialog.setProgress(progress);
        mPopDialog.show();
    }

    @Override
    protected void dismissBrightnessView() {
        mPopDialog.dismiss();
    }

    @Override
    protected void showSeekView(boolean isForward, int currentPosition, int oldCurrentPosition, int duration) {
        checkPopDialog();
        mPopDialog.setIconResId(isForward ? R.drawable.ic_fast_forward : R.drawable.ic_fast_back_off);
        mPopDialog.setProgress((float) oldCurrentPosition / (float) duration);
        mPopDialog.setTime(currentPosition, duration);
        mPopDialog.show();
    }

    @Override
    protected void changeSeekProgress(boolean isForward, int currentPosition, int oldCurrentPosition, int duration) {
        mPopDialog.setIconResId(isForward ? R.drawable.ic_fast_forward : R.drawable.ic_fast_back_off);
        mPopDialog.setProgress((float) oldCurrentPosition / (float) duration);
        mPopDialog.setTime(currentPosition, duration);
        mPopDialog.show();
    }

    @Override
    protected void dismissSeekView() {
        mPopDialog.dismiss();
    }

    private static class PopDialog extends Dialog {
        private ProgressBar progressBar;
        private ImageView ivIcon;
        private TextView tvTime;

        public PopDialog(Context context) {
            super(context, R.style.Translucent_NoTitle);
            configWindow(context);
            View view = View.inflate(context, R.layout.dialog_common_video_volume, null);
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            progressBar = (ProgressBar) view.findViewById(R.id.pb);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            progressBar.setMax(100);
            setContentView(view);
        }


        public void setIconResId(int iconResId) {
            ivIcon.setImageResource(iconResId);
        }


        public void setTime(long position, long duration) {
            tvTime.setVisibility(VISIBLE);
            tvTime.setText(buildSpennableText(buildColorSpennableText(Color.WHITE, formatTime(position)), buildColorSpennableText(Color.parseColor("#88ffffff"), "  /  "), buildColorSpennableText(Color.parseColor("#88ffffff"), formatTime(duration))));
        }

        public void setProgress(float progress) {
            tvTime.setVisibility(GONE);
            progressBar.setProgress((int) (progress * 100));
        }

        private String formatTime(long timeSeconds) {
            StringBuilder result = new StringBuilder();
// 负数转为零
            timeSeconds = Math.max(timeSeconds, 0);
            // 各单位的秒数
            long hSpit = 60 * 60000;
            long mSpit = 60000;
            long sSpit = 1000;
//            long SSpit = 1;
            // 计各单位的数值
            long h = timeSeconds / hSpit;
            long m = (timeSeconds % hSpit) / mSpit;
            long s = (timeSeconds % hSpit % mSpit) / sSpit;
//            long S = (timeSeconds % mSpit % sSpit) / SSpit;
            appendTimeWithZero(h, result);
            result.append(":");
            appendTimeWithZero(m, result);
            result.append(":");
            appendTimeWithZero(s, result);
//            result.append(":");
//            appendTimeWithZero(S, result);
            return result.toString();
        }

        /**
         * 创建特殊字体颜色的字符串
         *
         * @param color
         * @param charSequence
         * @return
         */
        public static CharSequence buildColorSpennableText(int color, CharSequence charSequence) {
            if (charSequence != null) {
                SpannableString ss = new SpannableString(charSequence);
                ss.setSpan(new ForegroundColorSpan(color), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return ss;
            }
            return null;

        }

        /**
         * 拼接不同的特殊字体
         *
         * @param css
         * @return
         */
        public static SpannableStringBuilder buildSpennableText(CharSequence... css) {
            if (css != null && css.length > 0) {
                SpannableStringBuilder builder = new SpannableStringBuilder();
                for (CharSequence charSequence : css) {
                    if (charSequence != null) {
                        builder.append(charSequence);
                    }
                }
                return builder;
            }
            return null;
        }

        private void appendTimeWithZero(long time, StringBuilder builder) {
            if (time < 10) {
                builder.append("0" + time);
            } else {
                if (time < 100) {
                    builder.append(time);
                } else {
                    builder.append(String.valueOf(time).substring(0, 2));
                }
            }
        }

        private void configWindow(Context context) {
            Window window = getWindow();
            if (!(context instanceof Activity)) {
                window.setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
            }
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
        }
    }
}
