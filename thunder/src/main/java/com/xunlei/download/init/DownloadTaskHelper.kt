package com.xunlei.download.init

import android.content.Context
import android.os.Build
import android.util.Log
import com.xunlei.download.provider.TorrentTaskManager
import com.xunlei.downloadlib.XLDownloadManager
import com.xunlei.downloadlib.parameter.InitParam
import java.util.*

/**
 * @author: read
 * @date: 2023/3/3 上午12:21
 * @description:
 */
object DownloadTaskHelper {

    private const val TAG = "[xunlei]DownloadTaskHelper"

    fun init(context: Context) {
        System.loadLibrary("xl_thunder_sdk")
        val initResult = XLDownloadManager.getInstance().init(context, InitParam().apply {
            this.mAppVersion = "7.60.0.8385"
            this.mGuid = UUID.randomUUID().toString()
            this.mLogSavePath = context.filesDir.absolutePath
            this.mPermissionLevel = 3
            this.mStatCfgSavePath = context.filesDir.absolutePath
            this.mStatSavePath = context.filesDir.absolutePath
        })
        if (initResult == 9000) {
            XLDownloadManager.getInstance().setOSVersion(Build.VERSION.INCREMENTAL)
            XLDownloadManager.getInstance().setUserId("Yt4vsji-qngamdRo")
            XLDownloadManager.getInstance().setSpeedLimit(-1, -1)
        }
        TorrentTaskManager.instance.init()
        Log.i(TAG, "initXl = $initResult")
    }
}