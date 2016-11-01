package com.jake.simple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jake.library.widget.SurfaceRenderView;
import com.jake.library.widget.TvView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author Administrator
 * @since 2016/10/31 21:43
 */


public class TvActivity extends Activity {
    private String mUrl;
    private TvView mSurfaceRenderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);
        mSurfaceRenderView = (TvView) findViewById(R.id.tv_view);
        mUrl = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(mUrl)) {
            finish();
            return;
        }
        mSurfaceRenderView.setUri(Uri.parse(mUrl.trim()));
        mSurfaceRenderView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mSurfaceRenderView.getMediaController().start();


    }

    @Override
    protected void onPause() {
        super.onPause();
//        mSurfaceRenderView.getMediaController().release(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSurfaceRenderView.getMediaController().release(true);
    }

    public static void start(@NonNull Context context, @NonNull String url) {
        Intent it = new Intent(context, TvActivity.class);
        it.putExtra("url", url);
        context.startActivity(it);
    }
}
