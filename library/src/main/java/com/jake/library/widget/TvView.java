package com.jake.library.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.jake.library.MediaControllerImp;
import com.jake.library.MediaPlayerHelper;

/**
 * description：
 *
 * @author Administrator
 * @since 2016/10/30 19:43
 */


public class TvView extends FrameLayout {
    private MediaControllerImp mMediaControllerImp;

    public TvView(Context context) {
        this(context, null);
    }

    public TvView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TvView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMediaControllerImp = new MediaControllerImp(context, MediaPlayerHelper.getIjkMediaPlayer(MediaPlayerHelper.getDefaultIjkMediaPlayerBuilder()), new SurfaceRenderView(context));
        addView(mMediaControllerImp.getView());
    }

    public void setUri(Uri uri) {
        if (uri != null) {
            mMediaControllerImp.setURI(uri);
        }
    }
    public void start(){
        mMediaControllerImp.start();
    }
}
