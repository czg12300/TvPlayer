package com.jake.clip;

/**
 *jni of video clip
 * @author jake
 * @since 2016/12/30 下午5:18
 */

public class VideoClipJni {
    static {
        System.loadLibrary("native-lib");
    }
    public static native String videoToPicture(String filePath,String picPathS);
    public static native String sayHello(String name);
    public static native void helloJni(String name);
}
