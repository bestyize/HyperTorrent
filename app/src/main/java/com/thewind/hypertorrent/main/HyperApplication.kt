package com.thewind.hypertorrent.main

import android.app.Application
import com.xunlei.download.init.DownloadTaskHelper

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
        DownloadTaskHelper.init()
    }

}

private const val TAG = "HyperApplication"

