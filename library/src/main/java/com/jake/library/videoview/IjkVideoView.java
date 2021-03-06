package com.jake.library.videoview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.jake.library.IRenderView;
import com.jake.library.IVideoView;
import com.jake.library.MediaPlayerBuilder;
import com.jake.library.SurfaceRenderView;
import com.jake.library.TextureRenderView;
import com.jake.library.VideoViewImp;

import java.lang.ref.WeakReference;
import java.util.Map;

import tv.danmaku.ijk.media.player.ISurfaceTextureHost;

/**
 * Created by jakechen on 2016/11/8.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class IjkVideoView extends FrameLayout implements IVideoView {
    public static enum RenderType {
        SURFACE_VIEW, TEXTURE_VIEW;
    }

    private static final String TAG = IjkVideoView.class.getSimpleName();
    private TextureRenderView mTextureRenderView;
    private boolean mOwnSurfaceTexture = false;
    private AudioManager mAudioManager;
    private GestureDetector mGestureDetector;
    private Context mAppContext;
    private VideoViewImp mVideoViewImp;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceTexture mSurfaceTexture;
    private RenderType mRenderType;
    private boolean mEnableSlideSeek = true;

    public void setEnableSlideSeek(boolean enableSlideSeek) {
        this.mEnableSlideSeek = enableSlideSeek;
    }

    public IjkVideoView(Context context) {
        super(context);
        init(context);
    }

    public IjkVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IjkVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mAppContext = context.getApplicationContext();
        mGestureDetector = new GestureDetector(mAppContext, new VolumeAndBrightnessGestureListener(this));
        mAudioManager = (AudioManager) mAppContext.getSystemService(Context.AUDIO_SERVICE);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        setClickable(true);
        setRenderType(RenderType.SURFACE_VIEW);
        if (context instanceof Activity) {
            mBrightness = ((Activity) context).getWindow().getAttributes().screenBrightness;
        }
    }

    public void setRenderType(@NonNull RenderType type) {
        if (type.equals(mRenderType)) {
            return;
        }
        mRenderType = type;
        switch (type) {
            case TEXTURE_VIEW:
                mOwnSurfaceTexture = true;
                mTextureRenderView = new TextureRenderView(getContext());
                mTextureRenderView.setSurfaceTextureListener(mSurfaceTextureListener);
                mVideoViewImp = new VideoViewImp(mTextureRenderView);
                addRenderView(mTextureRenderView);
                break;
            default:
            case SURFACE_VIEW:
                SurfaceRenderView surfaceRenderView = new SurfaceRenderView(getContext());
                surfaceRenderView.getHolder().addCallback(mSurfaceHolderCallback);
                mVideoViewImp = new VideoViewImp(surfaceRenderView);
                addRenderView(surfaceRenderView);
                break;
        }
        addOtherLayout();

    }

    /**
     * 子类在这个地方添加其他布局
     */
    protected void addOtherLayout() {
    }

    private void addRenderView(View view) {
        if (getChildCount() > 0) {
            removeAllViews();
        }

        addView(view, new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
    }

    private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mSurfaceHolder = holder;
            mVideoViewImp.surfaceCreated(mRenderViewHolder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mSurfaceHolder = holder;
            mVideoViewImp.surfaceChanged(mRenderViewHolder, format, width, height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mSurfaceHolder = null;
            mVideoViewImp.surfaceDestroyed(mRenderViewHolder);
        }
    };
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mSurfaceTexture = surface;
            mVideoViewImp.surfaceCreated(mRenderViewHolder);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            mSurfaceTexture = surface;
            mVideoViewImp.surfaceChanged(mRenderViewHolder, 0, width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            mSurfaceTexture = surface;
            mVideoViewImp.surfaceDestroyed(mRenderViewHolder);
            return mOwnSurfaceTexture;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
    private ISurfaceTextureHost mISurfaceTextureHost = new ISurfaceTextureHost() {
        @Override
        public void releaseSurfaceTexture(SurfaceTexture surfaceTexture) {
            if (surfaceTexture == null) {
                Log.d(TAG, "releaseSurfaceTexture: null");
            } else if (mTextureRenderView != null) {
                if (mTextureRenderView.isDidDetachFromWindow()) {
                    if (surfaceTexture != mSurfaceTexture) {
                        Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): release different SurfaceTexture");
                        surfaceTexture.release();
                    } else if (!mOwnSurfaceTexture) {
                        Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): release detached SurfaceTexture");
                        surfaceTexture.release();
                    } else {
                        Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): already released by TextureView");
                    }
                } else if (mTextureRenderView.isWillDetachFromWindow()) {
                    if (surfaceTexture != mSurfaceTexture) {
                        Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): release different SurfaceTexture");
                        surfaceTexture.release();
                    } else if (!mOwnSurfaceTexture) {
                        Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): re-attach SurfaceTexture to TextureView");
                        mRenderViewHolder.setOwnSurfaceTexture(true);
                    } else {
                        Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): will released by TextureView");
                    }
                }
            } else {
                if (surfaceTexture != mSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: alive: release different SurfaceTexture");
                    surfaceTexture.release();
                } else if (!mOwnSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: alive: re-attach SurfaceTexture to TextureView");
                    mRenderViewHolder.setOwnSurfaceTexture(true);
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: alive: will released by TextureView");
                }
            }
        }
    };
    private IRenderView.IRenderViewHolder mRenderViewHolder = new IRenderView.IRenderViewHolder() {
        @Override
        public SurfaceHolder getSurfaceHolder() {
            return mSurfaceHolder;
        }

        @Override
        public SurfaceTexture getSurfaceTexture() {
            return mSurfaceTexture;
        }

        @Override
        public void setOwnSurfaceTexture(boolean isOwn) {
            mOwnSurfaceTexture = isOwn;
        }

        @Override
        public TextureView getTextureView() {
            return mTextureRenderView;
        }

        @Override
        public ISurfaceTextureHost getISurfaceTextureHost() {
            return mISurfaceTextureHost;
        }
    };

    @Override
    public void setURI(Uri uri, Map<String, String> headers) {
        mVideoViewImp.setURI(uri, headers);
    }

    @Override
    public void setURI(Uri uri) {
        mVideoViewImp.setURI(uri);
    }

    @Override
    public void setMediaPlayerBuilder(MediaPlayerBuilder builder) {
        mVideoViewImp.setMediaPlayerBuilder(builder);
    }

    @Override
    public void release(boolean clearTargetState) {
        mVideoViewImp.release(clearTargetState);
    }

    @Override
    public void setAspectRatio(int aspectRatio) {
        mVideoViewImp.setAspectRatio(aspectRatio);
    }

    @Override
    public void stop() {
        mVideoViewImp.stop();
    }

    @Override
    public void start() {
        mVideoViewImp.start();
    }

    @Override
    public void pause() {
        mVideoViewImp.pause();
    }

    @Override
    public int getDuration() {
        return mVideoViewImp.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mVideoViewImp.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mVideoViewImp.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mVideoViewImp.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return mVideoViewImp.getBufferPercentage();
    }

    @Override
    public boolean canPause() {
        return mVideoViewImp.canPause();
    }

    @Override
    public boolean canSeekBackward() {
        return mVideoViewImp.canSeekBackward();
    }

    @Override
    public boolean canSeekForward() {
        return mVideoViewImp.canSeekForward();
    }

    @Override
    public int getAudioSessionId() {
        return mVideoViewImp.getAudioSessionId();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                restVolumeAndBrightness();
                break;
        }
        return super.onTouchEvent(event);
    }

    private int mMaxVolume;
    private int mVolume = -1;
    private float mBrightness = -1f;
    private int mOldCurrentPosition = -1;
    private int mCurrentPosition = -1;
    private int mPositionSlide = SLIDE_DEFAULT;
    private static final int SLIDE_DEFAULT = 0;
    private static final int SLIDE_VOLUME = 2;
    private static final int SLIDE_BRIGHTNESS = 1;
    private static final int SLIDE_SEEK = 3;

    /**
     * 手势结束
     */
    private void restVolumeAndBrightness() {
        if (mVolume != -1) {
            dismissVolumeView();
        }
        if (mBrightness != -1) {
            dismissBrightnessView();
        }
        if (mOldCurrentPosition != mCurrentPosition && mCurrentPosition != -1) {
            seekTo(mCurrentPosition);
            start();
        }
        if (mOldCurrentPosition != -1) {
            dismissSeekView();
        }
        mVolume = -1;
        mBrightness = -1f;
        mOldCurrentPosition = -1;
        mCurrentPosition = -1;
        mPositionSlide = SLIDE_DEFAULT;
    }

    private int getPositionSlide() {
        return mPositionSlide;
    }

    private static class VolumeAndBrightnessGestureListener extends GestureDetector.SimpleOnGestureListener {
        private WeakReference<IjkVideoView> mIjkVideoView;

        public VolumeAndBrightnessGestureListener(@NonNull IjkVideoView ijkVideoView) {
            mIjkVideoView = new WeakReference<IjkVideoView>(ijkVideoView);
        }

        private IjkVideoView getIjkVideoView() {
            return mIjkVideoView.get();
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            getIjkVideoView().onSingleTapUp(e);
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent oldEv, MotionEvent posEv,
                                float distanceX, float distanceY) {
            float countX = Math.abs(oldEv.getX() - posEv.getX());
            float countY = Math.abs(oldEv.getY() - posEv.getY());
            int windowWidth = getIjkVideoView().getResources().getDisplayMetrics().widthPixels;
            int positionSlide = getIjkVideoView().getPositionSlide();
            if (countY > countX) {
                int windowHeight = getIjkVideoView().getResources().getDisplayMetrics().heightPixels;
                float mOldX = oldEv.getX();
                float present = (oldEv.getY() - posEv.getY()) / windowHeight;
                if (mOldX > windowWidth / 2) {// 右边滑动
                    if (positionSlide == SLIDE_DEFAULT || positionSlide == SLIDE_VOLUME) {
                        getIjkVideoView().onVolumeSlide(present);
                    }
                } else if (mOldX < windowWidth / 2)// 左边滑动
                    if (positionSlide == SLIDE_DEFAULT || positionSlide == SLIDE_BRIGHTNESS) {
                        getIjkVideoView().onBrightnessSlide(present);
                    }
            } else {
                float present = (posEv.getX() - oldEv.getX()) / windowWidth;
                if (getIjkVideoView().isEnableSlideSeek() && (positionSlide == SLIDE_DEFAULT || positionSlide == SLIDE_SEEK)) {
                    getIjkVideoView().onSeekSlide(present);
                }
            }
            return super.onScroll(oldEv, posEv, distanceX, distanceY);
        }
    }

    private boolean isEnableSlideSeek() {
        return mEnableSlideSeek;
    }

    private void onSeekSlide(float percent) {
        int duration = getDuration();
        if (mOldCurrentPosition == -1) {
            mPositionSlide = SLIDE_SEEK;
            mOldCurrentPosition = getCurrentPosition();
            mCurrentPosition = getCurrentPosition();
            showSeekView(percent > 0, mCurrentPosition, mOldCurrentPosition, duration);
        }
        if (mPositionSlide != SLIDE_SEEK) {
            return;
        }
        mCurrentPosition = mOldCurrentPosition + (int) (duration * percent);
        if (mCurrentPosition < 0) {
            mCurrentPosition = 0;
        } else if (mCurrentPosition > duration) {
            mCurrentPosition = duration;
        }
        showSeekView(percent > 0, mCurrentPosition, mOldCurrentPosition, duration);
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mPositionSlide = SLIDE_VOLUME;
            mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0) {
                mVolume = 0;
            }
            showVolumeView((float) mVolume / (float) mMaxVolume);
        }
        if (mPositionSlide != SLIDE_VOLUME) {
            return;
        }
        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume) {
            index = mMaxVolume;
        } else if (mVolume < 0) {
            index = 0;
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        changeVolumeProgress((float) index / (float) mMaxVolume);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (getContext() != null && getContext() instanceof Activity) {
            Activity activity = (Activity) getContext();
            if (mBrightness == -1) {
                mPositionSlide = SLIDE_BRIGHTNESS;
                mBrightness = activity.getWindow().getAttributes().screenBrightness;
                if (mBrightness < 0.01f) {
                    if (mBrightness <= 0.00f) {
                        mBrightness = 0.50f;
                    } else {
                        mBrightness = 0.01f;
                    }
                }
                showBrightnessView(mBrightness);
            }
            if (mPositionSlide != SLIDE_BRIGHTNESS) {
                return;
            }
            WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
            lpa.screenBrightness = mBrightness + percent;
            if (lpa.screenBrightness > 1.0f) {
                lpa.screenBrightness = 1.0f;
            } else if (lpa.screenBrightness < 0.01f) {
                lpa.screenBrightness = 0.01f;
            }
            activity.getWindow().setAttributes(lpa);
            changeBrightnessProgress(lpa.screenBrightness);
        }
    }

    /**
     * 显示音量调节的ui
     */
    protected void showVolumeView(float percent) {

    }

    /**
     * 改变音量大小
     */
    protected void changeVolumeProgress(float percent) {

    }

    /**
     * 消失音量调节的ui
     */
    protected void dismissVolumeView() {

    }

    /**
     * 显示亮度调节的ui
     */
    protected void showBrightnessView(float progress) {

    }

    /**
     * 改变亮度大小
     */
    protected void changeBrightnessProgress(float progress) {

    }

    /**
     * 消失亮度调节的ui
     */
    protected void dismissBrightnessView() {

    }

    /**
     * 显示播放进度调节的ui
     */
    protected void showSeekView(boolean isForward, int currentPosition, int oldCurrentPosition, int duration) {

    }

    /**
     * 改变播放进度大小
     */
    protected void changeSeekProgress(boolean isForward, int currentPosition, int oldCurrentPosition, int duration) {

    }

    /**
     * 消失播放进度调节的ui
     */
    protected void dismissSeekView() {

    }

    /**
     * 单次点击
     *
     * @param ev
     */
    protected void onSingleTapUp(MotionEvent ev) {
    }

}
