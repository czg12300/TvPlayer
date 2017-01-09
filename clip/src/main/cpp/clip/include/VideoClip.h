//
// Created by jakechen on 2016/12/30.
//


#include <jni.h>
#include <string>
#include <iostream>
#include "Log.h"

using namespace std;

extern "C"
{
#define JNI(rettype, name) JNIEXPORT rettype JNICALL Java_com_jake_clip_VideoClipJni_##name
JNI(jstring, videoToPicture)(JNIEnv *env, jclass type, jstring filePath_, jstring picPath);
JNI(jstring, sayHello)(JNIEnv *env, jclass type, jstring name_);
JNI(void, helloJni)(JNIEnv *env, jclass type, jstring name_);
}