package com.jake.simple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jake.library.widget.SurfaceRenderView;

/**
 * @author Administrator
 * @since 2016/10/31 21:43
 */


public class TvActivity extends Activity {
    private String mUrl;
private SurfaceRenderView mSurfaceRenderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);
        mSurfaceRenderView= (SurfaceRenderView) findViewById(R.id.surface_view);
        mUrl = getIntent().getStringExtra("url");
    }

    public static void start(@NonNull Context context, @NonNull String url) {
        Intent it = new Intent(context, TvActivity.class);
        it.putExtra("url", url);
        context.startActivity(it);
    }
}
