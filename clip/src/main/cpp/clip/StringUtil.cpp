//
// Created by jakechen on 2017/1/9.
//


#include "StringUtil.h"

char *constCharToChar(const char *src) {
    char *result = new char[strlen(src) + 1];
    strcpy(result, src);
    return result;
}

char *intToChar(int src) {
    char *result;
    sprintf(result, "%d", src);
    return result;
}