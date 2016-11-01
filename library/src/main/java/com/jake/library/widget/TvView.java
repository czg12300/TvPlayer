package com.jake.library.widget;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.jake.library.IMediaPlayerBuilder;
import com.jake.library.MediaControllerImp;

/**
 * description：
 *
 * @author Administrator
 * @since 2016/10/30 19:43
 */


public class TvView extends FrameLayout {
    private MediaControllerImp mMediaControllerImp;
    private static final int[] s_allAspectRatio = {
            IRenderView.AR_ASPECT_FIT_PARENT,
            IRenderView.AR_ASPECT_FILL_PARENT,
            IRenderView.AR_ASPECT_WRAP_CONTENT,
            // IRenderView.AR_MATCH_PARENT,
            IRenderView.AR_16_9_FIT_PARENT,
            IRenderView.AR_4_3_FIT_PARENT};

    public TvView(Context context) {
        this(context, null);
    }

    public TvView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TvView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        IMediaPlayerBuilder playerBuilder = getIMediaPlayerBuilder(context);
        IRenderView renderView = new SurfaceRenderView(context);
//        renderView.setAspectRatio(IRenderView.AR_16_9_FIT_PARENT);
        mMediaControllerImp = new MediaControllerImp(context, playerBuilder, renderView);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        addView(mMediaControllerImp.getView(), lp);
    }

    @NonNull
    private IMediaPlayerBuilder getIMediaPlayerBuilder(Context context) {
        IMediaPlayerBuilder playerBuilder = IMediaPlayerBuilder.create(context, IMediaPlayerBuilder.TYPE_IJK);
        IMediaPlayerBuilder.IjkMediaPlayerBuilder ijkMediaPlayerBuilder = IMediaPlayerBuilder.IjkMediaPlayerBuilder.create();
        ijkMediaPlayerBuilder.setUsingOpenSLES();
        ijkMediaPlayerBuilder.setMediaCodec(true, true);
        playerBuilder.setIjkMediaPlayerBuilder(ijkMediaPlayerBuilder);
        return playerBuilder;
    }

    public void setUri(Uri uri) {
        if (uri != null) {
            mMediaControllerImp.setURI(uri);
        }

    }

    public void start() {
        mMediaControllerImp.start();
    }

    public MediaControllerImp getMediaController() {
        return mMediaControllerImp;
    }

}
