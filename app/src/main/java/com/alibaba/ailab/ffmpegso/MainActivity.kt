package com.alibaba.ailab.ffmpegso

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.ailab.ffmpegso.FFmpegHandler.*
import com.alibaba.ailab.ffmpegso.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    private var ffmpegHandler: FFmpegHandler? = null
    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_BEGIN -> {
                    binding.progressBar!!.visibility = View.VISIBLE
                    //layoutVideoHandle!!.visibility = View.GONE
                }
                MSG_FINISH -> {
                    binding.progressBar!!.visibility = View.GONE
                   // layoutVideoHandle!!.visibility = View.VISIBLE
//                    if (isJointing) {
//                        isJointing = false
//                        FileUtil.deleteFile(outputPath1)
//                        FileUtil.deleteFile(outputPath2)
//                        FileUtil.deleteFile(listPath)
//                    }
                    if (!outputPath.isNullOrEmpty() && !this@MainActivity.isDestroyed) {
                        Toasty.showToast("Save to:$outputPath")
                        outputPath = ""
                    }
                }
                MSG_PROGRESS -> {
                    val progress = msg.arg1
                    val duration = msg.arg2
                    if (progress > 0) {
                        binding.txtProgress!!.visibility = View.VISIBLE
                        val percent = if (duration > 0) "%" else ""
                        val strProgress = progress.toString() + percent
                        binding.txtProgress!!.text = strProgress
                    } else {
                        binding.txtProgress!!.visibility = View.INVISIBLE
                    }
                }
                else -> {
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        // Example of a call to a native method
        //binding.sampleText.text = stringFromJNI()
        ffmpegHandler = FFmpegHandler(mHandler)
        //调用ffmpegInfo()
        binding.sampleText.text = ffmpegInfo()
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Toast.makeText(MainActivity@this,"点击了",Toast.LENGTH_LONG).show()
            PermissionManager.requestPermission(MainActivity@this,object :PermissionManager.OnPermissionListener{
                override fun onResultSuccess() {
                    //  var path=it.data
                    val uri: Uri? = it.data?.data
                    var path=GetFilePathFromUri.getFileAbsolutePath(baseContext,uri)
                    Log.e("ssssAAsss",path)
                    addWatermark(path)
                }
            })
        }
        binding.ffmpeg.setOnClickListener {
            selectFile()
        }
    }

    private fun selectFile() {
        var intent: Intent?
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) { //19以后这个api不可用，demo这里简单处理成图库选择图片
            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
        } else {
            intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        }
        val bundle = Bundle()
        intent.putExtras(bundle)
        activityResultLauncher?.launch(intent)
    }

    /**
     * A native method that is implemented by the 'ffmpegso' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    //调用ffmpegInfo
    external fun ffmpegInfo(): String?

    //添加水印
   // external fun addWatermark(argc: Int, argv: Array<String?>?): String?

    companion object {
        // Used to load the 'ffmpegso' library on application startup.
        init {
            System.loadLibrary("ffmpegso")
            System.loadLibrary("media-handle")

        }
        private var outputPath :String ?= null
        private val PATH = AppFileHelper.appDataPath
    }

    fun addWatermark(path:String) {
        if (!FileUtil.checkFileExist(path)) {
            return
        }
        if (!FileUtil.isVideo(path)) {
            Toasty.showToast(getString(R.string.wrong_video_format))
            return
        }
        val suffix = FileUtil.getFileSuffix(path)
        if (suffix == null || suffix.isEmpty()) {
            return
        }
//        outputPath = PATH + File.separator + "transformVideo.mp4"
//        var commandLine: Array<String>? = FFmpegUtil.transformVideo(path, outputPath)
        var commandLine: Array<String>? = null
        var bitRate = 500
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val mBitRate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
        if (mBitRate != null && mBitRate.isNotEmpty()) {
            val probeBitrate = Integer.valueOf(mBitRate)
            bitRate = probeBitrate / 1000 / 100 * 100
        }
        //1:top left 2:top right 3:bottom left 4:bottom right
        val location = 1
        val offsetXY = 10
        val photo = PATH + File.separator + "hello.png"
        outputPath = PATH + File.separator + "photoMark.mp4"
        commandLine = FFmpegUtil.addWaterMarkImg(path, photo, location, bitRate, offsetXY, outputPath)
//        when (waterMarkType) {
//            TYPE_IMAGE// image
//            -> {
//                val photo = PATH + File.separator + "hello.png"
//                outputPath = PATH + File.separator + "photoMark.mp4"
//                commandLine = FFmpegUtil.addWaterMarkImg(srcFile, photo, location, bitRate, offsetXY, outputPath)
//            }
//            TYPE_GIF// gif
//            -> {
//                val gifPath = PATH + File.separator + "ok.gif"
//                outputPath = PATH + File.separator + "gifWaterMark.mp4"
//                commandLine = FFmpegUtil.addWaterMarkGif(srcFile, gifPath, location, bitRate, offsetXY, outputPath)
//            }
//            TYPE_TEXT// text
//            -> {
//                val text = "Hello,FFmpeg"
//                val textPath = PATH + File.separator + "text.png"
//                val result = BitmapUtil.textToPicture(textPath, text, Color.BLUE, 20)
//                outputPath = PATH + File.separator + "textMark.mp4"
//                commandLine = FFmpegUtil.addWaterMarkImg(srcFile, textPath, location, bitRate, offsetXY, outputPath)
//            }
//            else -> {
//            }
//        }
//        val str =
//            "ffmpeg -i " + ipFile.getAbsolutePath().toString() + " -i " + wmFile.getAbsolutePath()
//                .toString() + " -filter_complex overlay=480:10 " + opFile.getAbsolutePath()
//        val argv = str.split(" ").toTypedArray()
//        val argc = argv.size
//        object : Thread() {
//            override fun run() {
//                //AudioPlayer.player.ffmpegCmdUtil(argc, argv)
//                Log.i("main", "------加水印完成-------")
//            }
//        }.start()

        if (ffmpegHandler != null && commandLine != null) {
            ffmpegHandler!!.executeFFmpegCmd(commandLine)
        }
    }


}