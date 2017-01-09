cmake_minimum_required(VERSION 3.4.1)

#视频剪辑代码位置
set(src_clip_DIR src/main/cpp/clip)
#ffmpeg的库文件所在位置
set(ffmpeg_lib_DIR ../../../../src/main/jniLibs/armeabi)

#include目录，也就是头文件目录
include_directories(
        src/main/jniLibs/include
        src/main/cpp/clip/include
)

#导入so文件
add_library(avcodec SHARED IMPORTED)
set_target_properties( avcodec
                       PROPERTIES IMPORTED_LOCATION
                       ${ffmpeg_lib_DIR}/libavcodec-57.so )

add_library(avfilter SHARED IMPORTED)
set_target_properties( avfilter
                       PROPERTIES IMPORTED_LOCATION
                       ${ffmpeg_lib_DIR}/libavfilter-6.so )

add_library(avformat SHARED IMPORTED)
set_target_properties( avformat
                       PROPERTIES IMPORTED_LOCATION
                       ${ffmpeg_lib_DIR}/libavformat-57.so )

add_library(avutil SHARED IMPORTED)
set_target_properties( avutil
                       PROPERTIES IMPORTED_LOCATION
                       ${ffmpeg_lib_DIR}/libavutil-55.so )

add_library(swresample SHARED IMPORTED)
set_target_properties( swresample
                       PROPERTIES IMPORTED_LOCATION
                       ${ffmpeg_lib_DIR}/libswresample-2.so )

add_library(swscale SHARED IMPORTED)
set_target_properties( swscale
                       PROPERTIES IMPORTED_LOCATION
                       ${ffmpeg_lib_DIR}/libswscale-4.so )

add_library(avdevice SHARED IMPORTED)
set_target_properties( avdevice
                       PROPERTIES IMPORTED_LOCATION
                       ${ffmpeg_lib_DIR}/libavdevice-57.so )
file(GLOB native_srcs src/main/cpp/clip/*.cpp)
add_library(native-lib SHARED
            ${native_srcs})

#支持-std=gnu++11
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

#判断编译器类型,如果是gcc编译器,则在编译选项中加入c++11支持
if(CMAKE_COMPILER_IS_GNUCXX)
    set(CMAKE_CXX_FLAGS "-std=c++11 ${CMAKE_CXX_FLAGS}")
    message(STATUS "optional:-std=c++11")
endif(CMAKE_COMPILER_IS_GNUCXX)

find_library(ndk-lib log)

target_link_libraries(native-lib
                      avcodec
                      avfilter
                      avformat
                      avutil
                      swresample
                      swscale
                      avdevice
					 ${ndk-lib}
					  )
