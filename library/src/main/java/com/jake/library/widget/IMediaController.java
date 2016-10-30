package com.jake.library.widget;

import android.net.Uri;
import android.view.View;
import android.widget.MediaController;

import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * description：
 *
 * @author Administrator
 * @since 2016/10/29 23:09
 */


public interface IMediaController extends MediaController.MediaPlayerControl {
    /**
     * get a  video view
     *
     * @return return a video view
     */
    View getView();

    /**
     * Sets video URI
     *
     * @param uri the URI of the video.
     */
    void setURI(Uri uri);

    /**
     * Sets video URI using specific headers.
     *
     * @param uri     the URI of the video.
     * @param headers the headers for the URI request.
     *                Note that the cross domain redirection is allowed by default, but that can be
     *                changed with key/value pairs through the headers parameter with
     *                "android-allow-cross-domain-redirect" as the key and "0" or "1" as the value
     *                to disallow or allow cross domain redirection.
     */
    void setURI(Uri uri, Map<String, String> headers);

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l The callback that will be run
     */
    void setOnPreparedListener(IMediaPlayer.OnPreparedListener l);

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */
    void setOnCompletionListener(IMediaPlayer.OnCompletionListener l);

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */
    void setOnErrorListener(IMediaPlayer.OnErrorListener l);

    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param l The callback that will be run
     */
    void setOnInfoListener(IMediaPlayer.OnInfoListener l);

    /**
     * release the media player in any state
     */
    void release(boolean cleartargetstate);

}
