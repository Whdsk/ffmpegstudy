package com.alibaba.ailab.ffmpegso

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.ailab.ffmpegso.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    private var ffmpegHandler: FFmpegHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Example of a call to a native method
        //binding.sampleText.text = stringFromJNI()

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
    external fun addWatermark(argc: Int, argv: Array<String?>?): String?

    companion object {
        // Used to load the 'ffmpegso' library on application startup.
        init {
            System.loadLibrary("ffmpegso")
        }
        private var outputPath :String ?= null
        private val PATH = AppFileHelper.appDataPath
    }

    fun addWatermark(path:String) {
        var commandLine: Array<String>? = null
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
        outputPath = PATH + File.separator + "transformVideo.mp4"
        commandLine = FFmpegUtil.transformVideo(path, outputPath)
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