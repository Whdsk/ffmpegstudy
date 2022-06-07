#include <string.h>
#include <android/bitmap.h>
#include <jni.h>
#include <stdio.h>
#include <setjmp.h>
#include <math.h>
#include <stdint.h>
#include <time.h>
#include <string>
#include <android/bitmap.h>
#include <android/log.h>
#include <atomic>
extern "C" JNIEXPORT jstring JNICALL
Java_com_alibaba_ailab_ffmpegso_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavfilter/avfilter.h>
#include <libavcodec/jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_alibaba_ailab_ffmpegso_MainActivity_ffmpegInfo(JNIEnv *env, jobject  /* this */) {

//    char info[40000] = {0};
//    AVCodec *c_temp = av_codec_next(NULL);
//    while (c_temp != NULL) {
//        if (c_temp->decode != NULL) {
//            sprintf(info, "%sdecode:", info);
//        } else {
//            sprintf(info, "%sencode:", info);
//        }
//        switch (c_temp->type) {
//            case AVMEDIA_TYPE_VIDEO:
//                sprintf(info, "%s(video):", info);
//                break;
//            case AVMEDIA_TYPE_AUDIO:
//                sprintf(info, "%s(audio):", info);
//                break;
//            default:
//                sprintf(info, "%s(other):", info);
//                break;
//        }
//        sprintf(info, "%s[%s]\n", info, c_temp->name);
//        c_temp = c_temp->next;
//    }

    //return env->NewStringUTF(info);
//    const AVCodec *current_codec;
//    void *i = nullptr;
//    while ((current_codec = av_codec_iterate(&i))) {
//        if (av_codec_is_encoder(current_codec)) {
//            //std::cout << "Found encoder " << current_codec->long_name << std::endl;
//            sprintf(info, "%s[%s]\n", info, current_codec->long_name);
//        }
//
//    }
    //获取avutil数字版本号
//    int version = avutil_version();
//    //获取avutil三个子版本号
//    int a = version / (int) pow(2, 16);
//    int b = (int) (version - a * pow(2, 16)) / (int) pow(2, 8);
//    int c = version % (int) pow(2, 8);
//    //拼接avutil完整版本号
//   // char *charArray = new char[1024];
//    std::string version1 =&"版本%d---"[a];
//    std::string version2 =&"版本%d---"[b];
//    std::string version3 =&"版本%d---"[c];
//
//    std::string aaa=version1+version2+version3;
//    return env->NewStringUTF(aaa.c_str());
//    return env->NewStringUTF(avcodec_configuration());
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
    //std::stringstream() << a << "." << b << "." << c << '\0' >> charArray;
    //std::cout << "MeidaPlayer ffmpeg/avutil version "  << std::endl;
//    std::cout <<"MeidaPlayer ffmpeg/avutil version"<<std::endl;
//    ————————————————
//    版权声明：本文为CSDN博主「命运之手」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//    原文链接：https://blog.csdn.net/u013718730/article/details/118518967


//    return env->NewStringUTF(aaa.c_str());
}


//视频转码压缩主函数入口
//SDL（main）
//ffmpeg_mod.c有一个FFmpeg视频转码主函数入口
//标记（声明有一个这样的函数提供给我调用）
//参数含义分析
//首先分析：String str = "ffmpeg -i input.mov -b:v 640k output.mp4"
// argc = str.split(" ").length()
// argv = str.split(" ")  字符串数组
//参数一：命令行字符串命令个数
//参数二：命令行字符串数组

//ffmpeg主函数入口
//int ffmpegmain(int argc, char **argv) {
//    int ret;
//    int64_t ti;
//    //av_log_set_callback(av_log_callback);
//    register_exit(ffmpeg_cleanup);
//
//    setvbuf(stderr, NULL, _IONBF, 0); /* win32 runtime needs this */
//
//    av_log_set_flags(AV_LOG_SKIP_REPEATED);
//    parse_loglevel(argc, argv, options);
//
//    if (argc > 1 && !strcmp(argv[1], "-d")) {
//        run_as_daemon = 1;
//        av_log_set_callback(log_callback_null);
//        argc--;
//        argv++;
//    }
//
//    avcodec_register_all();
//#if CONFIG_AVDEVICE
//    avdevice_register_all();
//#endif
//    avfilter_register_all();
//    av_register_all();
//    avformat_network_init();
//
//    show_banner(argc, argv, options);
//
//    term_init();
//
//    /* parse options and open all input/output files */
//    ret = ffmpeg_parse_options(argc, argv);
//    if (ret < 0) {
//        ffmpeg_cleanup(1);
//        return 1;
//    }
//
//    if (nb_output_files <= 0 && nb_input_files == 0) {
//        show_usage();
//        av_log(NULL, AV_LOG_WARNING, "Use -h to get full help or, even better, run 'man %s'\n",
//               program_name);
//        ffmpeg_cleanup(1);
//        return 1;
//    }
//
//    /* file converter / grab */
//    if (nb_output_files <= 0) {
//        av_log(NULL, AV_LOG_FATAL, "At least one output file must be specified\n");
//        ffmpeg_cleanup(1);
//        return 1;
//    }
//
////     if (nb_input_files == 0) {
////         av_log(NULL, AV_LOG_FATAL, "At least one input file must be specified\n");
////         exit_program(1);
////     }
//
//    current_time = ti = getutime();
//    if (transcode() < 0) {
//        ffmpeg_cleanup(1);
//        return 1;
//    }
//    ti = getutime() - ti;
//    av_log(NULL, AV_LOG_FATAL, "Transcode has Finished\n");
//    // if (do_benchmark) {
//    //     printf("bench: utime=%0.3fs\n", ti / 1000000.0);
//    // }
//    // av_log(NULL, AV_LOG_DEBUG, "%"PRIu64" frames successfully decoded, %"PRIu64" decoding errors\n",
//    //       decode_error_stat[0], decode_error_stat[1]);
//    //if ((decode_error_stat[0] + decode_error_stat[1]) * max_error_rate < decode_error_stat[1])
//    // {    exit_program(69); return 69;}
//    ///exit_program(received_nb_signals ? 255 : main_return_code);
//    ffmpeg_cleanup(0);
//    return main_return_code;
//}


//JNIEXPORT void JNICALL
//Java_com_alibaba_ailab_ffmpegso_MainActivity_addWatermark
//        (JNIEnv *env, jobject jobj, jint jlen, jobjectArray jobjArray) {
//    //转码
//    //将java的字符串数组转成C字符串
//    int argc = jlen;
//    //开辟内存空间
//    char **argv = (char **) malloc(sizeof(char *) * argc);
//
//    //填充内容
//    for (int i = 0; i < argc; ++i) {
//        jstring str = static_cast<jstring>(env->GetObjectArrayElement(jobjArray, i));
//        const char *tem = env->GetStringUTFChars(str, 0);
//        argv[i] = (char *) malloc(sizeof(char) * 1024);
//        strcpy(argv[i], tem);
//    }
//
//    //开始转码(底层实现就是只需命令)
//    ffmpegmain(argc, argv);
//
//    //释放内存空间
//    for (int i = 0; i < argc; ++i) {
//        free(argv[i]);
//    }
//
//    //释放数组
//    free(argv);
//}

}
//extern "C"
//JNIEXPORT jstring JNICALL
//Java_com_alibaba_ailab_ffmpegso_MainActivity_addWatermark(JNIEnv *env, jobject thiz, jint argc,
//                                                          jobjectArray argv) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
//}