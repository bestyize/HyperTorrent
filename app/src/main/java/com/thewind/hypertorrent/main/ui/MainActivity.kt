package com.thewind.hypertorrent.main.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.ActivityMainBinding
import com.thewind.util.toJson

import com.thewind.util.toast
import com.xunlei.download.provider.TaskInfo
import com.xunlei.download.provider.TorrentTaskHelper
import com.xunlei.download.provider.TorrentTaskListener
import com.xunlei.download.provider.TorrentTaskManager


class MainActivity : AppCompatActivity() {

    var taskId: Long = -1

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        requestPermission()
    }

    private val taskListener =  object : TorrentTaskListener {
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
        binding.btnAddMagnetTask.setOnClickListener {
            val hash = com.xunlei.download.config.TorrentTaskHelper.getMagnetHash(binding.etHash.text.toString())
            taskId = TorrentTaskHelper.instance.addMagnetTask(binding.etHash.text.toString())
            TorrentTaskManager.instance.registerTaskListener(hash, taskListener)
            Toast.makeText(applicationContext, "创建磁力链接任务， taskId = $taskId", Toast.LENGTH_LONG).show()
        }

        binding.btnParseTorrent.setOnClickListener {
            val fullPath = com.xunlei.download.config.TORRENT_DIR + com.xunlei.download.config.TorrentTaskHelper.getMagnetHash(binding.etHash.text.toString()) + ".torrent"
            val torrentInfo = TorrentTaskHelper.instance.getTorrentInfo(fullPath)
            Toast.makeText(applicationContext, "解析磁力文件， fullPath = $fullPath", Toast.LENGTH_LONG).show()
            binding.tvTorrentInfo.text = torrentInfo.toJson()
        }

        binding.btnPauseDown.setOnClickListener {
            TorrentTaskHelper.instance.pauseTask(taskId)
        }

        binding.btnDownload.setOnClickListener {
            val n = binding.etSelectedFile.text.toString()
            val fullPath = com.xunlei.download.config.TORRENT_DIR + com.xunlei.download.config.TorrentTaskHelper.getMagnetHash(binding.etHash.text.toString()) + ".torrent"
            val torrentInfo = TorrentTaskHelper.instance.getTorrentInfo(fullPath)
            if (n.isEmpty()) {
                taskId = TorrentTaskHelper.instance.addTorrentTask(torrentInfo)
            } else {
                val mutableList: MutableList<Int> = mutableListOf<Int>().apply { add(n.toInt()) }
                taskId = TorrentTaskHelper.instance.addTorrentTask(torrentInfo = torrentInfo, selectedFileList = mutableList)
            }
            TorrentTaskManager.instance.registerTaskListener(torrentInfo.mInfoHash, taskListener)
            Toast.makeText(applicationContext, "下载种子的文件， taskId = $taskId", Toast.LENGTH_LONG).show()
        }

        binding.btnQueryTaskState.setOnClickListener {
            val info = TorrentTaskHelper.instance.getTaskInfo(taskId)
            binding.tvTaskInfo.text = info.toJson()
        }
        binding.btnQueryTaskList.setOnClickListener {
            supportFragmentManager.beginTransaction().add(R.id.fcv_task_list, DownloadListFragment.newInstance()).commitNow()
        }
    }


    private fun requestPermission() {
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
                toast("存储权限获取失败")
            }
        }
    }
}

private const val TAG = "[main]MainActivity"