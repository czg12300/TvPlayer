package com.jake.simple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jake.library.SurfaceVideoView;
import com.jake.library.widget.TvView;

/**
 * @author Administrator
 * @since 2016/10/31 21:43
 */


public class TvActivity extends Activity {
    private String mUrl;
    private SurfaceVideoView mSurfaceRenderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);
        mSurfaceRenderView = (SurfaceVideoView) findViewById(R.id.video_view);
        mUrl = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(mUrl)) {
            finish();
            return;
        }
        mSurfaceRenderView.setURI(Uri.parse(mUrl.trim()));
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
        mSurfaceRenderView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSurfaceRenderView.release(true);
    }

    public static void start(@NonNull Context context, @NonNull String url) {
        Intent it = new Intent(context, TvActivity.class);
        it.putExtra("url", url);
        context.startActivity(it);
    }
}
