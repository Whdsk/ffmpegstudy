# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
#上面已经把 so 和 头文件 放置到对应的目录中了，但是编译器是不会把它们编译、链接、并打包到 Apk 中的，我们还需要在 CMakeLists.txt 中显性的把相关的 so 添加和链接起来
cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("ffmpegso")


# 支持gnu++11
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# 1. 定义so库和头文件所在目录，方面后面使用
set(ffmpeg_lib_dir ${CMAKE_SOURCE_DIR}/../libs/${ANDROID_ABI})
set(ffmpeg_head_dir ${CMAKE_SOURCE_DIR})

# 2. 添加头文件目录
include_directories(${ffmpeg_head_dir}/ffmpeg)
include_directories(${ffmpeg_head_dir}/include)
#include_directories(src/main/cpp/metadata)
include_directories(${ffmpeg_head_dir}/metadata)
add_library( # Sets the name of the library.
        media-handle

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).

        ${ffmpeg_head_dir}/ffmpeg/cmdutils.c
        ${ffmpeg_head_dir}/ffmpeg/ffmpeg.c
        ${ffmpeg_head_dir}/ffmpeg/ffmpeg_filter.c
        ${ffmpeg_head_dir}/ffmpeg/ffmpeg_opt.c
        ${ffmpeg_head_dir}/ffmpeg_cmd.c
        ${ffmpeg_head_dir}/ffmpeg/ffprobe.c
        ${ffmpeg_head_dir}/ffmpeg/ffmpeg_hw.c
        ${ffmpeg_head_dir}/audio_player.cpp
        ${ffmpeg_head_dir}/opensl_audio_player.cpp
        ${ffmpeg_head_dir}/video_player.cpp
        ${ffmpeg_head_dir}/ffmpeg_pusher.cpp
        ${ffmpeg_head_dir}/video_filter.c
        ${ffmpeg_head_dir}/ffprobe_cmd.cpp
        ${ffmpeg_head_dir}/cut_video.cpp
        ${ffmpeg_head_dir}/visualizer/fft.cpp
        ${ffmpeg_head_dir}/visualizer/fixed_fft.cpp
        ${ffmpeg_head_dir}/visualizer/block_queue.c
        ${ffmpeg_head_dir}/visualizer/frank_visualizer.cpp
        ${ffmpeg_head_dir}/visualizer/frank_visualizer_jni.cpp
        ${ffmpeg_head_dir}/visualizer/window.cpp
        ${ffmpeg_head_dir}/metadata/media_retriever.cpp
        ${ffmpeg_head_dir}/metadata/media_retriever_jni.cpp
        ${ffmpeg_head_dir}/metadata/metadata_util.c
        ${ffmpeg_head_dir}/metadata/ffmpeg_media_retriever.c
        )

if(${CMAKE_ANDROID_ARCH_ABI} MATCHES "armeabi-v7a")
    include_directories(src/main/cpp/include/armeabi-v7a)
    message("This is armeabi-v7a")
elseif(${CMAKE_ANDROID_ARCH_ABI} MATCHES "arm64-v8a")
    include_directories(src/main/cpp/include/arm64-v8a)
    message("This is arm64-v8a")
endif()

# 3. 添加ffmpeg相关的so库
add_library( avutil
        SHARED
        IMPORTED )
set_target_properties( avutil
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavutil.so )

add_library( swresample
        SHARED
        IMPORTED )
set_target_properties( swresample
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libswresample.so )

add_library( avcodec
        SHARED
        IMPORTED )
set_target_properties( avcodec
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavcodec.so )

add_library( avfilter
        SHARED
        IMPORTED)
set_target_properties( avfilter
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavfilter.so )

add_library( swscale
        SHARED
        IMPORTED)
set_target_properties( swscale
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libswscale.so )

add_library( avformat
        SHARED
        IMPORTED)
set_target_properties( avformat
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavformat.so )

add_library( avdevice
        SHARED
        IMPORTED)
set_target_properties( avdevice
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavdevice.so )

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
# 配置目标so库编译信息
add_library( # Sets the name of the library.
        ffmpegso

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.
# 查找代码中使用到的系统库
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
# 指定编译目标库时，cmake要链接的库
target_link_libraries( # Specifies the target library.
        media-handle
        # 指定目标库，native-lib 是在上面 add_library 中配置的目标库
        ffmpegso

        # 4. 连接 FFmpeg 相关的库
        avutil
        swresample
        avcodec
        avfilter
        swscale
        avformat
        avdevice

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})