package com.jake.library;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.SurfaceHolder;
import android.view.TextureView;

import tv.danmaku.ijk.media.player.ISurfaceTextureHost;

public interface IRenderView {
    int AR_ASPECT_FIT_PARENT = 0; // without clip
    int AR_ASPECT_FILL_PARENT = 1; // may clip
    int AR_ASPECT_WRAP_CONTENT = 2;
    int AR_MATCH_PARENT = 3;
    int AR_16_9_FIT_PARENT = 4;
    int AR_4_3_FIT_PARENT = 5;

    Context getApplicationContext();

    void setVideoSize(int videoWidth, int videoHeight);

    void setVideoSampleAspectRatio(int videoSarNum, int videoSarDen);

    void setAspectRatio(int aspectRatio);

    interface IRenderViewHolder {
        SurfaceHolder getSurfaceHolder();

        SurfaceTexture getSurfaceTexture();

        void setOwnSurfaceTexture(boolean isOwn);

        TextureView getTextureView();

        ISurfaceTextureHost getISurfaceTextureHost();
    }
}