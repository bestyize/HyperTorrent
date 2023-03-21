package com.thewind.hypertorrent.main.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.ActivityMainBinding
import com.thewind.local.LocalFileFragment
import com.thewind.torrent.search.TorrentSearchFragment
import com.thewind.torrent.search.recommend.TorrentSearchRecommendFragment
import com.thewind.util.ViewUtils
import com.thewind.util.spToPx
import com.thewind.util.toJson
import com.thewind.util.toast
import com.xunlei.download.config.TORRENT_DIR
import com.xunlei.download.config.TORRENT_FILE_DIR
import com.xunlei.download.provider.TaskInfo
import com.xunlei.download.provider.TorrentTaskListener
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val torrentSearchFragment by lazy { TorrentSearchRecommendFragment.newInstance()}
    private val localFileFragment by lazy { LocalFileFragment.newInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermission()
        ViewUtils.setStatusBarColor(this, true)
        supportActionBar?.hide()
        initView()
        initVView2()
    }

    private val taskListener = object : TorrentTaskListener {
        override fun onStart(taskInfo: TaskInfo) {
            Log.i(TAG, "onStart, taskInfo = ${taskInfo.toJson()}")
        }

        override fun onDownloading(taskInfo: TaskInfo) {
            Log.i(TAG, "onStart, taskInfo = ${taskInfo.toJson()}")
        }

        override fun onError(taskInfo: TaskInfo) {
            Log.i(TAG, "onError, taskInfo = ${taskInfo.toJson()}")
        }

        override fun onSuccess(taskInfo: TaskInfo) {
            Log.i(TAG, "onSuccess, taskInfo = ${taskInfo.toJson()}")
        }
    }

    private fun initView() {
        Log.i(TAG, "initView")
//        binding.btnAddMagnetTask.setOnClickListener {
//            val hash =
//                com.xunlei.download.config.TorrentUtil.getMagnetHash(binding.etHash.text.toString())
//            taskId = TorrentTaskHelper.instance.addMagnetTask(binding.etHash.text.toString())
//            TorrentRecordManager.instance.registerTaskListener(hash, taskListener)
//            Toast.makeText(applicationContext, "创建磁力链接任务， taskId = $taskId", Toast.LENGTH_LONG)
//                .show()
//        }
//
//        binding.btnParseTorrent.setOnClickListener {
//            val fullPath =
//                com.xunlei.download.config.TORRENT_DIR + com.xunlei.download.config.TorrentUtil.getMagnetHash(
//                    binding.etHash.text.toString()
//                ) + ".torrent"
//            val torrentInfo = TorrentTaskHelper.instance.getTorrentInfo(fullPath)
//            Toast.makeText(applicationContext, "解析磁力文件， fullPath = $fullPath", Toast.LENGTH_LONG)
//                .show()
//            binding.tvTorrentInfo.text = torrentInfo.toJson()
//        }
//
//        binding.btnPauseDown.setOnClickListener {
//            TorrentTaskHelper.instance.pauseTask(taskId)
//        }
//
//        binding.btnDownload.setOnClickListener {
//            val n = binding.etSelectedFile.text.toString()
//            val fullPath =
//                com.xunlei.download.config.TORRENT_DIR + com.xunlei.download.config.TorrentUtil.getMagnetHash(
//                    binding.etHash.text.toString()
//                ) + ".torrent"
//            val torrentInfo = TorrentTaskHelper.instance.getTorrentInfo(fullPath)
//            if (n.isEmpty()) {
//                val selectedFileList: MutableList<Int> = mutableListOf<Int>()
//                torrentInfo.mSubFileInfo?.forEach {
//                    selectedFileList.add(it.mFileIndex)
//                }
//                taskId = TorrentTaskHelper.instance.addTorrentTask(
//                    torrentInfo = torrentInfo,
//                    selectedFileList = selectedFileList
//                )
//            } else {
//                val mutableList: MutableList<Int> = mutableListOf<Int>().apply { add(n.toInt()) }
//                taskId = TorrentTaskHelper.instance.addTorrentTask(
//                    torrentInfo = torrentInfo,
//                    selectedFileList = mutableList
//                )
//            }
//            TorrentRecordManager.instance.registerTaskListener(torrentInfo.mInfoHash, taskListener)
//            Toast.makeText(applicationContext, "下载种子的文件， taskId = $taskId", Toast.LENGTH_LONG)
//                .show()
//        }
//
//        binding.btnQueryTaskState.setOnClickListener {
//            val info = TorrentTaskHelper.instance.getTaskInfo(taskId)
//            binding.tvTaskInfo.text = info.toJson()
//        }
//        binding.btnQueryTaskList.setOnClickListener {
//            val fullPath =
//                com.xunlei.download.config.TORRENT_DIR + com.xunlei.download.config.TorrentUtil.getMagnetHash(
//                    binding.etHash.text.toString()
//                ) + ".torrent"
//            TorrentSelectDialogFragment.newInstance(fullPath).showNow(supportFragmentManager, "ccc")
//        }
    }

    private fun initVView2() {
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, localFileFragment).commitNowAllowingStateLoss()
        supportFragmentManager.beginTransaction().hide(localFileFragment).commitNowAllowingStateLoss()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, torrentSearchFragment).commitNowAllowingStateLoss()
        binding.mainItemMain.setOnCheckedChangeListener { buttonView, isChecked ->
            handleMainTabChecked(buttonView, isChecked)
            if (isChecked) {
                supportFragmentManager.beginTransaction().show(torrentSearchFragment).commitNowAllowingStateLoss()
            } else {
                supportFragmentManager.beginTransaction().hide(torrentSearchFragment).commitNowAllowingStateLoss()
            }
        }
        binding.mainItemLocal.setOnCheckedChangeListener { buttonView, isChecked ->
            handleMainTabChecked(buttonView, isChecked)
            if (isChecked) {
                supportFragmentManager.beginTransaction().show(localFileFragment).commitNowAllowingStateLoss()
            } else {
                supportFragmentManager.beginTransaction().hide(localFileFragment).commitNowAllowingStateLoss()
            }
        }
        binding.mainItemMy.setOnCheckedChangeListener { buttonView, isChecked ->
            handleMainTabChecked(buttonView, isChecked)
        }

    }
    private fun handleMainTabChecked(button: CompoundButton, checked: Boolean) {
        button.textSize = if (checked) 20f else 18f
        button.typeface = if (checked) Typeface.create(Typeface.DEFAULT, Typeface.BOLD) else  Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    }



    private fun requestPermission() {
        initDir()
        // 通过api判断手机当前版本号
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 安卓11，判断有没有“所有文件访问权限”权限
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + application.packageName)
                startActivityForResult(intent, 100)
            }
        } else {
            // 安卓6 判断有没有读写权限权限
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) === PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                100
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                toast("存储权限获取失败, 无法正常搜索下载")
            } else {
                initDir()
            }
        }
    }

    private fun initDir() {
        var file = File(TORRENT_FILE_DIR)
        if (!file.exists()) {
            file.mkdirs()
        }
        file = File(TORRENT_DIR)
        if (!file.exists()) {
            file.mkdirs()
        }
    }
}

private const val TAG = "[main]MainActivity"