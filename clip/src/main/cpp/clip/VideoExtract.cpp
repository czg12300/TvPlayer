extern "C" {
#include <libavcodec/avcodec.h>
#include <libavdevice/avdevice.h>
#include <libavfilter/avfilter.h>
#include <libswscale/swscale.h>
}


#include <string>
#include <libavutil/imgutils.h>
#include "Log.h"
#include "StringUtil.h"

using namespace std;

int save_frame(AVPacket packet, int width, int height, int iFrame, string picPath);

int save_frame_as_jpeg(AVCodecContext *pCodecCtx, AVFrame *pFrame, int FrameNo, string picPath);

int MyWriteJPEG(AVFrame *pFrame, int width, int height, int iIndex, string picPath);

void generate_file_name(char *file_name, char *file_path, long long pts);

void save_picture_uinit(FILE *pFile, AVPacket pkt);

int isDebug = 0;

AVFormatContext *pFormatCtx; //这个结构体描述了一个媒体文件或媒体流的构成和基本信息
AVCodec *pCodec;

int extractVideo(string filePath, string picPath) {
    LOGI("version = %d", avcodec_version());
    int ret;
    int err;
    av_register_all();
    if (avformat_open_input(&pFormatCtx, filePath.c_str(), NULL, NULL)) {
        LOGD("avformat_open_input  error!!!\n");
        return -1;
    }
//查找输入文件信息
    if (avformat_find_stream_info(pFormatCtx, NULL) < 0) {
        LOGD("avformat_find_stream_info error !!!\n");
        free(pFormatCtx);
        return -1;
    }
    int videoindex = -1;
    videoindex = av_find_best_stream(pFormatCtx, AVMEDIA_TYPE_VIDEO, -1, -1, NULL, 0);
    if (videoindex == -1) {
        LOGD("can't find video stream in %s\n", pFormatCtx->filename);
        return -1;
    }
    LOGE("find codec!");
    AVCodecContext *pCodecCtx;
    avcodec_parameters_to_context(pCodecCtx, pFormatCtx->streams[videoindex]->codecpar);
    //根据编解码信息查找解码器
    pCodec = avcodec_find_decoder(pCodecCtx->codec_id);
    if (pCodec == NULL) {
        LOGE("Could not found codec!");
        return -1;
    }
//打开解码器
    if (avcodec_open2(pCodecCtx, pCodec, NULL) < 0) {
        LOGE("Could not open codec!");
        return -1;
    }
    LOGE("codec the video ok!");
//如果参数为1，会报  Fatal signal 11 错误
    av_dump_format(pFormatCtx, 0, filePath.c_str(), 0);

    AVFrame *pFrame, *pFrameRGB;
    struct SwsContext *pSwsCtx;
    AVPacket packet;
    int pictureSize;
    int frameFinished;
    uint8_t *buf;
    int videoWidth = pCodecCtx->width;
    int videoHeight = pCodecCtx->height;
    pFrame = av_frame_alloc(); //为该帧图像分配内存 avcodec_alloc_frame 废弃
    pFrameRGB = av_frame_alloc(); //为该帧图像分配内存
    pictureSize = av_image_get_buffer_size(AV_PIX_FMT_BGR24, videoWidth, videoHeight, 1);
    buf = (uint8_t *) av_malloc(pictureSize);
    LOGE("pictureSize: %d\n", pictureSize);

//已经分配的空间的结构体AVPicture挂上一段用于保存数据的空间
    av_image_fill_arrays(pFrameRGB->data, pFrameRGB->linesize, buf, AV_PIX_FMT_BGR24, videoWidth,
                         videoHeight, 1);

//设置图像转换上下文
    pSwsCtx = sws_getContext(videoWidth, videoHeight, pCodecCtx->pix_fmt, videoWidth, videoHeight,
                             AV_PIX_FMT_BGR24, SWS_BICUBIC, NULL, NULL, NULL);
//av_read_frame,读取码流中的音频若干帧或者视频一帧
    int i = 0;
    while (av_read_frame(pFormatCtx, &packet) >= 0) {
        if (packet.stream_index == videoindex) {
//的作用是解码一帧视频数据。输入一个压缩编码的结构体AVPacket，输出一个解码后的结构体AVFrame。该函数的声明位于libavcodec\avcodec.h
            ret = avcodec_send_packet(pCodecCtx, &packet);
            if (ret < 0 && ret != AVERROR(EAGAIN) && ret != AVERROR_EOF) {
                return -1;
            }
            //从解码器返回解码输出数据
            ret = avcodec_receive_frame(pCodecCtx, pFrame);
            if (ret < 0 && ret != AVERROR_EOF) {
                return -1;
            }
//            int tempRet = avcodec_decode_video2(pCodecCtx, pFrame, &frameFinished, &packet);
//为0说明没有找到可以解压的帧
            if (frameFinished != 0) {
                LOGD("开始提取i帧");
                if (pFrame->pict_type == AV_PICTURE_TYPE_I) {
                    sws_scale(pSwsCtx, (const uint8_t *const *) pFrame->data,
                              pFrame->linesize, 0, videoHeight, pFrameRGB->data,
                              pFrameRGB->linesize);
                    LOGD("AV_PICTURE_TYPE_I");
//将yuv格式转换为RGB32
//                    sws_scale(pSwsCtx, (const uint8_t *const *) pFrame->data,
//                              pFrame->linesize, 0, pCodecCtx->height, pFrameRGB->data,
//                              pFrameRGB->linesize);
//保存该帧图片为PPM格式
//                    save_frame(pFrameRGB, pCodecCtx->width, pCodecCtx->height, i, picPath);
                    int result = save_frame_as_jpeg(pCodecCtx, pFrameRGB, i, picPath);
                    i++;
//                    int result= MyWriteJPEG( pFrame,pCodecCtx->width ,pCodecCtx->height,i,  picPath);
                    if (result == 0) {
                        LOGD("写入jpg成功");
                    } else {
                        LOGD("写入jpg失败");
                    }
                }
            } else {
                LOGD("skip 开始提取i帧");
            }
        }
        av_packet_unref(&packet);
    }
    LOGE("decode finished!");
    sws_freeContext(pSwsCtx);
    av_free(pFrame);
    av_free(pFrameRGB);
    avcodec_close(pCodecCtx);
    avformat_close_input(&pFormatCtx);
    return 1;
}

int save_frame(AVPacket pkt, int width, int height, int iFrame, string savePath) {
    char *filName = intToChar(iFrame);
    generate_file_name(filName, constCharToChar(savePath.c_str()), pkt.pts);
    FILE *video_dst_filetemp = fopen(filName, "wb");
    if (!video_dst_filetemp) {
        fprintf(stderr, "Could not open destination file %s\n", video_dst_filetemp);
    }
    if (video_dst_filetemp) {
        save_picture_uinit(video_dst_filetemp, pkt);
    }

    return 0;
}

void save_picture_uinit(FILE *pFile, AVPacket pkt) {
    fwrite(pkt.data, sizeof(uint8_t), pkt.size, pFile);
    fclose(pFile);
    pFile = NULL;
    av_packet_unref(&pkt);
}

void generate_file_name(char *file_name, char *file_path, long long pts) {
    if (file_path[strlen(file_path) - 1] == '\\') {
        sprintf(file_name, "%s%lld.jpg", file_path, pts);
    } else {
        sprintf(file_name, "%s\\%lld.jpg", file_path, pts);
    }
}

int save_frame_as_jpeg(AVCodecContext *pCodecCtx, AVFrame *pFrame, int FrameNo, string picPath) {
    AVCodec *jpegCodec = avcodec_find_encoder(AV_CODEC_ID_JPEG2000);
    if (!jpegCodec) {
        return -1;
    }

    AVCodecContext *jpegContext = avcodec_alloc_context3(jpegCodec);
    if (!jpegContext) {
        LOGD("jpegContext is null");
        return -1;
    }
    LOGD("jpegContext is not null");
    jpegContext->pix_fmt = pCodecCtx->pix_fmt;
    jpegContext->height = pFrame->height;
    jpegContext->width = pFrame->width;

    if (avcodec_open2(jpegContext, jpegCodec, NULL) != 0) {
        LOGD("avcodec_open2 fail");
        return -1;
    }
    FILE *JPEGFile;
    char JPEGFName[256];

    AVPacket packet = {.data = NULL, .size = 0};
    LOGD("av_init_packet");
    av_init_packet(&packet);
    int gotFrame;
    int ret = avcodec_send_packet(pCodecCtx, &packet);
    if (ret < 0 && ret != AVERROR(EAGAIN) && ret != AVERROR_EOF) {
        return -1;
    }
    //从解码器返回解码输出数据
    ret = avcodec_receive_frame(pCodecCtx, pFrame);
    if (ret < 0 && ret != AVERROR_EOF) {
        return -1;
    }
    string fjName = picPath + "dvr-%06d.jpg";
    LOGD("start create file");
    sprintf(JPEGFName, fjName.c_str(), FrameNo);
    JPEGFile = fopen(JPEGFName, "wb");
    LOGD("start write file");
    fwrite(packet.data, 1, packet.size, JPEGFile);
    fclose(JPEGFile);
    LOGD("finish write file");
    av_free_packet(&packet);
    avcodec_close(jpegContext);
    return 0;
}

int MyWriteJPEG(AVFrame *pFrame, int width, int height, int iIndex, string picPath) {
    // 输出文件路径
    char out_file[256] = {0};
    sprintf(out_file, (const char *) sizeof(out_file), "%s%d.jpg", (picPath + "/").c_str(), iIndex);

    // 分配AVFormatContext对象
    AVFormatContext *pFormatCtx = avformat_alloc_context();

    // 设置输出文件格式
    pFormatCtx->oformat = av_guess_format("mjpeg", NULL, NULL);
    // 创建并初始化一个和该url相关的AVIOContext
    if (avio_open(&pFormatCtx->pb, out_file, AVIO_FLAG_READ_WRITE) < 0) {
        LOGD("Couldn't open output file.");
        return -1;
    }

    // 构建一个新stream
    AVStream *pAVStream = avformat_new_stream(pFormatCtx, 0);
    if (pAVStream == NULL) {
        return -1;
    }

    // 设置该stream的信息
    AVCodecContext *pCodecCtx = pAVStream->codec;

    pCodecCtx->codec_id = pFormatCtx->oformat->video_codec;
    pCodecCtx->codec_type = AVMEDIA_TYPE_VIDEO;
    pCodecCtx->pix_fmt = AV_PIX_FMT_YUVJ420P;
    pCodecCtx->width = width;
    pCodecCtx->height = height;
    pCodecCtx->time_base.num = 1;
    pCodecCtx->time_base.den = 25;

    // Begin Output some information
    av_dump_format(pFormatCtx, 0, out_file, 1);
    // End Output some information

    // 查找解码器
    AVCodec *pCodec = avcodec_find_encoder(pCodecCtx->codec_id);
    if (!pCodec) {
        LOGD("Codec not found.");
        return -1;
    }
    // 设置pCodecCtx的解码器为pCodec
    if (avcodec_open2(pCodecCtx, pCodec, NULL) < 0) {
        LOGD("Could not open codec.");
        return -1;
    }

    //Write Header
    avformat_write_header(pFormatCtx, NULL);

    int y_size = pCodecCtx->width * pCodecCtx->height;

    //Encode
    // 给AVPacket分配足够大的空间
    AVPacket pkt;
    av_new_packet(&pkt, y_size * 3);

    //
    int got_picture = 0;
    int ret = avcodec_encode_video2(pCodecCtx, &pkt, pFrame, &got_picture);
    if (ret < 0) {
        LOGD("Encode Error.\n");
        return -1;
    }
    if (got_picture == 1) {
        //pkt.stream_index = pAVStream->index;
        ret = av_write_frame(pFormatCtx, &pkt);
    }

    av_free_packet(&pkt);

    //Write Trailer
    av_write_trailer(pFormatCtx);

    LOGD("Encode Successful.\n");

    if (pAVStream) {
        avcodec_close(pAVStream->codec);
    }
    avio_close(pFormatCtx->pb);
    avformat_free_context(pFormatCtx);

    return 0;
}