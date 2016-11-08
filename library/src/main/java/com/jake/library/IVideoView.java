package com.jake.library;

import android.net.Uri;
import android.widget.MediaController;

import com.jake.library.MediaPlayerBuilder;

import java.util.Map;

/**
 * Created by jakechen on 2016/11/8.
 */

public interface IVideoView extends MediaController.MediaPlayerControl {
    void setURI(Uri uri, Map<String, String> headers);

    void setURI(Uri uri);

    void setMediaPlayerBuilder(MediaPlayerBuilder builder);

    void release(boolean clearTargetState);

    void stop();
}
