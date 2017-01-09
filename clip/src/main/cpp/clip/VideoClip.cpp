//
// Created by jakechen on 2016/12/30.
//

#include "VideoClip.h"
#include "VideoFrameExtract.h"
#include "VideoExtract.h"

JNI(jstring, sayHello)(JNIEnv *env, jclass type, jstring name_) {
    string name = env->GetStringUTFChars(name_, NULL);
    string jni = name + ",this is come from jni";
    env->ReleaseStringUTFChars(name_, name.c_str());
    return env->NewStringUTF(jni.c_str());
}

JNI(void, helloJni)(JNIEnv *env, jclass type, jstring name_) {
    const string name = env->GetStringUTFChars(name_, 0);
    LOGD("this is jni log");

    env->ReleaseStringUTFChars(name_, name.c_str());
}


JNI(jstring, videoToPicture)(JNIEnv *env, jclass type, jstring filePath_, jstring picPath_) {
    const char *filePath = env->GetStringUTFChars(filePath_, 0);
    const char *picPath = env->GetStringUTFChars(picPath_, 0);
    int result = extractVideo(filePath, picPath);
    env->ReleaseStringUTFChars(filePath_, filePath);
    string resultStr = result > 0 ? "frame extract success" : "frame extract fail";
    return env->NewStringUTF(resultStr.c_str());
}