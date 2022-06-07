package com.alibaba.ailab.ffmpegso

import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * author gaohangbo
 * date: 2018/7/10 0010.
 */
object AppFileHelper {
    private val OVERM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    private const val TAG = "AppFileHelper"
    val INTERNAL_STORAGE_PATHS = arrayOf("/mnt/", "/emmc/")
    const val DATA_PATH = "data/"
    const val CACHE_PATH = "cache/"
    const val PIC_PATH = "pic/"
    const val CAMERA_PATH = "pic/camera/"
    const val LOG_PATH = "log/"
    const val DOWNLOAD_PATH = "download/"
    const val TEMP_PATH = "temp/"
    fun initStoragePathInternal() {
        val dataDir = FFmpegApplication.getInstance().getExternalFilesDir(DATA_PATH)
        checkAndMakeDir(dataDir)
        val cacheDir = FFmpegApplication.getInstance().getExternalFilesDir(CACHE_PATH)
        checkAndMakeDir(cacheDir)
        val picDir = FFmpegApplication.getInstance().getExternalFilesDir(PIC_PATH)
        checkAndMakeDir(picDir)
        val cameraDir = FFmpegApplication.getInstance().getExternalFilesDir(CAMERA_PATH)
        checkAndMakeDir(cameraDir)
        val logDir = FFmpegApplication.getInstance().getExternalFilesDir(LOG_PATH)
        checkAndMakeDir(logDir)
    }

    private fun checkAndMakeDir(file: File?) {
        if (!file!!.exists()) {
            val result = file.mkdirs()
            Log.i(TAG, "mkdirs  >>>  " + file.absolutePath + "  success  >>  " + result)
        }
    }

    val appDataPath: String
        get() {
            val dataDir = FFmpegApplication.getInstance().getExternalFilesDir(DATA_PATH)
            checkAndMakeDir(dataDir)
            Log.i(TAG, "getAppDataPath  >>>  " + dataDir!!.absolutePath)
            return dataDir.absolutePath
        }
    val appCachePath: String
        get() {
            val dataDir = FFmpegApplication.getInstance().getExternalFilesDir(CACHE_PATH)
            checkAndMakeDir(dataDir)
            Log.i(TAG, "getAppCachePath  >>>  " + dataDir!!.absolutePath)
            return dataDir.absolutePath
        }
    @JvmStatic
    val appPicPath: String
        get() {
            val dataDir = FFmpegApplication.getInstance().getExternalFilesDir(PIC_PATH)
            checkAndMakeDir(dataDir)
            Log.i(TAG, "getAppPicPath  >>>  " + dataDir!!.absolutePath)
            return dataDir.absolutePath
        }
    val cameraPath: String
        get() {
            val dataDir = FFmpegApplication.getInstance().getExternalFilesDir(CAMERA_PATH)
            checkAndMakeDir(dataDir)
            Log.i(TAG, "getCameraPath  >>>  " + dataDir!!.absolutePath)
            return dataDir.absolutePath
        }
    val appLogPath: String
        get() {
            val dataDir = FFmpegApplication.getInstance().getExternalFilesDir(LOG_PATH)
            checkAndMakeDir(dataDir)
            Log.i(TAG, "getCameraPath  >>>  " + dataDir!!.absolutePath)
            return dataDir.absolutePath
        }
    @JvmStatic
    val appDownLoadPath: String
        get() {
            val dataDir = FFmpegApplication.getInstance().getExternalFilesDir(DOWNLOAD_PATH)
            checkAndMakeDir(dataDir)
            Log.i(TAG, "getAppDownLoadPath  >>>  " + dataDir!!.absolutePath)
            return dataDir.absolutePath
        }

    fun createShareImageName(): String {
        return createImageName(false)
    }

    fun createImageName(isJpg: Boolean): String {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.US
        )
        return dateFormat.format(date) + if (isJpg) ".jpg" else ".png"
    }

    fun createCropImageName(): String {
        val date = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.US
        )
        return dateFormat.format(date) + "_crop.png"
    }

    fun deleteFile() {
        var storagePath: String? = null
        //M开始用的filePorider
        if (!OVERM) {
            storagePath = FileUtils.getStoragePath(FFmpegApplication.getInstance(), false)
        }
        if (TextUtils.isEmpty(storagePath)) {
            //没有路径就使用getExternalStorageDirectory
            storagePath = Environment.getExternalStorageDirectory().absolutePath
            if (TextUtils.isEmpty(storagePath)) {
                //依然没法创建路径则使用私有的
                storagePath = FFmpegApplication.getInstance().filesDir.absolutePath
            }
        }
        storagePath = FileUtils.checkFileSeparator(storagePath)
        Log.i(TAG, "storagepath  >>  $storagePath")
        val rootDir = File(storagePath + FFmpegApplication.getInstance().packageName + "/")
        if (rootDir.exists()) {
            FileUtils.deleteFile(storagePath + FFmpegApplication.getInstance().packageName + "/")
        }
    }
}