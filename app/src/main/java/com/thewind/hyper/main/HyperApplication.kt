package com.thewind.hyper.main

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
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
        ARouter.init(this)
        ConfigManager.initConfig()
        TorrentDownloadModule.init(this)
        AccountHelper.updateUserInfo()
    }

}

private const val TAG = "HyperApplication"

