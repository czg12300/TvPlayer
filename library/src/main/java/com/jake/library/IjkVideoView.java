package com.jake.library;

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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

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
    private int mMaxVolume;
    private int mVolume;
    private float mBrightness;

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
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
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
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
        if (mGestureDetector.onTouchEvent(event))
            return true;

        // 处理手势结束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }

        return super.onTouchEvent(event);
    }


    /**
     * 手势结束
     */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;
        // 隐藏
//        mDismissHandler.removeMessages(0);
//        mDismissHandler.sendEmptyMessageDelayed(0, 500);
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
        public boolean onScroll(MotionEvent oldEv, MotionEvent posEv,
                                float distanceX, float distanceY) {
            if (distanceY > distanceX) {
                float mOldX = oldEv.getX(), mOldY = oldEv.getY();
                int y = (int) posEv.getRawY();
                int windowWidth = getIjkVideoView().getResources().getDisplayMetrics().widthPixels;
                int windowHeight = getIjkVideoView().getResources().getDisplayMetrics().heightPixels;

                if (mOldX > windowWidth / 2)// 右边滑动
                    getIjkVideoView().onVolumeSlide((mOldY - y) / windowHeight);
                else if (mOldX < windowWidth / 2)// 左边滑动
                    getIjkVideoView().onBrightnessSlide((mOldY - y) / windowHeight);
            }
            return super.onScroll(oldEv, posEv, distanceX, distanceY);
        }
    }

    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        Log.d(TAG, "onVolumeSlide percent=" + percent);
        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume) {
            index = mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }
        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
    }

    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        Log.d(TAG, "onBrightnessSlide percent=" + percent);
        if (getContext() != null && getContext() instanceof Activity) {
            Activity activity = (Activity) getContext();
            if (mBrightness < 0) {
                mBrightness = activity.getWindow().getAttributes().screenBrightness;
                if (mBrightness <= 0.00f)
                    mBrightness = 0.50f;
                if (mBrightness < 0.01f)
                    mBrightness = 0.01f;
                // 显示
//            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
//            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
            }
            WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
            lpa.screenBrightness = mBrightness + percent;
            if (lpa.screenBrightness > 1.0f)
                lpa.screenBrightness = 1.0f;
            else if (lpa.screenBrightness < 0.01f)
                lpa.screenBrightness = 0.01f;
            activity.getWindow().setAttributes(lpa);

//        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
//        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
//        mOperationPercent.setLayoutParams(lp);
        }
    }


}
