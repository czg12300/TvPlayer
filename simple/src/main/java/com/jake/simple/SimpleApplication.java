package com.jake.simple;

import android.app.Application;
import android.content.Context;

//import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by jakechen on 2016/11/1.
 */

public class SimpleApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // init player
//        IjkMediaPlayer.loadLibrariesOnce(null);
//        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }
}
