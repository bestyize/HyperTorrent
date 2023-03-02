package com.xunlei.download.init

import android.os.Build
import android.util.Log
import com.thewind.hypertorrent.main.globalApplication
import com.xunlei.downloadlib.XLDownloadManager
import com.xunlei.downloadlib.parameter.InitParam
import java.util.*

/**
 * @author: read
 * @date: 2023/3/3 上午12:21
 * @description:
 */
object DownloadTaskHelper {

    private const val TAG = "DownloadTaskHelper"

    fun init() {
        System.loadLibrary("xl_thunder_sdk")
        val initResult = XLDownloadManager.getInstance().init(globalApplication.applicationContext, InitParam().apply {
            this.mAppVersion = "7.60.0.8385"
            this.mGuid = UUID.randomUUID().toString()
            this.mLogSavePath = globalApplication.filesDir.absolutePath
            this.mPermissionLevel = 3
            this.mStatCfgSavePath = globalApplication.filesDir.absolutePath
            this.mStatSavePath = globalApplication.filesDir.absolutePath
        })
        if (initResult == 9900) {
            XLDownloadManager.getInstance().setOSVersion(Build.VERSION.INCREMENTAL)
            XLDownloadManager.getInstance().setUserId("Yt4vsji-qngamdRo")
            XLDownloadManager.getInstance().setSpeedLimit(-1, -1)
        }
        Log.i(TAG, "initXl = $initResult")
    }
}