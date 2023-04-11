package com.thewind.hypertorrent.config.model

/**
 * @author: read
 * @date: 2023/4/12 上午2:01
 * @description:
 */
class AppConfig {
    var baseUrl: String? = null

    fun isValid(): Boolean {
        return !baseUrl.isNullOrBlank()
    }
}