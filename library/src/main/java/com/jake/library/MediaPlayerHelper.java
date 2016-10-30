package com.jake.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;

import tv.danmaku.ijk.media.exo.IjkExoMediaPlayer;
import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.TextureMediaPlayer;

/**
 * description：
 *
 * @author Administrator
 * @since 2016/10/30 11:56
 */


public class MediaPlayerHelper {
    public static final int PV_PLAYER__Auto = 0;
    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;
    public static final int PV_PLAYER__IjkExoMediaPlayer = 3;

    public static IMediaPlayer getAndroidMediaPlayer(boolean isTextureMediaPlayer) {
        IMediaPlayer mediaPlayer = new AndroidMediaPlayer();
        if (isTextureMediaPlayer) {
            mediaPlayer = new TextureMediaPlayer(mediaPlayer);
        }
        return mediaPlayer;
    }

    public static IMediaPlayer getAndroidMediaPlayer() {
        return getAndroidMediaPlayer(false);
    }

    public static IMediaPlayer getIjkExoMediaPlayer(@NonNull Context applicationContext, boolean isTextureMediaPlayer) {
        IMediaPlayer mediaPlayer = new IjkExoMediaPlayer(applicationContext);
        if (isTextureMediaPlayer) {
            mediaPlayer = new TextureMediaPlayer(mediaPlayer);
        }
        return mediaPlayer;
    }

    public static IMediaPlayer getIjkExoMediaPlayer(@NonNull Context applicationContext) {
        return getIjkExoMediaPlayer(applicationContext, false);
    }

    public static IMediaPlayer getIjkMediaPlayer(IjkMediaPlayerBuilder builder, boolean isTextureMediaPlayer) {
        IMediaPlayer mediaPlayer = builder.build();
        if (isTextureMediaPlayer) {
            mediaPlayer = new TextureMediaPlayer(mediaPlayer);
        }
        return mediaPlayer;
    }

    public static IMediaPlayer getIjkMediaPlayer(IjkMediaPlayerBuilder builder) {
        return getIjkMediaPlayer(builder, false);
    }

    public static IjkMediaPlayerBuilder getDefaultIjkMediaPlayerBuilder() {
        return IjkMediaPlayerBuilder.create();
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


}
