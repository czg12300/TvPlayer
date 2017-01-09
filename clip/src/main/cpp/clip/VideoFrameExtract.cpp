/*
 * Created by jakechen on 2017/1/5.
*/
#include "VideoFrameExtract.h"

extern "C" {
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include <unistd.h>
#include <libavutil/imgutils.h>
}

void SaveFrame(AVFrame *pFrame, int width, int height, int iFrame, string picPath);
void extractFrame(string filePath,string picPath);
//查找视频流
int findVideoIndex(AVFormatContext* pformat,const  char * filePath);
int findVideoIndex(AVFormatContext* pformat,const char * filePath){
    //打开文件
    if (avformat_open_input(&pformat, filePath, NULL, NULL)) {
        LOGD("avformat_open_input  error!!!\n");
        return -1;
    }
    LOGD("file is open ");
//查找输入文件信息
    if (avformat_find_stream_info(pformat, NULL) < 0) {
        LOGD("avformat_find_stream_info error !!!\n");
        free(pformat);
        return -1;
    }
    LOGD("查找输入文件信息 ");
    int videoindex = -1;
    videoindex = av_find_best_stream(pformat, AVMEDIA_TYPE_VIDEO, -1, -1, NULL, 0);
    LOGD("查找视频流 success");
    if (videoindex == -1) {
        LOGD("can't find video stream in %s\n", pformat->filename);
    }
    return videoindex;
}
int extractFrameFromVideo(string filePath, string picPath) {
    if (filePath.empty()) {
        LOGD("file path is null,exit!");
        return -1;
    }
//注册所有类型
    av_register_all();
    avformat_network_init();
//为pformat结构申请空间 打开文件
    AVFormatContext  *pformat = avformat_alloc_context();
    int videoIndex=findVideoIndex(pformat,filePath.c_str());
//AVFormatContext  ->AVStream ->AVCodecContext
    AVStream *avStream=pformat->streams[videoIndex];
//查找该视频流对应的解码器AVCodec
    AVCodec *pcc = avcodec_find_decoder(avStream->codecpar->codec_id);
    AVCodecContext  *pcodec = avcodec_alloc_context3(pcc);
    LOGD(" find video stream in %s\n", pformat->filename);
//
    if (pcc == NULL) {
        LOGD("cann't find  decoder in %s\n", pformat->filename);
        return -1;
    }
//用该解码器打开该视频文件
    if (avcodec_open2(pcodec, pcc, NULL) < 0) {
        printf("avcodec_open2  error !!!\n");
        return -1;
    }
    LOGD("avcodec_open2  success !!!\n");
//AVFrame  是存储视频数据的结构
//pframe  是从数据包中读出来的数据放在其中
//pframeRGB 是通过 sws_scale转化格式之后所要储存的数据结构
//    AVFrame *pframe, *pframeRGB;
//为pframe 开辟内存空间
    AVFrame  *pframe = av_frame_alloc();
    if (pframe == NULL) {
        printf("pframe  alloc error !!!\n");
        return -1;
    }
//为转化视频格式之后的结构开辟内存空间
    AVFrame *pframeRGB = av_frame_alloc();
    if (pframeRGB == NULL) {
        printf("pframeRGB  alloc error !!!\n");
        return -1;
    }
    LOGD("pframeRGB  alloc success !!!\n");
//申请空间 通过得到ＰＩＸ＿ＦＭＴ＿ＲＧＢ24的一个像素所占用的内存(avpicture_get_size())
    uint8_t *out_buffer = (uint8_t *) av_malloc(
            avpicture_get_size(AV_PIX_FMT_RGB24, pcodec->width, pcodec->height));
//将out_buffer 以pframeRGB的形式关联起来
    avpicture_fill((AVPicture *) pframeRGB, out_buffer, AV_PIX_FMT_RGB24, pcodec->width,
                   pcodec->height);
    struct SwsContext *sws;
//转换上下文
    sws = sws_getContext(pcodec->width, pcodec->height, pcodec->pix_fmt, 320, 240, AV_PIX_FMT_RGB24,
                         SWS_BICUBIC, NULL, NULL, NULL);
    int j = 0;
    AVPacket *ppacket;//cann*t init
    int got_picture;
    LOGD("start read frame");
    while (av_read_frame(pformat, ppacket) >= 0) {
        LOGD("start read frame success");
        if (ppacket->stream_index == videoIndex) {
            int ret = avcodec_decode_video2(pcodec, pframe, &got_picture, ppacket);
            if (ret < 0) {
                LOGD("Decode Error.（解码错误）\n");
                return -1;
            }
            LOGD("Decode success.（解码成功）\n");
            if (got_picture) {
//提取i帧
                LOGD("开始提取i帧\n");
                if (pframe->pict_type == AV_PICTURE_TYPE_I) {
                    sws_scale(sws, (const uint8_t *const *) pframe->data, pframe->linesize, 0,
                              pcodec->height, pframeRGB->data, pframeRGB->linesize);
                    LOGD("AV_PICTURE_TYPE_I\n");
                    SaveFrame(pframeRGB, 320, 240, j, picPath);
                    j++;
                    sleep(1);
                }

//提取p帧
                if (pframe->pict_type == AV_PICTURE_TYPE_P) {
                    sws_scale(sws, (const uint8_t *const *) pframe->data, pframe->linesize, 0,
                              pcodec->height, pframeRGB->data, pframeRGB->linesize);
                    printf("AV_PICTURE_TYPE_P\n");
                    SaveFrame(pframeRGB, 320, 240, j, picPath);
                    j++;
                    sleep(1);

                }
//提取b帧
                if (pframe->pict_type == AV_PICTURE_TYPE_B) {
                    sws_scale(sws, (const uint8_t *const *) pframe->data, pframe->linesize, 0,
                              pcodec->height, pframeRGB->data, pframeRGB->linesize);
                    printf("AV_PICTURE_TYPE_B\n");
                    SaveFrame(pframeRGB, 320, 240, j, picPath);
                    j++;
                    sleep(1);

                }

            }
        }
    }

    av_free_packet(ppacket);

}
void SaveFrame(AVFrame *pFrame, int width, int height, int iFrame, string picPath) {

    LOGD("-------------------\n");
    FILE *pFile;
    char szFilename[32];
    int y;
// Open file
    sprintf(szFilename, (picPath + "/" + "frame%d.ppm").c_str(), iFrame);
    pFile = fopen(szFilename, "wb");
    if (pFile == NULL) {
        return;
    }
    LOGD("P6\n%d %d\n255\n", width, height);
    fprintf(pFile, "P6\n%d %d\n255\n", width, height);
    for (y = 0; y < height; y++) {
        fwrite(pFrame->data[0] + y * pFrame->linesize[0], 1, width * 3, pFile);
    }

    fclose(pFile);
}

