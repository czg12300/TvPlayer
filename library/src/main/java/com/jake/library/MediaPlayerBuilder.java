package com.jake.library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import tv.danmaku.ijk.media.exo.IjkExoMediaPlayer;
import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.TextureMediaPlayer;

/**
 * Created by jakechen on 2016/11/1.
 */

public final class MediaPlayerBuilder {
    public static final int TYPE_IJK = 1;
    public static final int TYPE_IJK_EXO = 2;
    public static final int TYPE_ANDROID = 3;

    private int mType;
    private boolean isTextureMediaPlayer = false;
    private Context mAppContext;
    private IjkMediaPlayerBuilder mIjkMediaPlayerBuilder;

    private MediaPlayerBuilder(Context context, int type) {
        mType = type;
        mAppContext = context.getApplicationContext();
    }

    public static MediaPlayerBuilder create(@NonNull Context context, int type) {
        return new MediaPlayerBuilder(context, type);
    }

    public MediaPlayerBuilder setTextureMediaPlayer(boolean textureMediaPlayer) {
        isTextureMediaPlayer = textureMediaPlayer;
        return this;
    }

    public MediaPlayerBuilder setIjkMediaPlayerBuilder(IjkMediaPlayerBuilder builder) {
        mIjkMediaPlayerBuilder = builder;
        return this;
    }

    public IMediaPlayer build() {
        IMediaPlayer mediaPlayer = null;
        switch (mType) {
            case TYPE_ANDROID:
                mediaPlayer = new AndroidMediaPlayer();
                break;
            case TYPE_IJK_EXO:
            default:
                mediaPlayer = new IjkExoMediaPlayer(mAppContext);
                break;
            case TYPE_IJK:
                if (mIjkMediaPlayerBuilder == null) {
                    mIjkMediaPlayerBuilder = IjkMediaPlayerBuilder.create();
                }
                mediaPlayer = mIjkMediaPlayerBuilder.build();
                break;
        }
        if (isTextureMediaPlayer) {
            mediaPlayer = new TextureMediaPlayer(mediaPlayer);
        }
        return mediaPlayer;

    }

    public static class IjkMediaPlayerBuilder {
        private IjkMediaPlayer mMediaPlayer;
        private boolean isCallSetMediaCodec = false;
        private boolean isCallSetUsingOpenSLES = false;
        private boolean isSetPixelFormat = false;

        private IjkMediaPlayerBuilder() {
            mMediaPlayer = new IjkMediaPlayer();
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        }

        public static IjkMediaPlayerBuilder create() {
            return new IjkMediaPlayerBuilder();
        }

        public IjkMediaPlayerBuilder setMediaCodec(boolean usingMediaCodecAutoRotate, boolean mediaCodecHandleResolutionChange) {
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
            if (usingMediaCodecAutoRotate) {
                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
            } else {
                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);
            }
            if (mediaCodecHandleResolutionChange) {
                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
            } else {
                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 0);
            }
            isCallSetMediaCodec = true;
            return this;
        }

        public IjkMediaPlayerBuilder setUsingOpenSLES() {
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
            isCallSetUsingOpenSLES = true;
            return this;
        }

        public IjkMediaPlayerBuilder setPixelFormat(String pixelFormat) {
            if (TextUtils.isEmpty(pixelFormat)) {
                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
            } else {
                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", pixelFormat);
            }
            isSetPixelFormat = true;
            return this;
        }

        public IjkMediaPlayer build() {
            if (!isCallSetMediaCodec) {
                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
            }
            if (!isCallSetUsingOpenSLES) {
                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
            }
            if (!isSetPixelFormat) {
                mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
            }
            return mMediaPlayer;
        }
    }
}
