package com.thewind.hyper.main

import android.app.Application
import android.content.pm.ApplicationInfo
import com.tencent.mmkv.MMKV
import com.thewind.hyper.config.ConfigManager
import com.thewind.user.login.AccountHelper
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
        MMKV.initialize(this)
        ConfigManager.initConfig()
        TorrentDownloadModule.init(this)
        AccountHelper.updateUserInfo()
    }

}

fun getVersionCode(): Int {
    return globalApplication.applicationContext?.packageManager?.getPackageInfo(globalApplication.packageName, 0)?.versionCode?:100
}

fun isDebug() = globalApplication.applicationInfo.flags.and(ApplicationInfo.FLAG_DEBUGGABLE) != 0

private const val TAG = "HyperApplication"

