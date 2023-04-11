package com.thewind.hypertorrent.config

import com.tencent.mmkv.MMKV
import com.thewind.hypertorrent.config.model.AppConfig
import com.thewind.hypertorrent.config.service.AppConfigServiceHelper
import com.thewind.util.fromJson
import com.thewind.util.toJson

/**
 * @author: read
 * @date: 2023/4/12 上午2:12
 * @description:
 */
object ConfigManager {

    var appConfig: AppConfig? = null

    fun initConfig() {
        appConfig = MMKV.defaultMMKV().decodeString("app_config")?.fromJson()
        val nConfig = AppConfigServiceHelper.updateConfig()
        if (nConfig.isValid()) {
            MMKV.defaultMMKV().encode("app_config", appConfig?.toJson()?:"")
            appConfig = nConfig
        }

    }

}