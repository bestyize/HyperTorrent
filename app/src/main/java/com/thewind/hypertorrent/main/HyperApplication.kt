package com.thewind.hypertorrent.main

import android.app.Application
//import com.tencent.mmkv.MMKV
import com.xunlei.download.init.TorrentDownloadModule

/**
 * @author: read
 * @date: 2023/2/16 上午3:19
 * @description:
 */

lateinit var globalApplication: Application

class HyperApplication : Application() {

    override fun onCreate() {
        globalApplication = this
        super.onCreate()
//        MMKV.initialize(this)
        TorrentDownloadModule.init(this)
    }

}

private const val TAG = "HyperApplication"

